package com.gofar.anodais.Adapter.General;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.gofar.anodais.Activity.Admin.DetailPesananActivity;
import com.gofar.anodais.Fragment.Customer.FragCheckOut;
import com.gofar.anodais.Model.CheckOut;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {

    public List <CheckOut> checkOutList =new ArrayList<>();
    Context context;
    int VIEW_TYPE;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblcheckoutnama,lblcheckoutharga,lblsubtotal,lblalamatpesanan,lblnotelppesanan;
        TextView lbljumlah,lblidpesanan,lbljmlpesanan,lblnamapelanggan,lbltotalpesanan;
        ImageView ivcheckout;
        Button btncekpesanan,btnhapuspesanan,btnbatalcheckout;
        Spinner spnstatuspesanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if(VIEW_TYPE==1)
            {
                lblidpesanan=itemView.findViewById(R.id.lblidpesanan);
                lbljmlpesanan=itemView.findViewById(R.id.lbljumlahpesanan);
                lblnamapelanggan=itemView.findViewById(R.id.lblnamapelangganpesan);
                lbltotalpesanan=itemView.findViewById(R.id.lbltotalpesanan);
                lblalamatpesanan=itemView.findViewById(R.id.lblalamatpelangganpesanan);
                lblnotelppesanan=itemView.findViewById(R.id.lblnotelppelangganpesanan);

                btncekpesanan=itemView.findViewById(R.id.btncekpesan);
                btnhapuspesanan=itemView.findViewById(R.id.btnhapuspesanan);

                spnstatuspesanan=itemView.findViewById(R.id.spnstatuspesanan);
            }

            else if(VIEW_TYPE==2)
            {
                lblcheckoutnama=itemView.findViewById(R.id.lblcheckoutnama);
                lblcheckoutharga=itemView.findViewById(R.id.lblcheckoutharga);
                lblsubtotal=itemView.findViewById(R.id.lblcheckoutsubtotal);
                lbljumlah=itemView.findViewById(R.id.lblcheckoutjumlah);
                ivcheckout=itemView.findViewById(R.id.ivcheckout);
                btnbatalcheckout=itemView.findViewById(R.id.btnbatalcheckout);
            }

        }
    }


    public CheckOutAdapter(List<CheckOut> checkOutList, Context context, int VIEW_TYPE)
    {
        this.checkOutList = checkOutList;
        this.context=context;
        this.VIEW_TYPE=VIEW_TYPE;
    }

    @NonNull
    @Override
    public CheckOutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=null;

        switch (VIEW_TYPE)
        {
            case 1:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pesanan,null);
                break;

            case 2:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkout,null);
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutAdapter.ViewHolder holder, int position) {

        if(VIEW_TYPE==1)
        {
            getPesanan(holder,position);
        }

        else if(VIEW_TYPE==2)
        {
            getCheckout(holder,position);
        }

    }

    @Override
    public int getItemCount() {
        return checkOutList.size();
    }


    public void getCheckout(ViewHolder holder, final int position)
    {
        final String idcheckout= checkOutList.get(position).getId_checkout();
        String nama= checkOutList.get(position).getNamaMenu();
        String jumlah= checkOutList.get(position).getJumlah();
        final Double subtotal= checkOutList.get(position).getSubtotal();
        String image= checkOutList.get(position).getImage();
        Double harga=checkOutList.get(position).getHarga();
        final String idUser=checkOutList.get(position).getIdUser();

        holder.lblcheckoutnama.setText(nama);
        holder.lbljumlah.setText(jumlah);
        holder.lblcheckoutharga.setText(NumberFormat.getCurrencyInstance().format(harga));
        holder.lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(subtotal));

        Glide.with(context).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivcheckout);


        holder.btnbatalcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Batalkan Pesanan")
                        .setMessage("Pesanan yang sudah dibatalkan tidak dapat dikembalikan lagi")
                        .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteItem(position,idUser,idcheckout);
                                FragCheckOut.total-=subtotal;
                                FragCheckOut.lbltotal.setText(NumberFormat.getCurrencyInstance().format(FragCheckOut.total));

                                if(FragCheckOut.total < 50000)
                                {
                                    FragCheckOut.lbltotal.setBackgroundResource(0);
                                    FragCheckOut.lbltotaldiskon.setVisibility(View.INVISIBLE);
                                    FragCheckOut.lbltotaldiskon.setText(NumberFormat.getCurrencyInstance().format(FragCheckOut.total - (FragCheckOut.total * 10 / 100) ) );
                                    FragCheckOut.lbldiskon.setVisibility(View.INVISIBLE);
                                }


                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });


    }




    public void getPesanan(ViewHolder holder, final int position)
    {
        final String idpesanan= checkOutList.get(position).getId_checkout();
        String namaPelanggan=checkOutList.get(position).getUser();
        String jumlah= checkOutList.get(position).getJumlah();
        Double total= checkOutList.get(position).getTotal();
        String alamat=checkOutList.get(position).getAlamat();
        final String idUser=checkOutList.get(position).getIdUser();
        String notelp=checkOutList.get(position).getNoTelp();

        holder.lblidpesanan.setText(idpesanan);
        holder.lblnamapelanggan.setText(namaPelanggan);
        holder.lbljmlpesanan.setText(jumlah);
        holder.lblalamatpesanan.setText(alamat);
        holder.lbltotalpesanan.setText(NumberFormat.getCurrencyInstance().format(total));
        holder.lblnotelppesanan.setText(notelp);

        final ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context,R.array.statuspesanan,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spnstatuspesanan.setAdapter(adapter);


        holder.btncekpesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString("iduser",idUser);

                Intent intent=new Intent(context,DetailPesananActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        holder.btnhapuspesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("batalkan pesanan")
                        .setMessage("Pesanan yang dibatalkan tidak dapat dikembalikan")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePesanan(position,idUser);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();

            }
        });

        holder.spnstatuspesanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(position>0)
                {
                    String notifikasiPesanan="Pesanan mu "+adapter.getItem(position);
                    addNotifikasiPesanan(idUser,notifikasiPesanan);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void addNotifikasiPesanan(final String iduser, final String pesan)
    {
        try
        {
            String url=Server.URL+"addnotifikasi.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                       JSONObject jsonObject=new JSONObject(response);

                       if(jsonObject.getString("success").equals("0"))
                       {
                           Toast.makeText(context, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                       }

                    }

                    catch (Exception e)
                    {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

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
                    params.put("iduser",iduser);
                    params.put("pesan",pesan);
                    return params;
                }
            };

            queue.add(stringRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void updateStatusCheckout(final String idPelanggan, final String status)
    {
        try
        {
            String url=Server.URL+"updatestatuscheckout.php";
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
                    Map<String,String>params=new HashMap<>();

                    params.put("idpelanggan",idPelanggan);

                    params.put("status",status);

                    return params;
                }
            };

            queue.add(stringRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePesanan(final int position, final String id)
    {
        try
        {
            String url=Server.URL+"deletepesanan.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            checkOutList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,checkOutList.size());

                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (Exception e)
                    {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

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
                    Map<String,String>params=new HashMap<>();
                    params.put("idpelanggan",id);

                    return params;

                }
            };

            queue.add(request);


        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteItem(final int position, final String idpelanggan, final String idcheckout)
    {
        try
        {
            String url=Server.URL+"deleteitemcheckout.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            checkOutList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,checkOutList.size());

                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

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
                    Map<String,String>params=new HashMap<>();
                    params.put("idpelanggan",idpelanggan);
                    params.put("idcheckout",idcheckout);

                    return params;
                }
            };

            queue.add(request);
        }

        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }









}
