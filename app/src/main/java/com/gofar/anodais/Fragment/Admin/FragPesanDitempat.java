package com.gofar.anodais.Fragment.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.General.DaftarPengajuanMejaAdapter;
import com.gofar.anodais.Model.MenuPengajuan;
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

public class FragPesanDitempat extends Fragment {

    ArrayList<Reservasi>reservasiList=new ArrayList<>();
    RecyclerView rvpesanditempat;
    RecyclerView.Adapter reservasiAdapter;
    RecyclerView.LayoutManager layoutManager;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_pesan_ditempat,null,false);

        rvpesanditempat=view.findViewById(R.id.rv_pesanan_ditempat);
        layoutManager= new LinearLayoutManager(getActivity());
        rvpesanditempat.setLayoutManager(layoutManager);
        rvpesanditempat.setHasFixedSize(true);

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


    private void fillData()
    {
        try
        {
            String url= Server.URL+"getallpengajuanpesanan.php";
            RequestQueue queue= Volley.newRequestQueue(getActivity());
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    reservasiList.clear();

                    for(int i=0; i < response.length(); i++)
                    {
                        try
                        {
                            JSONObject jsonArrayObject=response.getJSONObject(i);
                            Reservasi reservasi=new Reservasi();
                            reservasi.setUsername(jsonArrayObject.getString("username"));
                            reservasi.setId_user(jsonArrayObject.getString("id_user"));
                            reservasi.setNomeja(jsonArrayObject.getString("no_meja"));
                            reservasi.setTgl_sewa(jsonArrayObject.getString("tgl_sewa"));
                            reservasi.setNoTelp(jsonArrayObject.getString("notelp"));
                            reservasi.setId_reservasi(jsonArrayObject.getString("id_reservasi"));

                            reservasiList.add(reservasi);
                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    reservasiAdapter=new DaftarPengajuanMejaAdapter(reservasiList,getContext(),3);
                    rvpesanditempat.setAdapter(reservasiAdapter);
                    reservasiAdapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonArrayRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
