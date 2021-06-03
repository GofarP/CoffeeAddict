package com.gofar.anodais.Adapter.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.gofar.anodais.Fragment.Customer.FragKeranjang;
import com.gofar.anodais.Model.Keranjang;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gofar.anodais.Interface.totalInterface;

import com.gofar.anodais.GeoCoder.GeoCoder;

import org.json.JSONException;
import org.json.JSONObject;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.ViewHolder> {

    public List<Keranjang>itemcart=new ArrayList<>();

    Context context;

    totalInterface totalInterface;

    boolean enableTextWatcher;



    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView lblnamamenucart,lblhargamenucart,lblsubtotalcart,lbljeniscart,lbljmlitemkeranjang;
        ImageView ivcart;
        Button btnadd,btnmin;
        ImageButton btnhapusitem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblnamamenucart=itemView.findViewById(R.id.lblnamamenucart);
            lblhargamenucart=itemView.findViewById(R.id.lblhargamenucart);
            lblsubtotalcart=itemView.findViewById(R.id.lblsubtotalcart);
            lbljeniscart=itemView.findViewById(R.id.lbljeniscart);
            lbljmlitemkeranjang=itemView.findViewById(R.id.lbljmlitemkeranjang);

            ivcart=itemView.findViewById(R.id.ivcart);

            btnadd=itemView.findViewById(R.id.btnadditemkeranjang);

            btnmin=itemView.findViewById(R.id.btnminitemkeranjang);

            btnhapusitem=itemView.findViewById(R.id.btndeleteitemkeranjang);

        }
    }



   public KeranjangAdapter(ArrayList<Keranjang>itemcart, Context context,totalInterface totalInterface)
   {
       this.itemcart=itemcart;
       this.context=context;
       this.totalInterface=totalInterface;
   }



    @NonNull
    @Override
    public KeranjangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_keranjang,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KeranjangAdapter.ViewHolder holder, final int position) {

        final String idkeranjang=itemcart.get(position).getIdkeranjang();
        final String idpelanggan=itemcart.get(position).getIdpelanggan();
        final String idmenu=itemcart.get(position).getIdmenu();
        final String nama=itemcart.get(position).getNama();
        final String jenis=itemcart.get(position).getJenis();
        final int jumlah=itemcart.get(position).getJumlah();
        final String image=itemcart.get(position).getImage();
        final Double harga=itemcart.get(position).getHarga();
        final Double subtotal=itemcart.get(position).getSubtotal();
        final int stock=itemcart.get(position).getStock();
        final String catatan=itemcart.get(position).getCatatan();

        holder.lblnamamenucart.setText(nama);
        holder.lblhargamenucart.setText(NumberFormat.getCurrencyInstance().format(harga));
        holder.lblsubtotalcart.setText(NumberFormat.getCurrencyInstance().format(subtotal));
        holder.lbljeniscart.setText(jenis);
        holder.lbljmlitemkeranjang.setText(String.valueOf(jumlah));

        Glide.with(context).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivcart);

        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addJml(holder,position);
               minStock(position);
               updateJmlKeranjang(position,Integer.parseInt(holder.lbljmlitemkeranjang.getText().toString()));
            }
        });


        holder.btnmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int jml=Integer.parseInt(holder.lbljmlitemkeranjang.getText().toString());

                if(jml>1)
                {
                    minJml(holder,position);
                    addStock(position);
                }

            }
        });

        holder.btnhapusitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Hapus item")
                        .setMessage("Hapus "+itemcart.get(position).getNama()+" dari keranjang?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusItemKeranjang(idkeranjang,position);
                                FragKeranjang.total-=subtotal;
                                totalInterface.deleteTotal(subtotal);
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

    @Override
    public int getItemCount() {
        return itemcart.size();
    }



    public void addJml(ViewHolder holder, int position)
    {

        int jml=Integer.parseInt(holder.lbljmlitemkeranjang.getText().toString());

        Double harga=itemcart.get(position).getHarga();

        int stock=itemcart.get(position).getStock();

        if(jml>=stock)
        {
            holder.lbljmlitemkeranjang.setText(String.valueOf(stock));
            holder.lblsubtotalcart.setText(NumberFormat.getCurrencyInstance().format(harga*stock));
            Toast.makeText(context, "Tidak bisa membeli lebih dari stock", Toast.LENGTH_SHORT).show();
        }

        else {


            Double subtotal = itemcart.get(position).getSubtotal();

            itemcart.get(position).setJumlah(jml += 1);

            itemcart.get(position).setSubtotal(subtotal + harga);

            holder.lbljmlitemkeranjang.setText(String.valueOf(jml));

            holder.lblsubtotalcart.setText(NumberFormat.getCurrencyInstance().format(harga * jml));

            FragKeranjang.total += harga;

            totalInterface.addTotal(harga);
        }

    }



    public void minJml(ViewHolder holder, int position)
    {
        enableTextWatcher=false;

        int jml=Integer.parseInt(holder.lbljmlitemkeranjang.getText().toString());

        Double harga= itemcart.get(position).getHarga();

        Double subtotal=itemcart.get(position).getSubtotal();

        itemcart.get(position).setJumlah(jml-=1);

        itemcart.get(position).setSubtotal(subtotal - harga);


        holder.lbljmlitemkeranjang.setText(String.valueOf(jml));
        holder.lblsubtotalcart.setText(NumberFormat.getCurrencyInstance().format(harga*jml));

        FragKeranjang.total-=harga;

        totalInterface.minTotal(harga);

    }



    public void hapusItemKeranjang(final String idkeranjang,final int position)
    {
        try
        {
            String url_delete=Server.URL+"deletekeranjangitem.php";
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
                            itemcart.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,itemcart.size());
                        }

                        else
                        {
                            Toast.makeText(context, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
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
                    params.put("idkeranjang",idkeranjang);
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



    public void addStock(final int position)
    {
        try
        {
            String urlAddStock=Server.URL+"addstockcheckout.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, urlAddStock, new Response.Listener<String>() {
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

                    params.put("idmenu",itemcart.get(position).getIdmenu());
                    params.put("stock","1");

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


    public void minStock(final int position)
    {
        try
        {
            String urlMinStock=Server.URL+"minstockcheckout.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, urlMinStock, new Response.Listener<String>() {
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
                    params.put("idmenu",itemcart.get(position).getIdmenu());
                    params.put("stock","1");

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


    public void updateJmlKeranjang(final int position,final int jumlah)
    {
        try
        {
            String updatejml=Server.URL+"setjumlahcart.php";
            RequestQueue queue=Volley.newRequestQueue(context);
            StringRequest request=new StringRequest(Request.Method.POST, updatejml, new Response.Listener<String>() {
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
                    params.put("jumlah", String.valueOf(jumlah));
                    params.put("idkeranjang",itemcart.get(position).getIdkeranjang());

                    return params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }


}
