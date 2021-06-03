package com.gofar.anodais.Adapter.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gofar.anodais.Model.MenuPengajuan;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class LihatMenuAdapter extends RecyclerView.Adapter<LihatMenuAdapter.ViewHolder> {

    Context context;
    ArrayList<MenuPengajuan> pengajuanList=new ArrayList<>();
    String JENIS_VIEW;

     class ViewHolder extends RecyclerView.ViewHolder {

         TextView lblnamamenu, lblsubtotal, lbljumlah,lblharga,lbljumlahpengajuan;
         ImageView ivdetailmenu;
         Button btnadd,btnmin,btnubahjumlah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if(JENIS_VIEW.equals("Admin"))
            {
                lblnamamenu=itemView.findViewById(R.id.lblnamamenupesan);
                lblsubtotal=itemView.findViewById(R.id.lblsubtotalmenupesan);
                lbljumlah=itemView.findViewById(R.id.lbljumlahpesan);
                lblharga=itemView.findViewById(R.id.lblhargamenupesan);
                ivdetailmenu=itemView.findViewById(R.id.ivdetailmenupesan);
            }


            else if(JENIS_VIEW.equals("Customer"))
            {

                lblnamamenu=itemView.findViewById(R.id.lblnamamenupesanan);
                lblsubtotal=itemView.findViewById(R.id.lblsubtotalmenupesanan);
                lbljumlah=itemView.findViewById(R.id.lbljumlahpesanan);
                lblharga=itemView.findViewById(R.id.lblhargamenupesanan);
                lbljumlahpengajuan=itemView.findViewById(R.id.lbljmlitempengajuan);

                ivdetailmenu=itemView.findViewById(R.id.ivdetailmenupesanan);

                btnadd=itemView.findViewById(R.id.btnadditempengajuan);
                btnmin=itemView.findViewById(R.id.btnminitempengajuan);

            }


        }
    }

    public LihatMenuAdapter(Context context, ArrayList<MenuPengajuan> pengajuanList, String JENIS_VIEW)
    {
        this.context=context;
        this.pengajuanList=pengajuanList;
        this.JENIS_VIEW=JENIS_VIEW;
    }

    @NonNull
    @Override
    public LihatMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

         View view=null;

       switch (JENIS_VIEW)
       {
           case "Admin":
                view= LayoutInflater.from(context).inflate(R.layout.layout_detail_pesanan_menu_admin,null,false);
               break;

           case "Customer":
               view= LayoutInflater.from(context).inflate(R.layout.layout_detail_pesanan_menu,null,false);
               break;
       }

       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LihatMenuAdapter.ViewHolder holder, int position) {

         switch(JENIS_VIEW)
         {
             case "Admin":
                 viewDetailMenuAdmin(holder,position);
                 break;

             case "Customer":
                 viewDetailMenuCustomer(holder,position);
                 break;
         }



    }

    @Override
    public int getItemCount() {
        return pengajuanList.size();
    }



    private void addJml(final String idreservasi, final String idmenu)
    {
        try
        {
            String url=Server.URL+"/addjumlahpengajuanpesanan.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();

                    params.put("idreservasi",String.valueOf(idreservasi));
                    params.put("idmenu",idmenu);

                    return  params;

                }
            };

            queue.add(stringRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void minJml(final String idreservasi, final String idmenu)
    {
        try
        {
            String url=Server.URL+"/minjumlahpengajuanpesanan.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params=new HashMap<>();

                    params.put("idreservasi",idreservasi);
                    params.put("idmenu",idmenu);

                    return  params;
                }
            };

            queue.add(request);
        }


        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void viewDetailMenuAdmin(final ViewHolder holder, final int position)
    {
        final String id_menu=pengajuanList.get(position).getId_menu();
        final String nama=pengajuanList.get(position).getNama();
        final String image=pengajuanList.get(position).getImage();
        final int jumlah=pengajuanList.get(position).getJumlah();
        final Double harga=pengajuanList.get(position).getHarga();
        final Double subtotal=pengajuanList.get(position).getSubtotal();
        final String id_reservasi=pengajuanList.get(position).getIdreservasi();
        final int stock=pengajuanList.get(position).getStock();


        holder.lblnamamenu.setText(nama);
        holder.lbljumlah.setText(String.valueOf(jumlah));
        holder.lblharga.setText(NumberFormat.getCurrencyInstance().format(harga));
        holder.lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(subtotal));

        Glide.with(context).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivdetailmenu);

    }


    private void viewDetailMenuCustomer(final ViewHolder holder, final int position)
    {

        final String id_menu=pengajuanList.get(position).getId_menu();
        final String nama=pengajuanList.get(position).getNama();
        final String image=pengajuanList.get(position).getImage();
        final int jumlah=pengajuanList.get(position).getJumlah();
        final Double harga=pengajuanList.get(position).getHarga();
        final Double subtotal=pengajuanList.get(position).getSubtotal();
        final String id_reservasi=pengajuanList.get(position).getIdreservasi();
        final int stock=pengajuanList.get(position).getStock();


        holder.lblnamamenu.setText(nama);
        holder.lbljumlah.setText(String.valueOf(jumlah));
        holder.lbljumlahpengajuan.setText(String.valueOf(jumlah));
        holder.lblharga.setText(NumberFormat.getCurrencyInstance().format(harga));
        holder.lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(subtotal));

        Glide.with(context).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivdetailmenu);

        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int jumlahPengajuan=Integer.parseInt(holder.lbljumlahpengajuan.getText().toString());

                if(jumlahPengajuan >= stock)
                {
                    Toast.makeText(context, "Stock tidak tersedia lebih dari ini", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    holder.lbljumlahpengajuan.setText(String.valueOf(jumlahPengajuan + 1));
                    addJml(id_reservasi,id_menu);
                }


            }
        });


        holder.btnmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int jumlahPengajuan=Integer.parseInt(holder.lbljumlahpengajuan.getText().toString());

                if (jumlahPengajuan == 1)
                {
                    holder.lbljumlahpengajuan.setText("1");
                }

                else
                {
                    holder.lbljumlahpengajuan.setText(String.valueOf(jumlahPengajuan - 1));
                    minJml(id_reservasi,id_menu);
                }

            }
        });



    }



}
