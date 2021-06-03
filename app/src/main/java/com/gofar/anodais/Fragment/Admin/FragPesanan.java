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
import com.gofar.anodais.Adapter.General.CheckOutAdapter;
import com.gofar.anodais.Model.CheckOut;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragPesanan extends Fragment {

    List<CheckOut>pesananList=new ArrayList<>();

    RecyclerView.Adapter pesananAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView rvpesanan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.frag_pesanan_layout,null,false);

        rvpesanan=view.findViewById(R.id.rvpesanan);
        layoutManager=new LinearLayoutManager(getActivity());
        rvpesanan.setLayoutManager(layoutManager);
        rvpesanan.setHasFixedSize(true);

        return  view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if(menuVisible)
        {
            getPesanan();
        }
    }


    public void getPesanan()
    {
        try {

            String url_pesanan = Server.URL + "getpesanan.php";
            final RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonArrayRequest jar=new JsonArrayRequest(url_pesanan, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    pesananList.clear();

                    for(int i=0; i< response.length();i++)
                    {
                        try
                        {
                            JSONObject object=response.getJSONObject(i);
                            CheckOut checkOut=new CheckOut();

                            checkOut.setId_checkout(object.getString("id_checkout"));
                            checkOut.setIdUser(object.getString("id_pelanggan"));
                            checkOut.setJumlah(object.getString("jml pesanan"));
                            checkOut.setNoTelp(object.getString("notelp"));
                            checkOut.setTotal(object.getDouble("total"));
                            checkOut.setAlamat(object.getString("alamat"));
                            checkOut.setUser(object.getString("username"));
                            pesananList.add(checkOut);
                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    pesananAdapter=new CheckOutAdapter(pesananList,getActivity(),1);
                    rvpesanan.setAdapter(pesananAdapter);
                    pesananAdapter.notifyDataSetChanged();

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
