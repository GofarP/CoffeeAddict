package com.gofar.anodais.Adapter.General;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gofar.anodais.Activity.Admin.EditMenuActivity;
import com.gofar.anodais.Activity.Customer.DetailMenuActivity;
import com.gofar.anodais.Model.Menu;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    public List<Menu> menuList=new ArrayList<>();
    Context  Contexts;

    int jenisView;

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView lblmenuid,lblmenunama,lblmenuharga,lblmenustatus,lblnamamenu,lblharga,lblstatus,lblstock;
        Button btnedit,btndelete,btnbeli;
        ImageView ivmenu;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            if (jenisView==1)
            {
                lblmenuid = (TextView) itemView.findViewById(R.id.lblidmenu);
                lblmenunama = (TextView) itemView.findViewById(R.id.lblnamamenu);
                lblmenuharga = (TextView) itemView.findViewById(R.id.lblhargamenu);
                lblmenustatus = (TextView) itemView.findViewById(R.id.lblstatusmenu);
                lblstock=(TextView)itemView.findViewById(R.id.lbljmlstock);

                btnedit=(Button)itemView.findViewById(R.id.btneditmenu);
                btndelete=(Button)itemView.findViewById(R.id.btndeletemenu);

                ivmenu=(ImageView)itemView.findViewById(R.id.ivmenu);
            }

            else if(jenisView==2 || jenisView==3)
            {
                lblnamamenu=itemView.findViewById(R.id.lblnamapesanmenu);
                lblharga=itemView.findViewById(R.id.lblhargapesanmenu);
                lblstatus=itemView.findViewById(R.id.lblstatuspesanmenu);
                lblstock=itemView.findViewById(R.id.lblstockpesanmenu);
                btnbeli=itemView.findViewById(R.id.btnbeli);
                ivmenu=itemView.findViewById(R.id.ivpesanmenu);
            }



        }
    }

    public MenuAdapter(ArrayList<Menu> MenuList, Context contexts,int jenisView)
    {
        this.menuList=MenuList;
        this.Contexts=contexts;
        this.jenisView=jenisView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=null;

        switch(jenisView)
        {
            case 1:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu,parent,false);
                break;

            case 2:
            case 3:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pesan_menu,parent,false);
                break;

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        switch (jenisView)
        {
            case 1:
                kelolaMenu(holder,position);
                break;

            case 2:
                daftarMenu(holder,position);
                break;
            case 3:
                menuDitempat(position,holder);
                break;

        }


    }



    @Override
    public int getItemCount() {
        return menuList.size();
    }




    public void kelolaMenu(ViewHolder holder, final int position)
    {

            final String id=menuList.get(position).getId();
            final String nama=menuList.get(position).getNama();
            final Double harga=menuList.get(position).getHarga();
            final String jenis=menuList.get(position).getJenis();
            final String status=menuList.get(position).getStatus();
            final String image=menuList.get(position).getImage();
            final String deskripsi=menuList.get(position).getDeskripsi();
            final int stock=menuList.get(position).getStock();


            holder.lblmenuid.setText(id);
            holder.lblmenunama.setText(nama);
            holder.lblmenuharga.setText(NumberFormat.getCurrencyInstance().format(harga));
            holder.lblstock.setText(String.valueOf(stock));

        Glide.with(Contexts).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivmenu);


            if (status.equals("Tersedia"))
            {
                holder.lblmenustatus.setTextColor(Color.parseColor("#007E4E"));
            }

            else
            {
                holder.lblmenustatus.setTextColor(Color.parseColor("#BE1D25"));
            }

            holder.lblmenustatus.setText(status);

            holder.btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMenu(position);
                }
            });

            holder.btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putString("id",id);
                    bundle.putString("nama",nama);
                    bundle.putString("jenis",jenis);
                    bundle.putDouble("harga",harga);
                    bundle.putInt("stock",stock);
                    bundle.putString("status",status);
                    bundle.putString("deskripsi",deskripsi);

                    Intent intent=new Intent(Contexts, EditMenuActivity.class);
                    intent.putExtras(bundle);
                    Contexts.startActivity(intent);

                    ((Activity)Contexts).finish();

                }
            });

    }


    private void daftarMenu(ViewHolder holder,int position)
    {

        final String id=menuList.get(position).getId();
        final String nama=menuList.get(position).getNama();
        final Double harga=menuList.get(position).getHarga();
        final String jenis=menuList.get(position).getJenis();
        final String status=menuList.get(position).getStatus();
        final String image=menuList.get(position).getImage();
        final String deskripsi=menuList.get(position).getDeskripsi();
        final int stock=menuList.get(position).getStock();

        holder.lblnamamenu.setText(nama);
        holder.lblstatus.setText(status);
        holder.lblharga.setText(NumberFormat.getCurrencyInstance().format(harga));
        holder.lblstock.setText(String.valueOf(stock));

        Glide.with(Contexts).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivmenu);



        if(status.equals("Tersedia"))
        {
            holder.lblstatus.setTextColor(Color.parseColor("#007E4E"));
        }

        else
        {
            holder.lblstatus.setTextColor(Color.parseColor("#BE1D25"));
            holder.btnbeli.setEnabled(false);
            holder.btnbeli.setBackgroundColor(Color.parseColor("#ADAFAE"));
        }


        if(stock==0)
        {
            holder.btnbeli.setEnabled(false);
            holder.btnbeli.setBackgroundColor(Color.parseColor("#ADAFAE"));
            holder.lblstatus.setText("Stok Habis");
            holder.lblstatus.setTextColor(Color.parseColor("#7F7F7F"));
        }



        holder.btnbeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Contexts, DetailMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                bundle.putString("nama",nama);
                bundle.putString("jenis",jenis);
                bundle.putDouble("harga",harga);
                bundle.putString("status",status);
                bundle.putString("deskripsi",deskripsi);
                bundle.putString("image",image);
                bundle.putInt("stock",stock);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Contexts.startActivity(intent);

            }
        });

    }


    private void menuDitempat(int position, ViewHolder holder)
    {
        final String id=menuList.get(position).getId();
        final String nama=menuList.get(position).getNama();
        final Double harga=menuList.get(position).getHarga();
        final String jenis=menuList.get(position).getJenis();
        final String status=menuList.get(position).getStatus();
        final String image=menuList.get(position).getImage();
        final String deskripsi=menuList.get(position).getDeskripsi();
        final int stock=menuList.get(position).getStock();

        holder.lblnamamenu.setText(nama);
        holder.lblstatus.setText(status);
        holder.lblharga.setText(NumberFormat.getCurrencyInstance().format(harga));
        holder.lblstock.setText(String.valueOf(stock));

        Glide.with(Contexts).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivmenu);



        if(status.equals("Tersedia"))
        {
            holder.lblstatus.setTextColor(Color.parseColor("#007E4E"));
        }

        else
        {
            holder.lblstatus.setTextColor(Color.parseColor("#BE1D25"));
            holder.btnbeli.setEnabled(false);
            holder.btnbeli.setBackgroundColor(Color.parseColor("#ADAFAE"));
        }


        if(stock==0)
        {
            holder.btnbeli.setEnabled(false);
            holder.btnbeli.setBackgroundColor(Color.parseColor("#ADAFAE"));
            holder.lblstatus.setText("Stok Habis");
            holder.lblstatus.setTextColor(Color.parseColor("#7F7F7F"));
        }


        holder.btnbeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Contexts, DetailMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                bundle.putString("nama",nama);
                bundle.putString("jenis",jenis);
                bundle.putDouble("harga",harga);
                bundle.putString("status",status);
                bundle.putString("deskripsi",deskripsi);
                bundle.putString("image",image);
                bundle.putInt("stock",stock);
                bundle.putBoolean("pesan_disini",true);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Contexts.startActivity(intent);

            }
        });
    }




    public void deleteMenu(final int position)
    {

        String nama=menuList.get(position).getNama();

        AlertDialog.Builder builder=new AlertDialog.Builder(Contexts);
        builder.setTitle("Hapus menu "+nama+" ?")
                .setMessage("Menu yang sudah dihapus tidak dapat dikembalikan")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            RequestQueue requestQueue = Volley.newRequestQueue(Contexts);
                            String url_delete = Server.URL + "deletemenu.php";
                            StringRequest strdelete = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        int success;
                                        String TAG_success = "success";
                                        String TAG_MESSAGE = "message";
                                        JSONObject jObj = new JSONObject(response);
                                        success = jObj.getInt(TAG_success);

                                        if (success == 1) {
                                            Toast.makeText(Contexts, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                            menuList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, menuList.size());

                                        } else {
                                            Toast.makeText(Contexts, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                        }


                                    } catch (JSONException e) {
                                        Toast.makeText(Contexts, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Contexts, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", menuList.get(position).getId());

                                    return params;
                                }
                            };

                            requestQueue.add(strdelete);

                        } catch (Exception e) {
                            Toast.makeText(Contexts, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

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

}

