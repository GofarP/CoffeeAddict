package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Activity.Login.LoginActivity;
import com.gofar.anodais.Model.History;
import com.gofar.anodais.R;
import com.gofar.anodais.Preference.Prefrences;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomerActivity extends AppCompatActivity {

    CardView cvdelivery,cvmeja,cvkeranjang,cvdaftarmeja,cvtransaksi,cvnotifikasi,cvdaftarpesananmeja,cvdaftarpengajuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        cekstatusToko();

        cvdelivery=findViewById(R.id.cvdelivery);
        cvmeja=findViewById(R.id.cvmeja);
        cvkeranjang=findViewById(R.id.cvkeranjang);
        cvtransaksi=findViewById(R.id.cvtransaksi);
        cvnotifikasi=findViewById(R.id.cvnotifikasi);
        cvdaftarpesananmeja=findViewById(R.id.cvdaftarpesananmeja);
        cvdaftarpengajuan=findViewById(R.id.cvdaftarpengajuanmeja);


        cvdelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CustomerActivity.this, PesanDeliveryActivity.class));

            }
        });

        cvmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this, DaftarMejaActivity.class));
            }
        });

        cvkeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CustomerActivity.this,KeranjangActivity.class));

            }
        });


        cvdaftarpesananmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this, DaftarPesananMejaActivity.class));
            }
        });

        cvdaftarpengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this, DaftarPengajuanMejaActivity.class));
            }
        });


        cvnotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this, NotifikasiActivity.class));
            }
        });

        cvtransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menulogout:
                Prefrences.clearLoggedInUser(getBaseContext());
                startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void cekstatusToko()
    {
        try
        {
            String url= Server.URL+"cekstatustoko.php";
            RequestQueue queue= Volley.newRequestQueue(CustomerActivity.this);
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {


                    try
                    {
                        JSONObject jsonObject = response.getJSONObject(0);
                        String status =jsonObject.getString("status").equals("Buka") ? "Buka" : "Tutup";

                         if(status.equals("Tutup"))
                         {
                             Dialog builder=new Dialog(CustomerActivity.this);
                             LayoutInflater inflater=LayoutInflater.from(CustomerActivity.this);
                             View view=inflater.inflate(R.layout.layout_toko_tutup, null);
                             builder.setContentView(view);

                             Button btnkeluar=view.findViewById(R.id.btnkeluar);

                             btnkeluar.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     System.exit(0);
                                 }
                             });

                             builder.setCancelable(false);
                             builder.show();

                         }

                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CustomerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonArrayRequest);

        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }






}
