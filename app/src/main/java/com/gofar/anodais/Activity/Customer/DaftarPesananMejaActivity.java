package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.Customer.DaftarPesananMejaAdapter;
import com.gofar.anodais.Adapter.General.DaftarPengajuanMejaAdapter;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaftarPesananMejaActivity extends AppCompatActivity {

    ArrayList<Reservasi>reservasiList=new ArrayList<>();
    RecyclerView rvdaftarPesananMeja;
    RecyclerView.Adapter daftarPesananMejaAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan_meja);

        rvdaftarPesananMeja=findViewById(R.id.rvpesananmeja);
        layoutManager=new LinearLayoutManager(DaftarPesananMejaActivity.this);
        rvdaftarPesananMeja.setLayoutManager(layoutManager);

        filldata();

    }


    private void filldata()
    {
        try
        {
            String url= Server.URL+"/getpesananmejacustomer.php";
            RequestQueue queue= Volley.newRequestQueue(DaftarPesananMejaActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i=0; i< jsonArray.length(); i ++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            Reservasi reservasi=new Reservasi();

                            reservasi.setNomeja(object.getString("nomeja"));
                            reservasi.setUsername(object.getString("username"));
                            reservasi.setTgl_sewa(object.getString("tglsewa"));
                            reservasi.setId_reservasi(object.getString("id_reservasi"));

                            reservasiList.add(reservasi);

                        }

                        daftarPesananMejaAdapter=new DaftarPesananMejaAdapter(reservasiList, DaftarPesananMejaActivity.this);
                        rvdaftarPesananMeja.setAdapter(daftarPesananMejaAdapter);
                        daftarPesananMejaAdapter.notifyDataSetChanged();

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DaftarPesananMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(DaftarPesananMejaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();

                    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(DaftarPesananMejaActivity.this);
                    String iduser=pref.getString("id",null);

                    params.put("iduser",iduser);

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
}
