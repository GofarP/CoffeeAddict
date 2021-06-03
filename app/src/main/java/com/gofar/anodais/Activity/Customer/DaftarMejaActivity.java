package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.General.MejaAdapter;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DaftarMejaActivity extends AppCompatActivity {

    ArrayList<Reservasi>reservasiList=new ArrayList<>();
    RecyclerView rvreservasicustomer;
    RecyclerView.Adapter mejaadapter;
    RecyclerView.LayoutManager mejaLayoutManager;

    TextView lbltglbesok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_meja);
        rvreservasicustomer=findViewById(R.id.rvcustomerreservasi);
        lbltglbesok=findViewById(R.id.lbltglbesok);

        mejaLayoutManager=new GridLayoutManager(getApplicationContext(),3);
        rvreservasicustomer.setLayoutManager(mejaLayoutManager);
        rvreservasicustomer.setHasFixedSize(true);

        fillMeja();

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        cal.add(Calendar.DATE,1);
        Date tomorrow=cal.getTime();
        String strDate = df.format(tomorrow);

        lbltglbesok.setText(strDate);

    }

    public void fillMeja()
    {
        try
        {
            String url_getmeja= Server.URL+"getmeja.php";
            RequestQueue queue= Volley.newRequestQueue(DaftarMejaActivity.this);
            JsonArrayRequest jar=new JsonArrayRequest(url_getmeja, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    reservasiList.clear();

                    for(int i=0;i<response.length();i++)
                    {
                        try
                        {
                            JSONObject object=response.getJSONObject(i);
                            Reservasi reservasi=new Reservasi();
                            reservasi.setId_meja(object.getString("id_meja"));
                            reservasi.setNomeja(object.getString("no_meja"));
                            reservasi.setMejaImage(object.getString("images"));
                            reservasi.setJenis(object.getString("jenis"));

                            reservasiList.add(reservasi);
                        }

                        catch (Exception e)
                        {
                            Toast.makeText(DaftarMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    mejaadapter=new MejaAdapter(reservasiList, DaftarMejaActivity.this,2);
                    rvreservasicustomer.setAdapter(mejaadapter);
                    mejaadapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DaftarMejaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jar);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
