package com.gofar.anodais.Fragment.Customer;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Adapter.General.CheckOutAdapter;
import com.gofar.anodais.Model.CheckOut;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragCheckOut extends Fragment {

    List<CheckOut> checkOutList=new ArrayList();

    RecyclerView.Adapter checkoutAdapter;
    RecyclerView rvcheckout;
    RecyclerView.LayoutManager layoutManager;

    public static TextView lbltotal,lbltotaldiskon,lbldiskon;
   public static Double total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_checkout,null);

        rvcheckout=view.findViewById(R.id.rvcheckout);
        layoutManager=new LinearLayoutManager(getActivity());
        rvcheckout.setLayoutManager(layoutManager);

        lbltotal=view.findViewById(R.id.lblcheckouttotal);
        lbltotaldiskon=view.findViewById(R.id.lblcheckouttotaldiskon);
        lbldiskon=view.findViewById(R.id.lblcheckoutdiskon);

        rvcheckout.setHasFixedSize(true);

        return view;
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(menuVisible)
        {
            fillCheckOut();
        }
    }

    public  void fillCheckOut()
    {
        try
        {
            String url_checkout= Server.URL+"getcheckout.php";
            RequestQueue queue=Volley.newRequestQueue(getActivity());
            StringRequest request=new StringRequest(Request.Method.POST, url_checkout, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {

                        checkOutList=new ArrayList<>();

                        total=0.0;

                        JSONObject object=new JSONObject(response);

                        JSONArray jsonArray=object.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            CheckOut checkOut=new CheckOut();

                            checkOut.setId_checkout(jsonObject.getString("id_checkout"));
                            checkOut.setIdUser(jsonObject.getString("id_pelanggan"));
                            checkOut.setAlamat(jsonObject.getString("alamat"));
                            checkOut.setJumlah(jsonObject.getString("jumlah"));
                            checkOut.setSubtotal(jsonObject.getDouble("subtotal"));
                            checkOut.setHarga(jsonObject.getDouble("harga"));
                            checkOut.setNamaMenu(jsonObject.getString("nama"));
                            checkOut.setImage(jsonObject.getString("image"));

                            checkOutList.add(checkOut);

                            total+=jsonObject.getDouble("subtotal");

                        }

                        lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));

                        if(total>50000)
                        {
                            lbltotal.setBackgroundResource(R.drawable.strike_line);
                            lbltotaldiskon.setVisibility(View.VISIBLE);
                            lbltotaldiskon.setText(NumberFormat.getCurrencyInstance().format(total - (total * 10 / 100) ) );
                            lbldiskon.setVisibility(View.VISIBLE);
                        }


                        checkoutAdapter=new CheckOutAdapter(checkOutList,getActivity(),2);
                        rvcheckout.setAdapter(checkoutAdapter);
                        checkoutAdapter.notifyDataSetChanged();

                    }

                    catch(Exception e)
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
                    SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
                    params.put("idpelanggan", prefs.getString("id",null));
                    return params;
                }
            };

            queue.add(request);

        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
