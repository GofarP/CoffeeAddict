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
import com.gofar.anodais.Adapter.Customer.PemberitahuanCustomerAdapter;
import com.gofar.anodais.Model.Notifikasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifikasiActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    List<Notifikasi>notifikasiList=new ArrayList<>();
    RecyclerView.LayoutManager menuLayoutManager;
    RecyclerView rvnotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        rvnotifikasi=findViewById(R.id.rvnotifikasi);

        getSupportActionBar().setTitle("Notifikasi");

        menuLayoutManager=new LinearLayoutManager(this);
        rvnotifikasi.setLayoutManager(menuLayoutManager);
        rvnotifikasi.setHasFixedSize(true);

        getNotifikasi();

    }


    public void getNotifikasi()
    {
        try
        {
            String url_notif= Server.URL+"getnotifikasi.php";
            RequestQueue queue= Volley.newRequestQueue(this);
            StringRequest request=new StringRequest(Request.Method.POST, url_notif, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        notifikasiList=new ArrayList<>();
                        JSONObject object=new JSONObject(response);
                        JSONArray jsonArray=object.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);

                                Notifikasi notifikasi=new Notifikasi();

                                notifikasi.setIdNotifikasi(jsonObject.getString("id_notifikasi"));
                                notifikasi.setIduser(jsonObject.getString("id_user"));
                                notifikasi.setPesan(jsonObject.getString("pesan"));
                                notifikasi.setTanggal(jsonObject.getString("tanggal"));

                                notifikasiList.add(notifikasi);
                            }

                            adapter=new PemberitahuanCustomerAdapter(NotifikasiActivity.this,notifikasiList);
                            rvnotifikasi.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(NotifikasiActivity.this,"json error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NotifikasiActivity.this,"volley error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(NotifikasiActivity.this);
                    String iduser=preferences.getString("id",null);

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
