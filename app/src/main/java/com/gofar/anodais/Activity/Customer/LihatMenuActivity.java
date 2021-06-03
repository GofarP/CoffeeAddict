package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.Customer.LihatMenuAdapter;
import com.gofar.anodais.Model.MenuPengajuan;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LihatMenuActivity extends AppCompatActivity {

    RecyclerView rvlihatmenu;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter lihatMenuAdapter;

    String JENIS_VIEW="Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_menu);

        rvlihatmenu=findViewById(R.id.rvlihatmenu);

        layoutManager=new LinearLayoutManager(LihatMenuActivity.this);
        rvlihatmenu.setLayoutManager(layoutManager);
        rvlihatmenu.setHasFixedSize(true);

        Bundle bundle=getIntent().getExtras();
        String idreservasi=bundle.getString("idreservasi");
        getMenu(idreservasi);
    }


    private void getMenu(final String idreservasi)
    {
        try
        {
            String url= Server.URL+"getpengajuanpesanan.php";
            RequestQueue queue= Volley.newRequestQueue(LihatMenuActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        ArrayList<MenuPengajuan>pengajuanList=new ArrayList<>();
                        JSONObject object=new JSONObject(response);
                        JSONArray jsonArray=object.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            for(int i=0; i < jsonArray.length();i++)
                            {
                                JSONObject jsonArrayObject=jsonArray.getJSONObject(i);

                                MenuPengajuan menuPengajuan=new MenuPengajuan();
                                menuPengajuan.setId_menu(jsonArrayObject.getString("id_menu"));
                                menuPengajuan.setNama(jsonArrayObject.getString("nama"));
                                menuPengajuan.setHarga(jsonArrayObject.getDouble("harga"));
                                menuPengajuan.setJumlah(jsonArrayObject.getInt("jumlah"));
                                menuPengajuan.setImage(jsonArrayObject.getString("image"));
                                menuPengajuan.setSubtotal(jsonArrayObject.getDouble("subtotal"));
                                menuPengajuan.setIdreservasi(jsonArrayObject.getString("id_reservasi"));
                                menuPengajuan.setStock(jsonArrayObject.getInt("stock"));

                                pengajuanList.add(menuPengajuan);
                            }


                            lihatMenuAdapter=new LihatMenuAdapter(LihatMenuActivity.this, pengajuanList, JENIS_VIEW);
                            rvlihatmenu.setAdapter(lihatMenuAdapter);
                            lihatMenuAdapter.notifyDataSetChanged();

                        }


                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(LihatMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LihatMenuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("idreservasi",idreservasi);
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
