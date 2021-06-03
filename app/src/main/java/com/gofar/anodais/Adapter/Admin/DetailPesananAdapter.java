package com.gofar.anodais.Adapter.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gofar.anodais.Model.Keranjang;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import java.text.NumberFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailPesananAdapter extends RecyclerView.Adapter<DetailPesananAdapter.ViewHolder> {

    Context context;
    List<Keranjang>detailPesananList;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblnamapesanan,lblhargapesanan,lblsubtotalpesanan,lblcatatanpesanan,lbljumlah;
        ImageView ivpesanan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblnamapesanan=itemView.findViewById(R.id.lblnamamenudetailpesanan);
            lblhargapesanan=itemView.findViewById(R.id.lblhargamenudetailpesanan);
            lblsubtotalpesanan=itemView.findViewById(R.id.lblsubtotaldetailpesanan);
            lblcatatanpesanan=itemView.findViewById(R.id.lblcatatandetailpesanan);
            lbljumlah=itemView.findViewById(R.id.lbljumlahdetailpesanan);
            ivpesanan=itemView.findViewById(R.id.ivdetailpesanan);
        }
    }

    public DetailPesananAdapter(Context context, List<Keranjang>detailPesananList)
    {
        this.context=context;
        this.detailPesananList=detailPesananList;
    }

    @NonNull
    @Override
    public DetailPesananAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_detail_pesanan,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPesananAdapter.ViewHolder holder, final int position) {

        String idUser=detailPesananList.get(position).getIdpelanggan();
        String idMenu=detailPesananList.get(position).getIdmenu();
        String namaMenu=detailPesananList.get(position).getNama();
        Double hargaMenu=detailPesananList.get(position).getHarga();
        Double subTotal=detailPesananList.get(position).getSubtotal();
        String catatan=detailPesananList.get(position).getCatatan();
        String image=detailPesananList.get(position).getImage();
        int pesanan=detailPesananList.get(position).getJumlah();

        holder.lblnamapesanan.setText(namaMenu);
        holder.lblhargapesanan.setText(NumberFormat.getCurrencyInstance().format(hargaMenu));
        holder.lblsubtotalpesanan.setText(NumberFormat.getCurrencyInstance().format(subTotal));
        holder.lbljumlah.setText(String.valueOf(pesanan));

        Glide.with(context).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivpesanan);

        if(catatan.equals("-") || catatan.equals(""))
        {
            holder.lblcatatanpesanan.setText("Tidak ada catatan dari pengguna");
            holder.lblcatatanpesanan.setEnabled(false);
        }

        else
        {
            holder.lblcatatanpesanan.setText("Ada catatan dari pengguna(Klik disini)");
            holder.lblcatatanpesanan.setTextColor(Color.rgb(0,165,255));
        }

        holder.lblcatatanpesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCatatan(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return detailPesananList.size();
    }


    public void getCatatan(final int position)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View dialogView=inflater.inflate(R.layout.popupcatatanmenu,null,false);
        builder.setView(dialogView);

        TextView lblcatatan=dialogView.findViewById(R.id.lblcatatanmenu);

        lblcatatan.setText(detailPesananList.get(position).getCatatan());

        builder.show();
    }


}
