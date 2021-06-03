package com.gofar.anodais.Adapter.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gofar.anodais.Model.History;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

     Context context;

     ArrayList<History>historyList=new ArrayList<>();

     class ViewHolder extends RecyclerView.ViewHolder
     {
         TextView lblnamamenu, lbljumlah, lblharga, lblsubtotal, lbltglpesan;
         ImageView ivmenu;

         public ViewHolder(@NonNull View itemView)
         {
             super(itemView);

             lblnamamenu=itemView.findViewById(R.id.lblnamamenuhistory);
             lbljumlah=itemView.findViewById(R.id.lbljumlahhistory);
             lblharga=itemView.findViewById(R.id.lblhargamenuhistory);
             lblsubtotal=itemView.findViewById(R.id.lblsubtotalhistory);
             lbltglpesan=itemView.findViewById(R.id.lbltglpesanhistory);
             ivmenu=itemView.findViewById(R.id.ivhistory);
         }

     }

     public HistoryAdapter(ArrayList<History>historyList,Context context)
     {
         this.historyList=historyList;
         this.context=context;
     }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history, null, false);

         return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

         String namaMenu=historyList.get(position).getNama();
         int jumlahMenu=historyList.get(position).getJumlah();
         double hargaMenu=historyList.get(position).getHarga();
         double subTotal=historyList.get(position).getSubtotal();
         String tglPesan=historyList.get(position).getTglpesan();
         String image=historyList.get(position).getImage();

         try
         {
             SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             Date newTglPesan=sdf.parse(tglPesan);
             sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

             holder.lblnamamenu.setText(namaMenu);
             holder.lbljumlah.setText(String.valueOf(jumlahMenu));
             holder.lblharga.setText(NumberFormat.getCurrencyInstance().format(hargaMenu));
             holder.lblsubtotal.setText(NumberFormat.getCurrencyInstance().format(subTotal));
             holder.lbltglpesan.setText(sdf.format(newTglPesan));

             Glide.with(context).load(Server.URL+"/image/"+image).centerCrop().into(holder.ivmenu);


         }

         catch (Exception e)
         {
             Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
         }



    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


}
