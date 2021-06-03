package com.gofar.anodais.Fragment.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.Admin.PembatalanReservasiMejaAdapter;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragPembatalan extends Fragment {

    ArrayList<Reservasi>reservasiArrayList=new ArrayList<>();
    RecyclerView.Adapter batalAdapter;
    RecyclerView.LayoutManager batalLayoutManager;
    RecyclerView rvbatal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_pembatalan_layout,null,false);

        rvbatal=view.findViewById(R.id.rvbatal);
        batalLayoutManager=new LinearLayoutManager(getContext());
        rvbatal.setLayoutManager(batalLayoutManager);
        rvbatal.setHasFixedSize(true);

        fillDataBatal();

        return  view;
    }


    public void fillDataBatal()
    {
        try
        {
            String url_batal= Server.URL+"getpembatalan.php";
            RequestQueue queue= Volley.newRequestQueue(getActivity());

            final JsonArrayRequest jar=new JsonArrayRequest(Request.Method.POST, url_batal, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                  for(int i=0;i<response.length();i++)
                  {
                      try
                      {
                          JSONObject object=response.getJSONObject(i);
                          Reservasi reservasi=new Reservasi();

                          reservasi.setId_reservasi(object.getString("id_reservasi"));
                          reservasi.setTgl_sewa(object.getString("tgl_sewa"));
                          reservasi.setUsername(object.getString("username"));
                          reservasi.setNomeja(object.getString("no_meja"));
                          reservasi.setIdPembatalan(object.getString("id"));
                          reservasi.setId_user(object.getString("iduser"));
                          reservasi.setNoTelp(object.getString("notelp"));

                          reservasiArrayList.add(reservasi);
                      }

                      catch (JSONException e)
                      {
                          e.printStackTrace();
                      }

                      batalAdapter=new PembatalanReservasiMejaAdapter(reservasiArrayList,getActivity(),1);
                      rvbatal.setAdapter(batalAdapter);
                      batalAdapter.notifyDataSetChanged();
                  }

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
