package com.gofar.anodais.Adapter.Admin;

import android.content.Context;
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
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PembatalanReservasiMejaAdapter extends RecyclerView.Adapter<PembatalanReservasiMejaAdapter.ViewHolder> {

    ArrayList<Reservasi>reservasiList=new ArrayList<>();
    Context context;
    int jenisView;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView lblidreservasibatal,lblnomejabatal,lblusernamebatal,lbltglreservasibatal, lblnotelp;
        Button btnterimabatal,btntolakbatal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblidreservasibatal=itemView.findViewById(R.id.lblidreservasibatal);
            lblnomejabatal=itemView.findViewById(R.id.lblnomejabatal);
            lblusernamebatal=itemView.findViewById(R.id.lblusernamebatal);
            lbltglreservasibatal=itemView.findViewById(R.id.lbltglreservasibatal);

            lblnotelp=itemView.findViewById(R.id.lblnotelpreservasibatal);
            btnterimabatal=itemView.findViewById(R.id.btnterimabatal);
            btntolakbatal=itemView.findViewById(R.id.btntolakbatal);

        }
    }


    public PembatalanReservasiMejaAdapter(ArrayList<Reservasi> reservasiList, Context context, int jenisView) {
        this.reservasiList = reservasiList;
        this.context = context;
        this.jenisView = jenisView;
    }

    @NonNull
    @Override
    public PembatalanReservasiMejaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.layout_pembatalan,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PembatalanReservasiMejaAdapter.ViewHolder holder, final int position) {

        final String idreservasi=reservasiList.get(position).getId_reservasi();
        final String nomeja=reservasiList.get(position).getNomeja();
        String username=reservasiList.get(position).getUsername();
        final String tglreservasi=reservasiList.get(position).getTgl_sewa();
        final String idpembatalan=reservasiList.get(position).getIdPembatalan();
        final String iduser=reservasiList.get(position).getId_user();
        final String notelp=reservasiList.get(position).getNoTelp();

        try
        {
            holder.lblidreservasibatal.setText(idreservasi);
            holder.lblnomejabatal.setText(nomeja);
            holder.lblusernamebatal.setText(username);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date frmttglsewa= sdf.parse(tglreservasi);

            sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            holder.lbltglreservasibatal.setText(sdf.format(frmttglsewa));
            holder.lblnotelp.setText(notelp);

            Calendar cal=Calendar.getInstance();
            cal.add(Calendar.DATE,1);
            sdf=new SimpleDateFormat("yyyy-MM-dd");
            final Date besok=cal.getTime();
            final String formattedbesok=sdf.format(besok);


            holder.btnterimabatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //hapus data dari tabel reservasi
                    terimaBatal(position);

                    //jika tgl reservasi besok status meja akan diubah menjadi tersedia
                    if(tglreservasi.equals(formattedbesok))updateStatus(nomeja);


                }
            });


            holder.btntolakbatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tolakRequestbatal(position);

                }
            });

        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public int getItemCount() {
        return reservasiList.size();
    }


    public void terimaBatal(final int position)
    {
        try
        {
                String url_delete= Server.URL+"deletereservasi.php";
                RequestQueue queue= Volley.newRequestQueue(context);

                StringRequest request=new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject object=new JSONObject(response);

                            if(object.getString("success").equals("1"))
                            {
                                Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                                reservasiList.remove(position);
                                notifyItemRangeChanged(position, reservasiList.size());

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

                        String nomeja=reservasiList.get(position).getNomeja();
                        String idreservasi=reservasiList.get(position).getId_reservasi();
                        String iduser=reservasiList.get(position).getId_user();

                        String pesan="Pembatalan diterima untuk Meja "+nomeja+"!";

                        params.put("idreservasi",idreservasi);
                        params.put("pesan",pesan);
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


    public void tolakRequestbatal(final int position)
    {
        try
        {
            String url_delete= Server.URL+"deleterequestpembatalan.php";
            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            reservasiList.remove(position);
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

                    String idpembatalan=reservasiList.get(position).getIdPembatalan();
                    String nomeja=reservasiList.get(position).getNomeja();
                    String iduser=reservasiList.get(position).getId_user();
                    String idreservasi=reservasiList.get(position).getId_reservasi();
                    String pesan="Permintaan batal meja "+nomeja+ " ditolak!";

                    params.put("id",idpembatalan);
                    params.put("pesan",pesan);
                    params.put("iduser",iduser);
                    params.put("status","Diajukan");
                    params.put("idreservasi",idreservasi);

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



    public void updateStatus(final String nomeja)
    {
        try
        {
            String url_updatestatus=Server.URL+"updatestatusmeja.php";
            RequestQueue queue=Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.POST, url_updatestatus, new Response.Listener<String>() {
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
                    params.put("nomeja",nomeja);
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
