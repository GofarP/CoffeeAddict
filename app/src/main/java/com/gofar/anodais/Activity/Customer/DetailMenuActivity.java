package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gofar.anodais.Activity.Admin.DetailPesananActivity;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailMenuActivity extends AppCompatActivity {

    Button btnbelimenu,btnaddjmlmneu,btnkrgjmlmenu;
    EditText txtjmlmenu;
    TextView lblsubtotal,lblnamamenu,lbldeskripsimenu,lblharga;
    ImageView ivdetailmenu;
    int jml=1,stock;
    double harga,jmlsubtotal;
    String catatan="-";

    int Subtotal=0;

    ArrayList<Reservasi>reservasiList=new ArrayList<>();

    MaterialCardView cvCatatan,cvnomeja;
    Spinner spnnomeja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        getSupportActionBar().hide();


        btnaddjmlmneu=findViewById(R.id.btnaddjmlmenu);
        btnkrgjmlmenu=findViewById(R.id.btnkrgjmlmenu);
        btnbelimenu=findViewById(R.id.btnbelimenu);
        txtjmlmenu=findViewById(R.id.txtjmlmenu);
        lblsubtotal=findViewById(R.id.lblsubtotalmenu);
        ivdetailmenu=findViewById(R.id.ivdetailmenu);
        lblnamamenu=findViewById(R.id.lbldetailnamamenu);
        lbldeskripsimenu=findViewById(R.id.lbldetaildeskripsimenu);
        lblharga=findViewById(R.id.lbldetailhrgmenu);
        cvCatatan=findViewById(R.id.cvaddcatatan);
        cvnomeja=findViewById(R.id.cvnomeja);
        spnnomeja=findViewById(R.id.spnnomeja);

        final Bundle bundle=getIntent().getExtras();

        harga=bundle.getDouble("harga");
        stock=bundle.getInt("stock");

        lblnamamenu.setText(bundle.getString("nama"));
        lbldeskripsimenu.setText(bundle.getString("deskripsi"));
        lblharga.setText(NumberFormat.getCurrencyInstance().format(harga));

        if(bundle.getBoolean("pesan_disini")==true)
        {
            cvnomeja.setVisibility(View.VISIBLE);
            fillNoMeja();
        }



        Glide.with(DetailMenuActivity.this)
                .load(Server.URL+"/image/"+bundle.getString("image"))
                .centerCrop()
                .into(ivdetailmenu);


        txtjmlmenu.setText(String.valueOf(jml));

        lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(harga));

        txtjmlmenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String jml=txtjmlmenu.getText().toString().trim();

                    if(jml.equals("")){jml="0";}

                    if(Integer.parseInt(jml)>stock)
                    {
                        jml=String.valueOf(stock);
                        txtjmlmenu.setText(String.valueOf(stock));
                        Toast.makeText(DetailMenuActivity.this, "jumlah tidak cukup", Toast.LENGTH_SHORT).show();
                    }

                    Double subtotal=bundle.getDouble("harga");
                    lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(Integer.parseInt(jml)*subtotal));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnaddjmlmneu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(jml>=stock)
                {
                    txtjmlmenu.setText(String.valueOf(stock));
                    jml=stock;
                    Toast.makeText(DetailMenuActivity.this, "Tidak bisa membeli melebihi jumlah stok", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    txtjmlmenu.setText(String.valueOf(jml+=1));
                    jmlsubtotal=jml*harga;
                    lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(jmlsubtotal));
                }

            }
        });

        btnkrgjmlmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min=Integer.parseInt(txtjmlmenu.getText().toString());
                txtjmlmenu.setText(String.valueOf(jml-=1));
                jmlsubtotal=jml*harga;
                lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(jmlsubtotal));

                if(min<=1)
                {
                    jml=1;
                    txtjmlmenu.setText(String.valueOf(jml));
                    lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(harga));
                }

            }
        });

        btnbelimenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String validasijmlbeli=txtjmlmenu.getText().toString().trim();

                    if(validasijmlbeli.equals("0") || validasijmlbeli.equals(""))
                    {
                        Toast.makeText(DetailMenuActivity.this, "Silahkan isi jumlah menu yang mau dibeli", Toast.LENGTH_SHORT).show();
                    }

                    else if(bundle.getBoolean("pesan_disini")==true)
                    {
                        tambahpesananditempat();
                        catatan="-";

                    }

                    else
                    {
                        tambahkeranjang();
                        catatan="-";
                    }

                }

                catch (Exception e)
                {
                    Toast.makeText(DetailMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        cvCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCatatan();
            }
        });

    }


    public void tambahkeranjang()
    {
            String url_addcart=Server.URL+"addkeranjang.php";

            RequestQueue queue= Volley.newRequestQueue(DetailMenuActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url_addcart, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject obj=new JSONObject(response);
                        int success=obj.getInt("success");

                        if(success==1)
                        {
                            Toast.makeText(DetailMenuActivity.this, obj.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(DetailMenuActivity.this, obj.getString("messages"), Toast.LENGTH_SHORT).show();
                        }


                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailMenuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String id=prefs.getString("id",null);
                    Bundle bundle=getIntent().getExtras();

                    Double subtotal=jml*harga;

                    params.put("idpelanggan",id);
                    params.put("idmenu",bundle.getString("id"));
                    params.put("jumlah",txtjmlmenu.getText().toString());
                    params.put("jenis",bundle.getString("jenis"));
                    params.put("subtotal",subtotal.toString());
                    params.put("catatan",catatan);
                    return params;
                }
            };

            queue.add(request);
    }


    public void addCatatan()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(DetailMenuActivity.this);
        LayoutInflater inflater=LayoutInflater.from(DetailMenuActivity.this);
        View dialogView=inflater.inflate(R.layout.layout_catatan,null);

        builder.setView(dialogView);

        final AlertDialog closedialog=builder.create();


        Button btnadd=dialogView.findViewById(R.id.btntambahcatatan);
        final EditText txtcatatan=dialogView.findViewById(R.id.txttambahcatatan);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!txtcatatan.getText().toString().trim().equals(""))
                {
                    catatan=txtcatatan.getText().toString().trim();
                }

                Toast.makeText(DetailMenuActivity.this, "Catatan Ditambahkan", Toast.LENGTH_SHORT).show();

                closedialog.dismiss();

            }
        });


        closedialog.show();

    }

    public void tambahpesananditempat()
    {
        try
        {


            String url=Server.URL+"addpesanan.php";
            RequestQueue queue=Volley.newRequestQueue(DetailMenuActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(DetailMenuActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailMenuActivity.this, CustomerActivity.class));

                        }

                        else
                        {
                            Toast.makeText(DetailMenuActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(DetailMenuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String>params=new HashMap<>();

                    Bundle bundle=getIntent().getExtras();
                    Double subtotal=Double.parseDouble(txtjmlmenu.getText().toString().trim()) * bundle.getDouble("harga");
                    int index=spnnomeja.getSelectedItemPosition();

                    params.put("idreservasi",reservasiList.get(index).getId_reservasi());
                    params.put("idmenu", bundle.getString("id"));
                    params.put("tanggal",reservasiList.get(index).getTgl_sewa());
                    params.put("jumlah",txtjmlmenu.getText().toString().trim());
                    params.put("catatan",catatan);
                    params.put("subtotal",String.valueOf(subtotal));

                    return params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    private void fillNoMeja()
    {
        try
        {
            String url=Server.URL+"getpengajuanmejacustomer.php";
            RequestQueue queue=Volley.newRequestQueue(DetailMenuActivity.this);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try
                    {
                        ArrayAdapter<String> noMejaAdapter=new ArrayAdapter<>(DetailMenuActivity.this, android.R.layout.simple_spinner_item);

                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        if(jsonArray.length() > 0)
                        {
                            for(int i=0;i < jsonArray.length(); i++)
                            {
                                JSONObject jsonArrayObject=jsonArray.getJSONObject(i);

                                Reservasi reservasi=new Reservasi();

                                reservasi.setId_reservasi(jsonArrayObject.getString("idreservasi"));
                                reservasi.setNomeja(jsonArrayObject.getString("nomeja"));
                                reservasi.setTgl_sewa(jsonArrayObject.getString("tglsewa"));

                                noMejaAdapter.add(jsonArrayObject.getString("nomeja"));

                                reservasiList.add(reservasi);

                                spnnomeja.setAdapter(noMejaAdapter);

                            }

                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(DetailMenuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(DetailMenuActivity.this);
                    params.put("iduser",preferences.getString("id",null));
                    return params;
                }
            };

            queue.add(stringRequest);

        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
