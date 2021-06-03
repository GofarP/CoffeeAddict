package com.gofar.anodais.Fragment.Customer;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Activity.Customer.KeranjangActivity;
import com.gofar.anodais.Adapter.Customer.KeranjangAdapter;
import com.gofar.anodais.Model.Keranjang;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gofar.anodais.Interface.totalInterface;

public class FragKeranjang extends Fragment implements totalInterface  {

    RecyclerView.Adapter keranjangAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView rvkeranjang;
    Button btncheckout;
    public static TextView lbltotal;

    ArrayList<Keranjang> keranjangList=new ArrayList<>();

    public static Double total;

    String alamat="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_keranjang,null,false);

        rvkeranjang=view.findViewById(R.id.rvkeranjang);
        lbltotal=view.findViewById(R.id.lbltotal);
        btncheckout=view.findViewById(R.id.btncheckout);
        layoutManager=new LinearLayoutManager(getActivity());
        rvkeranjang.setLayoutManager(layoutManager);


        rvkeranjang.setHasFixedSize(true);

        fillKeranjang();


        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if(keranjangList.size()==0)
             {
                 Toast.makeText(getActivity(), "Keranjang anda kosong", Toast.LENGTH_SHORT).show();
             }

             else if(alamat.equals(""))
             {
                 alamatDialog();
             }

             else
             {
                 for(int position=0;position < keranjangList.size(); position++)
                 {
                     checkOut(alamat,position);
                 }

                 Toast.makeText(getActivity(), "Sukses Checkout", Toast.LENGTH_SHORT).show();
             }

            }
        });


        return view;
    }



    public void fillKeranjang()
    {
        try
        {
            String urlcart= Server.URL+"getkeranjangitem.php";
            RequestQueue queue= Volley.newRequestQueue(getActivity());
            StringRequest request=new StringRequest(Request.Method.POST, urlcart, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        keranjangList=new ArrayList<>();

                        JSONObject object=new JSONObject(response);
                        JSONArray jsonArray=object.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            total=0.0;

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                Keranjang keranjang=new Keranjang();

                                keranjang.setIdkeranjang(jsonObject.getString("idkeranjang"));
                                keranjang.setIdmenu(jsonObject.getString("idmenu"));
                                keranjang.setIdpelanggan(jsonObject.getString("idpelanggan"));
                                keranjang.setNama(jsonObject.getString("nama"));
                                keranjang.setHarga(jsonObject.getDouble("harga"));
                                keranjang.setImage(jsonObject.getString("image"));
                                keranjang.setJenis(jsonObject.getString("jenis"));
                                keranjang.setJumlah(jsonObject.getInt("jumlah"));
                                keranjang.setSubtotal(jsonObject.getDouble("subtotal"));
                                keranjang.setStock(jsonObject.getInt("stock"));
                                keranjang.setCatatan(jsonObject.getString("catatan"));

                                total+=jsonObject.getDouble("subtotal");

                                keranjangList.add(keranjang);

                            }

                            lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));

                            keranjangAdapter=new KeranjangAdapter(keranjangList,getActivity(),FragKeranjang.this);
                            rvkeranjang.setAdapter(keranjangAdapter);
                            keranjangAdapter.notifyDataSetChanged();

                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());

                    params.put("idpelanggan",pref.getString("id",null));

                    return params;
                }
            };

            queue.add(request);

        }

        catch(Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void checkOut(final String alamat,final int position)
    {
        try
        {
            String url_checkout=Server.URL+"checkout.php";
            RequestQueue queue=Volley.newRequestQueue(getActivity());
            StringRequest request=new StringRequest(Request.Method.POST, url_checkout, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            keranjangList.clear();
                            keranjangAdapter.notifyDataSetChanged();
                            lbltotal.setText(NumberFormat.getCurrencyInstance().format(0));
                        }

                        else
                        {
                            Toast.makeText(getActivity(), object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                   Map <String,String> params=new HashMap<>();

                   params.put("idmenu",keranjangList.get(position).getIdmenu());
                   params.put("alamat",alamat);
                   params.put("idpelanggan",keranjangList.get(position).getIdpelanggan());
                   params.put("jumlah",String.valueOf(keranjangList.get(position).getJumlah()));
                   params.put("subtotal",String.valueOf(keranjangList.get(position).getSubtotal()));
                   params.put("catatan",String.valueOf(keranjangList.get(position).getCatatan()));

                   return params;
                }

            };

            queue.add(request);
        }

        catch(Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void alamatDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View dialogView=inflater.inflate(R.layout.layout_alamat,null);
        builder.setView(dialogView);

        Button btntambahalamat=dialogView.findViewById(R.id.btntambahalamat);

        final EditText txtalamat=dialogView.findViewById(R.id.txttambahalamat);

        final AlertDialog dialog=builder.create();

        btntambahalamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtalamat.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getActivity(), "Silahkan isi alamat anda", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    alamat=txtalamat.getText().toString().trim();
                    Toast.makeText(getActivity(), "Alamat berhasil ditambahkan, silahkan tekan tombol CheckOut sekali lagi", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }






    @Override
    public void addTotal(Double harga) {
        lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));
    }

    @Override
    public void minTotal(Double harga) {
        lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));
    }

    @Override
    public void deleteTotal(Double subtotal) {
        lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));
    }

    @Override
    public void textTotal(Double harga) {
        lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));
    }

}
