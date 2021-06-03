package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.General.MenuAdapter;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;
import com.gofar.anodais.Model.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PesanDitempatActivity extends AppCompatActivity {

    RecyclerView.Adapter menuadapter;
    RecyclerView.LayoutManager menulayoutManager;
    RecyclerView rvmenu;
    SearchView txtsearch;
    Spinner spnkategori;

    ArrayList<Menu>listMenu=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_ditempat);

        rvmenu=findViewById(R.id.rvallmenu);
        txtsearch=findViewById(R.id.svmenu);
        spnkategori=findViewById(R.id.spnkategorimenu);

        ArrayAdapter<CharSequence>adapterKategori=ArrayAdapter.createFromResource(this,R.array.all_kategori,android.R.layout.simple_spinner_item);
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnkategori.setAdapter(adapterKategori);

        menulayoutManager=new LinearLayoutManager(getApplicationContext());
        rvmenu.setLayoutManager(menulayoutManager);
        rvmenu.setHasFixedSize(true);

        fillMenu();

        txtsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMenu(newText);

                return true;
            }
        });

        spnkategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                        fillMenu();
                        break;
                    case 1:
                        getMakanan();
                        break;
                    case 2:
                        getMinuman();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void fillMenu()
    {
        try
        {
            String url= Server.URL+"getallmenu.php";
            RequestQueue queue= Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    listMenu.clear();

                    for(int i=0; i < response.length(); i++)
                    {
                        try
                        {
                            JSONObject jsonObject=response.getJSONObject(i);
                            Menu menu=new Menu();
                            menu.setId(jsonObject.getString("id_menu"));
                            menu.setNama(jsonObject.getString("nama"));
                            menu.setHarga(jsonObject.getDouble("harga"));
                            menu.setJenis(jsonObject.getString("jenis"));
                            menu.setStatus(jsonObject.getString("status"));
                            menu.setDeskripsi(jsonObject.getString("deskripsi"));
                            menu.setImage(jsonObject.getString("image"));
                            menu.setStock(jsonObject.getInt("stock"));

                            listMenu.add(menu);
                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(PesanDitempatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        menuadapter=new MenuAdapter(listMenu,getApplicationContext(),3);
                        rvmenu.setAdapter(menuadapter);
                        menuadapter.notifyDataSetChanged();

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PesanDitempatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonArrayRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void searchMenu(final String nama)
    {
        try
        {
            String url=Server.URL+"carimenu.php";
            RequestQueue queue=Volley.newRequestQueue(this);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try
                    {
                        listMenu=new ArrayList<>();
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        if(jsonArray.length() > 0)
                        {
                            for(int i=0; i< jsonArray.length(); i++)
                            {

                                JSONObject jsonArrayObject=jsonArray.getJSONObject(i);

                                Menu menu=new Menu();
                                menu.setId(jsonArrayObject.getString("id_menu"));
                                menu.setNama(jsonArrayObject.getString("nama"));
                                menu.setHarga(jsonArrayObject.getDouble("harga"));
                                menu.setJenis(jsonArrayObject.getString("jenis"));
                                menu.setStatus(jsonArrayObject.getString("status"));
                                menu.setDeskripsi(jsonArrayObject.getString("deskripsi"));
                                menu.setImage(jsonArrayObject.getString("image"));
                                menu.setStock(jsonArrayObject.getInt("stock"));

                                listMenu.add(menu);
                            }

                            menuadapter=new MenuAdapter(listMenu,PesanDitempatActivity.this,3);
                            rvmenu.setAdapter(menuadapter);
                            menuadapter.notifyDataSetChanged();
                        }

                        else
                        {
                            Toast.makeText(PesanDitempatActivity.this, "Data Masih Kosong", Toast.LENGTH_SHORT).show();
                        }


                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(PesanDitempatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PesanDitempatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params=new HashMap<>();

                    params.put("nama",nama);

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


    private void getMakanan()
    {
        String url=Server.URL+"getmakanan.php";
        RequestQueue queue=Volley.newRequestQueue(this);

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                listMenu.clear();

                try
                {
                    for(int i=0; i < response.length(); i ++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);

                        Menu menu=new Menu();

                        menu.setId(jsonObject.getString("id_menu"));
                        menu.setNama(jsonObject.getString("nama"));
                        menu.setHarga(jsonObject.getDouble("harga"));
                        menu.setJenis(jsonObject.getString("jenis"));
                        menu.setStatus(jsonObject.getString("status"));
                        menu.setDeskripsi(jsonObject.getString("deskripsi"));
                        menu.setImage(jsonObject.getString("image"));
                        menu.setStock(jsonObject.getInt("stock"));

                        listMenu.add(menu);

                    }

                    menuadapter=new MenuAdapter(listMenu,PesanDitempatActivity.this,3);
                    rvmenu.setAdapter(menuadapter);
                    menuadapter.notifyDataSetChanged();
                }

                catch (JSONException e)
                {
                    Toast.makeText(PesanDitempatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PesanDitempatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jsonArrayRequest);

    }

    private void getMinuman()
    {
        String url=Server.URL+"getminuman.php";
        RequestQueue queue=Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(response.length()>0)
                {
                    listMenu.clear();

                    for(int i=0; i < response.length(); i++)
                    {

                        try
                        {
                            JSONObject jsonObject=response.getJSONObject(i);

                            Menu menu=new Menu();
                            menu.setId(jsonObject.getString("id_menu"));
                            menu.setNama(jsonObject.getString("nama"));
                            menu.setHarga(jsonObject.getDouble("harga"));
                            menu.setJenis(jsonObject.getString("jenis"));
                            menu.setStatus(jsonObject.getString("status"));
                            menu.setDeskripsi(jsonObject.getString("deskripsi"));
                            menu.setImage(jsonObject.getString("image"));
                            menu.setStock(jsonObject.getInt("stock"));

                            listMenu.add(menu);


                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(PesanDitempatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    menuadapter=new MenuAdapter(listMenu, PesanDitempatActivity.this,3);
                    rvmenu.setAdapter(menuadapter);
                    menuadapter.notifyDataSetChanged();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PesanDitempatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }

}
