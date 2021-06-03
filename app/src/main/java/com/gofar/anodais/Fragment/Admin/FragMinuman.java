package com.gofar.anodais.Fragment.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gofar.anodais.Model.Menu;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragMinuman extends Fragment {

    ArrayList<Menu> itemList = new ArrayList<Menu>();
    RecyclerView.Adapter menuadapter;
    RecyclerView.LayoutManager menulayoutManager;
    RecyclerView rvminuman;
    SearchView txtsearchminuman;

    final String TAG_ID = "id_menu";
    final String TAG_NAMA = "nama";
    final String TAG_JENIS = "jenis";
    final String TAG_HARGA = "harga";
    final String TAG_STATUS = "status";
    final String TAG_IMAGE = "image";
    final String TAG_DESKRIPSI="deskripsi";
    final String TAG_STOCK="stock";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.frag_minuman_layout,container,false);
        rvminuman=view.findViewById(R.id.rvmenuminuman);
        txtsearchminuman=view.findViewById(R.id.txtsearchminuman);

        menulayoutManager=new LinearLayoutManager(getContext());
        rvminuman.setLayoutManager(menulayoutManager);
        rvminuman.setHasFixedSize(true);

        txtsearchminuman.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cariminuman(newText);
                return true;
            }
        });

        return view;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
        {
            filldataminuman();
        }
    }



    public void filldataminuman()
    {
        try
        {
            String url_selectminuman= Server.URL+"getminuman.php";
            RequestQueue requestQueue= Volley.newRequestQueue(getContext());

            JsonArrayRequest jar=new JsonArrayRequest(url_selectminuman, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    itemList.clear();

                    for(int i=0;i<response.length();i++)
                    {
                        try
                        {
                            JSONObject obj=response.getJSONObject(i);
                            Menu item=new Menu();
                            item.setId(obj.getString(TAG_ID));
                            item.setNama(obj.getString(TAG_NAMA));
                            item.setHarga( obj.getDouble(TAG_HARGA));
                            item.setJenis(obj.getString(TAG_JENIS));
                            item.setStatus(obj.getString(TAG_STATUS));
                            item.setImage(obj.getString(TAG_IMAGE));
                            item.setDeskripsi(obj.getString(TAG_DESKRIPSI));
                            item.setStock(obj.getInt(TAG_STOCK));

                            itemList.add(item);
                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    menuadapter=new MenuAdapter(itemList,getContext(),1);
                    rvminuman.setAdapter(menuadapter);
                    menuadapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jar);
        }

        catch(Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void cariminuman(final String cari)
    {
        String url_cariminuman=Server.URL+"cariminuman.php";
        RequestQueue requestQueue=Volley.newRequestQueue(getActivity());
        try
        {
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url_cariminuman, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        ArrayList<Menu>itemList=new ArrayList<>();
                        JSONObject obj=new JSONObject(response);
                        JSONArray jsonArray=obj.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                Menu item=new Menu();
                                item.setId(jsonObject.getString(TAG_ID));
                                item.setNama(jsonObject.getString(TAG_NAMA));
                                item.setHarga( jsonObject.getDouble(TAG_HARGA));
                                item.setStatus(jsonObject.getString(TAG_STATUS));
                                item.setImage(jsonObject.getString(TAG_IMAGE));
                                item.setStock(jsonObject.getInt(TAG_STOCK));

                                itemList.add(item);
                            }

                            menuadapter=new MenuAdapter(itemList,getContext(),1);
                            rvminuman.setAdapter(menuadapter);
                            menuadapter.notifyDataSetChanged();
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
                    Map<String,String> params=new HashMap<>();
                    params.put("nama",cari);

                    return params;
                }

            };

            requestQueue.add(stringRequest);

        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
