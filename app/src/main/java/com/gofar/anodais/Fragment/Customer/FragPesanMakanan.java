package com.gofar.anodais.Fragment.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.gofar.anodais.Adapter.General.MenuAdapter;
import com.gofar.anodais.Model.Menu;
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
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
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

public class FragPesanMakanan extends Fragment {

    ArrayList<Menu>itemList=new ArrayList<>();
    RecyclerView.Adapter menuadapter;
    RecyclerView.LayoutManager menulayoutManager;
    RecyclerView rvmenu;
    SearchView txtsearch;

    final String TAG_ID = "id_menu";
    final String TAG_NAMA = "nama";
    final String TAG_JENIS = "jenis";
    final String TAG_HARGA = "harga";
    final String TAG_STATUS = "status";
    final String TAG_SUCCESS = "sukses";
    final String TAG_DESC = "deskripsi";
    final String TAG_IMAGE = "image";
    final String TAG_STOCK = "stock";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pesan_menu,container,false);
        rvmenu=view.findViewById(R.id.rvmenu);
        txtsearch=view.findViewById(R.id.txtsearchmenu);

        menulayoutManager=new LinearLayoutManager(getContext());
        rvmenu.setLayoutManager(menulayoutManager);
        rvmenu.setHasFixedSize(true);

        filldata();


        txtsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                cariMakanan(newText);

                return true;
            }
        });


        return view;
    }


    public void filldata()
    {
        try
        {
            String url_selectmakanan= Server.URL+"getmakanan.php";
            RequestQueue queue=Volley.newRequestQueue(getActivity());
            JsonArrayRequest jar=new JsonArrayRequest(url_selectmakanan, new Response.Listener<JSONArray>() {
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
                            item.setDeskripsi(obj.getString(TAG_DESC));
                            item.setImage(obj.getString(TAG_IMAGE));
                            item.setStock(obj.getInt(TAG_STOCK));

                            itemList.add(item);

                        }

                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                       menuadapter=new MenuAdapter(itemList,getActivity(),2);
                       rvmenu.setAdapter(menuadapter);
                       menuadapter.notifyDataSetChanged();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(jar);
        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void cariMakanan(final String cari)
    {
        try
        {
            String url_carimakanan=Server.URL+"carimakanan.php";
            RequestQueue queue=Volley.newRequestQueue(getActivity());

            StringRequest streq=new StringRequest(Request.Method.POST, url_carimakanan, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        ArrayList<Menu> itemList = new ArrayList<>();
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
                                item.setHarga(jsonObject.getDouble(TAG_HARGA));
                                item.setJenis(jsonObject.getString(TAG_JENIS));
                                item.setStatus(jsonObject.getString(TAG_STATUS));
                                item.setDeskripsi(jsonObject.getString(TAG_DESC));
                                item.setImage(jsonObject.getString(TAG_IMAGE));
                                item.setStock(jsonObject.getInt(TAG_STOCK));

                                itemList.add(item);

                            }

                            menuadapter=new MenuAdapter(itemList,getActivity(),2);
                            rvmenu.setAdapter(menuadapter);
                            menuadapter.notifyDataSetChanged();

                        }


                    }

                    catch (Exception e)
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
                    params.put("nama",cari);

                    return params;
                }
            };

            queue.add(streq);

        }

        catch(Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
