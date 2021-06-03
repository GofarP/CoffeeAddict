package com.gofar.anodais.Fragment.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.General.DaftarPengajuanMejaAdapter;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragDaftarPengajuanMeja extends Fragment {

    RecyclerView.Adapter reservasiadapter;
    RecyclerView.LayoutManager reservasilayoutManager;
    final ArrayList<Reservasi> reservasiList = new ArrayList<>();
    RecyclerView rvmenureservasi;
    SearchView txtsearchreservasi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_menu_reservasi_layout,container,false);

        rvmenureservasi=view.findViewById(R.id.rvmenureservasi);
        txtsearchreservasi=view.findViewById(R.id.txtsearchreservasi);

        reservasilayoutManager=new LinearLayoutManager(getActivity());
        rvmenureservasi.setLayoutManager(reservasilayoutManager);
        rvmenureservasi.setHasFixedSize(true);

        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if(menuVisible)
        {
            fillData();
        }
    }




    public  void fillData()
   {
       try
       {
           String url_getallreservasi=Server.URL+"getpengajuanmeja.php";
           RequestQueue queue=Volley.newRequestQueue(getActivity());

           JsonArrayRequest jar=new JsonArrayRequest(url_getallreservasi, new Response.Listener<JSONArray>() {
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
                           reservasi.setId_reservasi(object.getString("id_reservasi"));
                           reservasi.setNomeja(object.getString("no_meja"));
                           reservasi.setId_user(object.getString("id"));
                           reservasi.setUsername(object.getString("username"));
                           reservasi.setNoTelp(object.getString("notelp"));
                           reservasi.setTgl_sewa(object.getString("tgl_sewa"));
                           reservasi.setMejaImage(object.getString("images"));

                           reservasiList.add(reservasi);
                       }

                       catch (JSONException e)
                       {
                           Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                       }

                   }

                   reservasiadapter=new DaftarPengajuanMejaAdapter(reservasiList,getActivity(),1);
                   rvmenureservasi.setAdapter(reservasiadapter);
                   reservasiadapter.notifyDataSetChanged();

               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {

                   Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

               }
           });

           queue.add(jar);
       }


       catch (Exception e)
       {
           Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
       }
   }


}
