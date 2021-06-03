package com.gofar.anodais.Adapter.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gofar.anodais.Model.Notifikasi;
import com.gofar.anodais.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PemberitahuanCustomerAdapter extends RecyclerView.Adapter<PemberitahuanCustomerAdapter.ViewHolder> {

    Context context;
    List<Notifikasi> notifikasiList=new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblpesan,lbltgl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblpesan=itemView.findViewById(R.id.lblnotifikasipelanggan);
            lbltgl=itemView.findViewById(R.id.lbltglnotifikasipelanggan);

        }


    }

    public  PemberitahuanCustomerAdapter(Context context,List<Notifikasi>notifikasiList)
    {
        this.context=context;
        this.notifikasiList=notifikasiList;
    }

    @NonNull
    @Override
    public PemberitahuanCustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.layout_notifikasi,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PemberitahuanCustomerAdapter.ViewHolder holder, int position) {

        try
        {
            final String idNotifikasi=notifikasiList.get(position).getIdNotifikasi();
            final String pesan=notifikasiList.get(position).getPesan();
            final String tanggalPesan=notifikasiList.get(position).getTanggal();

            holder.lblpesan.setText(pesan);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date tgl=sdf.parse(tanggalPesan);

            sdf=new SimpleDateFormat("dd-MM-yyyy");
            String formattedTanggalPesan=sdf.format(tgl);

            holder.lbltgl.setText(formattedTanggalPesan);

        }

        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return notifikasiList.size();
    }


}
