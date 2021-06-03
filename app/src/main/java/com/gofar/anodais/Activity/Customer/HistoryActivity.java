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
import com.gofar.anodais.Adapter.Customer.HistoryAdapter;
import com.gofar.anodais.Model.History;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView rvhistory;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;
    ArrayList<History>historyList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvhistory=findViewById(R.id.rvhistory);

        layoutManager=new LinearLayoutManager(this);
        rvhistory.setLayoutManager(layoutManager);
        rvhistory.setHasFixedSize(true);

        fillData();
    }


    private void fillData()
    {
        try
        {
            String url= Server.URL+ "gethistory.php";
            RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        JSONArray jsonArray=object.getJSONArray("data");

                        for(int i=0; i< jsonArray.length(); i++)
                        {
                            JSONObject jsonArrayObject=jsonArray.getJSONObject(i);

                            History history=new History();
                            history.setNama(jsonArrayObject.getString("nama"));
                            history.setHarga(jsonArrayObject.getDouble("harga"));
                            history.setSubtotal(jsonArrayObject.getDouble("subtotal"));
                            history.setJumlah(jsonArrayObject.getInt("jumlah"));
                            history.setTglpesan(jsonArrayObject.getString("tanggal"));
                            history.setImage(jsonArrayObject.getString("image"));

                            historyList.add(history);

                        }

                        historyAdapter=new HistoryAdapter(historyList,getApplicationContext());
                        rvhistory.setAdapter(historyAdapter);
                        historyAdapter.notifyDataSetChanged();
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(HistoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(HistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String iduser=preferences.getString("id",null );

                    params.put("iduser",iduser);

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
