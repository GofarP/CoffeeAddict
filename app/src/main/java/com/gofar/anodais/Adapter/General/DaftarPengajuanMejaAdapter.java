package com.gofar.anodais.Adapter.General;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Activity.Admin.DetailPesananActivity;
import com.gofar.anodais.Activity.Customer.LihatMenuActivity;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DaftarPengajuanMejaAdapter extends RecyclerView.Adapter<DaftarPengajuanMejaAdapter.ViewHolder> {

    ArrayList<Reservasi>reservasiArrayList=new ArrayList<>();
    Context context;
    int jenisView;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView lblnomeja,lblusername,lbltgl,lblidmeja,lblnomejareservasi;
        TextView lblusernamereservasi,lbltglreservasi,lblnotelpuserreservasi;
        TextView lblnomejapesan,lblnamapelangganpesan,lblsewatanggalpesan, lblnotelppesan;

        Button btnterimameja,btntolakmeja,btncancelmeja,btnlihatpesanan;
        Button btncekpesan, btnhapuspesan, btncetastruk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if(jenisView==1)
            {
                lblnomejareservasi=itemView.findViewById(R.id.lblnomejaresevasi);
                lblusernamereservasi=itemView.findViewById(R.id.lblusernamereservasi);
                lbltglreservasi=itemView.findViewById(R.id.lbltglreservasi);
                lblnotelpuserreservasi=itemView.findViewById(R.id.lblnotelpreservasi);

                btnterimameja=itemView.findViewById(R.id.btnterimameja);
                btntolakmeja=itemView.findViewById(R.id.btntolakmeja);

            }

            else if(jenisView==2)
            {
                lblnomeja=itemView.findViewById(R.id.lblnomejadaftarreservasi);
                lblusername=itemView.findViewById(R.id.lblnamapenyewadaftarreservasi);
                lbltgl=itemView.findViewById(R.id.lbltglsewadaftarreservasi);
                btncancelmeja=itemView.findViewById(R.id.btnbatalpengajuansewa);
                btnlihatpesanan=itemView.findViewById(R.id.btnpengajuanmenupesanan);
            }

            else if(jenisView ==3)
            {
                lblnomejapesan=itemView.findViewById(R.id.lblnomejapesan);
                lblnamapelangganpesan=itemView.findViewById(R.id.lblnamapelangganpesan);
                lblnotelppesan=itemView.findViewById(R.id.lblnotelppesan);
                lblsewatanggalpesan=itemView.findViewById(R.id.lbltglsewapesan);
                btncekpesan=itemView.findViewById(R.id.btncekpesan);

            }


        }
    }

    public DaftarPengajuanMejaAdapter(ArrayList<Reservasi>reservasiArrayList, Context context, int jenisView)
    {
        this.reservasiArrayList=reservasiArrayList;
        this.context=context;
        this.jenisView=jenisView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;

        switch (jenisView)
        {
            case 1:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reservasi,null,false);
                break;

            case 2:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_daftar_pengajuan_meja,null,false);
                break;

            case 3:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pesanan_ditempat,null,false);
                break;

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)  {


        switch (jenisView)
        {
            case 1:
                viewAdminDaftarReservasi(holder,position);
                break;

            case 2:
                viewCustomerDaftarReservasi(holder,position);
                break;

            case 3:
                viewAdminDaftarPesananDitempat(holder,position);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return reservasiArrayList.size();
    }


    public void viewAdminDaftarReservasi(ViewHolder holder, final int position)
    {
        try
        {
                final String nomeja=reservasiArrayList.get(position).getNomeja();
                final String namapenyewa=reservasiArrayList.get(position).getUsername();
                final String notelp=reservasiArrayList.get(position).getNoTelp();
                final String tglsewa=reservasiArrayList.get(position).getTgl_sewa();
                final String idreservasi=reservasiArrayList.get(position).getId_reservasi();
                final String iduser=reservasiArrayList.get(position).getId_user();

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date frmttglsewa= sdf.parse(tglsewa);

                holder.lblnomejareservasi.setText(nomeja);
                holder.lblusernamereservasi.setText(namapenyewa);
                holder.lblnotelpuserreservasi.setText(notelp);

                sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                holder.lbltglreservasi.setText(sdf.format(frmttglsewa));

                holder.btnterimameja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateStatus(idreservasi);
//                        insertTransaksi(iduser,"Reservasi","0");
//                        terimaMeja(idreservasi,"Sukses menyewa meja "+ nomeja+"!",iduser);

                        reservasiArrayList.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,reservasiArrayList.size());

                    }
                });

                holder.btntolakmeja.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tolakMeja(idreservasi);
                        reservasiArrayList.remove(position);

                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,reservasiArrayList.size());
                    }
                });

        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void viewCustomerDaftarReservasi(ViewHolder holder, final int position)
    {
        try
        {
            final String nomeja=reservasiArrayList.get(position).getNomeja();
            final String namapenyewa=reservasiArrayList.get(position).getUsername();
            final String tglsewa=reservasiArrayList.get(position).getTgl_sewa();
            final String idreservasi=reservasiArrayList.get(position).getId_reservasi();
            final String idmeja=reservasiArrayList.get(position).getId_meja();

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date frmttglsewa= sdf.parse(tglsewa);

            sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            holder.lblnomeja.setText(nomeja);
            holder.lblusername.setText(namapenyewa);
            holder.lbltgl.setText(sdf.format(frmttglsewa));

            holder.btnlihatpesanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, LihatMenuActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("idreservasi",idreservasi);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });


            holder.btncancelmeja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);

                    builder.setTitle("Pembatalan sewa meja")
                            .setMessage("Apakah anda yakin akan mengajukan pembatalan sewa?, " +
                                    "pengajuan yang sudah diajukan tidak dapat dibatalkan!")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPembatalan(idreservasi,position);
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

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void viewAdminDaftarPesananDitempat(ViewHolder viewHolder, int position)
    {
        try
        {
            String nomeja=reservasiArrayList.get(position).getNomeja();
            String username=reservasiArrayList.get(position).getUsername();
            String notelp=reservasiArrayList.get(position).getNoTelp();
            String tglsewa=reservasiArrayList.get(position).getTgl_sewa();
            final String idreservasi=reservasiArrayList.get(position).getId_reservasi();
            final String iduser=reservasiArrayList.get(position).getId_user();

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fmtTglSewa=sdf.parse(tglsewa);
            sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            viewHolder.lblnomejapesan.setText(nomeja);
            viewHolder.lblnamapelangganpesan.setText(username);
            viewHolder.lblsewatanggalpesan.setText(sdf.format(fmtTglSewa));
            viewHolder.lblnotelppesan.setText(notelp);

            viewHolder.btncekpesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, DetailPesananActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("idreservasi",idreservasi);
                    bundle.putString("iduser",iduser);
                    bundle.putBoolean("pesan_ditempat",true);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    public void requestPembatalan(final String idreservasi, final int position)
    {
        try
        {
            String url_notif= Server.URL+"addbatalpinjam.php";
            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.POST, url_notif, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(context, "Sukses mengajukan pembatalan", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            reservasiArrayList.remove(position);
                            notifyItemRangeChanged(position,reservasiArrayList.size());


                        }

                        else if(object.getString("success").equals("2"))
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(context, "Gagal mengajukan pembatalan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
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
                    params.put("idreservasi",idreservasi);

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


    public void terimaMeja(final String idreservasi,final String pesan,final String iduser)
    {
        try
        {
            String url_terima=Server.URL+"deletereservasi.php";
            RequestQueue queue=Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.POST, url_terima, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
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
                    params.put("idreservasi",idreservasi);
                    params.put("pesan", pesan);
                    params.put("iduser",iduser);
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


    public void insertTransaksi(final String iduser, final String jenis, final String total)
    {
        try
        {
            String url_trans=Server.URL+"addtransaksi.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, url_trans, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
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
                    HashMap<String,String>params=new HashMap<>();

                    params.put("iduser",iduser);
                    params.put("jenis",jenis);
                    params.put("total",total);

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



    public void updateStatus(final String idReservasi)
    {
        try
        {
            String url_updatestatus=Server.URL+"updatestatusmeja.php";
            RequestQueue queue=Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.POST, url_updatestatus, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject jsonObject=new JSONObject(response);

                        if(jsonObject.getString("success").equals("1"))
                        {
                            Toast.makeText(context, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(context, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e)
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
                    params.put("idreservasi",idReservasi);
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


    private void tolakMeja(final String idreservasi)
    {
        try
        {
            String url=Server.URL+"deletereservasi.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject jsonObject=new JSONObject(response);

                        if(jsonObject.getString("success").equals("1"))
                        {
                            Toast.makeText(context, "Berhasil membatalkan meja", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(context, jsonObject.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
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
                    Map<String, String>params=new HashMap<>();
                    params.put("idreservasi",idreservasi);

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








}
