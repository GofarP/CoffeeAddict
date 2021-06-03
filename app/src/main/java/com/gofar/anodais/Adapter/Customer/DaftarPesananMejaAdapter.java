package com.gofar.anodais.Adapter.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class DaftarPesananMejaAdapter extends RecyclerView.Adapter<DaftarPesananMejaAdapter.ViewHolder>
{
    ArrayList<Reservasi> reservasiArrayList;
    Context context;



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblnamapenyewa, lbltglsewa,lblnomeja;
        Button btnlihatmenu, btncancelmeja;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblnamapenyewa=itemView.findViewById(R.id.lblnamapenyewapesananmeja);
            lbltglsewa=itemView.findViewById(R.id.lbltglsewapesananmeja);
            lblnomeja=itemView.findViewById(R.id.lblnomejapesananmeja);

            btnlihatmenu=itemView.findViewById(R.id.btnlihatpesananmenu);

            btncancelmeja=itemView.findViewById(R.id.btnbatalsewapesanan);
        }
    }


    public DaftarPesananMejaAdapter(ArrayList<Reservasi> reservasiArrayList, Context context)
    {
        this.reservasiArrayList=reservasiArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public DaftarPesananMejaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(context).inflate(R.layout.layout_daftar_pesanan_meja, null, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DaftarPesananMejaAdapter.ViewHolder holder, int position) {

        try
        {
            String noMeja=reservasiArrayList.get(position).getNomeja();
            String namaPenyewa=reservasiArrayList.get(position).getUsername();
            String noTelp=reservasiArrayList.get(position).getNoTelp();
            final String tglsewa=reservasiArrayList.get(position).getTgl_sewa();
            final String idreservasi=reservasiArrayList.get(position).getId_reservasi();
            final String iduser=reservasiArrayList.get(position).getId_user();

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date frmttglsewa= sdf.parse(tglsewa);
            sdf=new SimpleDateFormat("dd-MM-yyyy");

            holder.lblnomeja.setText(noMeja);
            holder.lblnamapenyewa.setText(namaPenyewa);
            holder.lbltglsewa.setText(sdf.format(frmttglsewa));

            holder.btnlihatmenu.setOnClickListener(new View.OnClickListener() {
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
                                    requestPembatalan(idreservasi);
                                }
                            })

                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builder.show();


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
        return reservasiArrayList.size();
    }




    private void requestPembatalan(final  String idreservasi)
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
                    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
                    String username=pref.getString("Username_logged_in",null);

                    params.put("dari",username);
                    params.put("ke","admin");
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


}
