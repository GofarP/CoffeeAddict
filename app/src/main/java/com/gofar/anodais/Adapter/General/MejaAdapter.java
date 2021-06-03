package com.gofar.anodais.Adapter.General;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gofar.anodais.Activity.Admin.EditMejaActivity;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MejaAdapter extends RecyclerView.Adapter<MejaAdapter.ViewHolder> {

    public List<Reservasi>mejalist=new ArrayList<>();
    Context Contexts;
    int jenisView;

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView lblnomeja,lblreservasinomeja,lblstatus,lbljenis,lblreservasijenis;
        Button btneditmeja,btndeletemeja;
        CardView cvreservasimejacustomer;
        ImageView ivkelolameja,ivdaftarmeja;
        Spinner spnbulan,spntgl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                    if(jenisView==1)
                    {
                        lblnomeja=(TextView)itemView.findViewById(R.id.lblnomeja);
                        lbljenis=(TextView)itemView.findViewById(R.id.lbljenismeja);
                        btneditmeja=(Button)itemView.findViewById(R.id.btneditmeja);
                        btndeletemeja=(Button)itemView.findViewById(R.id.btndeletemeja);
                        ivkelolameja=(ImageView)itemView.findViewById(R.id.ivkelolameja);
                    }

                    else if(jenisView==2)
                    {
                        lblreservasinomeja=(TextView)itemView.findViewById(R.id.lblreservasinomeja);
                        lblreservasijenis=(TextView)itemView.findViewById(R.id.lblreservasijenismeja);
                        cvreservasimejacustomer=(CardView)itemView.findViewById(R.id.cvreservasimejacustomer);
                        ivdaftarmeja=(ImageView)itemView.findViewById(R.id.ivdaftarmeja);

                    }

            }


    }

    public MejaAdapter(List<Reservasi> mejalist, Context contexts, int jenisView) {
        this.mejalist = mejalist;
        this.Contexts = contexts;
        this.jenisView=jenisView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;

        switch (jenisView)
        {
            case 1:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kelola_meja,parent,false);
                break;

            case 2:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_meja,parent,false);
                break;
        }


        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        switch (jenisView)
        {
            case 1:
                kelolaMejaView(holder,position);
                break;

            case 2:
                sewaMejaView(holder, position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mejalist.size();
    }


    public void kelolaMejaView(ViewHolder holder, final int position)
    {
        final String id=mejalist.get(position).getId_meja();
        final String nomeja=mejalist.get(position).getNomeja();
        final String images=mejalist.get(position).getMejaImage();
        final String jenis=mejalist.get(position).getJenis();


        holder.lblnomeja.setText("Meja "+nomeja);
        holder.lbljenis.setText(jenis);

        Glide.with(Contexts).load(Server.URL+"/meja/"+images).centerCrop().into(holder.ivkelolameja);

        holder.btneditmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                       Bundle bundle=new Bundle();
                       bundle.putString("nomeja",nomeja);
                       bundle.putString("idmeja",id);
                       bundle.putString("jenis",jenis);

                       Intent intent=new Intent(Contexts, EditMejaActivity.class);
                       intent.putExtras(bundle);
                       Contexts.startActivity(intent);

                       ((Activity)Contexts).finish();
            }
        });

        holder.btndeletemeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder=new AlertDialog.Builder(Contexts);
                builder.setTitle("Hapus meja meja "+nomeja+" ?")
                        .setMessage("Meja yang sudah dihapus tidak dapat dikemablikan lagi!")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletemeja(position,nomeja);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                builder.create().show();

            }
        });
    }


    public void sewaMejaView(final ViewHolder holder, final int position)
    {

        final String idmeja=mejalist.get(position).getId_meja();
        final String nomeja=mejalist.get(position).getNomeja();
        final String images=mejalist.get(position).getMejaImage();
        final String jenis=mejalist.get(position).getJenis();



        holder.lblreservasinomeja.setText(nomeja);
        holder.lblreservasijenis.setText(jenis);

        Glide.with(Contexts).load(Server.URL+"/meja/"+images).into(holder.ivdaftarmeja);

        holder.ivdaftarmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(Contexts);
                LayoutInflater inflater=LayoutInflater.from(Contexts);
                View dialogView=inflater.inflate(R.layout.layout_zoom_image,null);
                builder.setView(dialogView);

                ImageView ivzoom=dialogView.findViewById(R.id.ivzoommeja);

                Glide.with(dialogView).load(Server.URL+"/meja/"+images).into(ivzoom);

                builder.show();

            }
        });




        holder.cvreservasimejacustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar=Calendar.getInstance();


                DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//                                Toast.makeText(Contexts, sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();

                                tambahReservasi(position,images,sdf.format(calendar.getTime()));

                            }
                        };

                        new TimePickerDialog(Contexts,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };

                new DatePickerDialog(Contexts,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }


    public void deletemeja(final int position, final String nomeja)
    {
        try
        {
            String url_hapusmeja= Server.URL+"deletemeja.php";
            RequestQueue queue= Volley.newRequestQueue(Contexts);

            StringRequest stringRequest=new StringRequest(Request.Method.POST, url_hapusmeja, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            mejalist.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,mejalist.size());

                            Toast.makeText(Contexts, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(Contexts, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch(Exception e)
                    {
                        Toast.makeText(Contexts, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Contexts, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("nomeja",nomeja);
                    return params;
                }
            };

            queue.add(stringRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(Contexts, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    public void tambahReservasi(final int position, final String image,final String tglsewa)
    {
        try
        {
            String url_reservasi= Server.URL+"addreservasi.php";
            RequestQueue queue= Volley.newRequestQueue(Contexts);
            StringRequest request=new StringRequest(Request.Method.POST, url_reservasi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("status").equals("Tersedia"))
                        {
                            Reservasi reservasi=new Reservasi();
                            reservasi.setNomeja(mejalist.get(position).getNomeja());
                            reservasi.setStatus("Disewa");
                            reservasi.setMejaImage(image);
                            reservasi.setJenis(mejalist.get(position).getJenis());
                            mejalist.set(position,reservasi);
                            notifyItemChanged(position);
                            Toast.makeText(Contexts, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else if(object.getString("status").equals("Disewa"))
                        {
                            Toast.makeText(Contexts, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(Contexts, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(Contexts, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Contexts, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();

                    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(Contexts);
                    String iduser=pref.getString("id",null);

                    params.put("idmeja",mejalist.get(position).getId_meja());
                    params.put("iduser",iduser);
                    params.put("tglsewa",tglsewa);

                    return  params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(Contexts, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }




}
