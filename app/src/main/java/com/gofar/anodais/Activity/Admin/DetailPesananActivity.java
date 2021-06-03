package com.gofar.anodais.Activity.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PDFPrint;
import android.view.View;
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
import com.gofar.anodais.Activity.Customer.NotifikasiActivity;
import com.gofar.anodais.Adapter.Admin.DetailPesananAdapter;
import com.gofar.anodais.Adapter.Customer.KeranjangAdapter;
import com.gofar.anodais.Adapter.Customer.LihatMenuAdapter;
import com.gofar.anodais.Adapter.General.DaftarPengajuanMejaAdapter;
import com.gofar.anodais.Model.CheckOut;
import com.gofar.anodais.Model.Keranjang;
import com.gofar.anodais.Model.MenuPengajuan;
import com.gofar.anodais.Model.Notifikasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPesananActivity extends AppCompatActivity {


    RecyclerView rvdetailPesanan;

    RecyclerView.Adapter adapterDetailPesanan;

    RecyclerView.LayoutManager detailLayoutManager;

    List<Keranjang>detailPesananList=new ArrayList<>();

    String JENIS_VIEW="Admin";
    int diskon=0;

    Button btnserahpesanan,btncetakstruk;

    Double total=0.0;

    TextView lbltotaldetailpesanan,lbldiskontotaldetailpesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        rvdetailPesanan=findViewById(R.id.rvdetailpesanan);
        btnserahpesanan=findViewById(R.id.btnserahpesanan);
        lbltotaldetailpesanan=findViewById(R.id.lbltotaldetailpesanan);
        lbldiskontotaldetailpesanan=findViewById(R.id.lbldiskontotaldetailpesanan);

        btncetakstruk=findViewById(R.id.btncetakstruk);

        detailLayoutManager=new LinearLayoutManager(this);
        rvdetailPesanan.setLayoutManager(detailLayoutManager);
        rvdetailPesanan.setHasFixedSize(true);


        Bundle bundle=getIntent().getExtras();

        final String iduser=bundle.getString("iduser");
        final String idreservasi=bundle.getString("idreservasi");
        final boolean pesanDitempat=bundle.getBoolean("pesan_ditempat");


        if(pesanDitempat)
        {
            getPesananDitempat(idreservasi);
        }

        else
        {
            getPesanan(iduser);
        }


        btnserahpesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(DetailPesananActivity.this);
                    builder.setTitle("Serahkan Pesanan?")
                            .setMessage("Pesanan Yang Sudah Diserahkan Tidak Dapat Dikembalikan")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try
                                    {
                                        String pesan="";

                                        if(pesanDitempat)
                                        {
                                            pesan="Transaksi Meja Berhasil!";
                                            deletePesananMeja(idreservasi);
                                            addTransaksi(iduser, "Reservasi Meja", String.valueOf(total));
                                            addNotifikasi(iduser,pesan);
                                        }

                                        else
                                        {
                                            pesan="Pesananmu sudah diantarkan";

                                            addTransaksi(iduser,"Makanan & Minuman",String.valueOf(total));
                                            addNotifikasi(iduser,pesan);
                                            deleteCheckout(iduser);
                                        }

                                        startActivity(new Intent(DetailPesananActivity.this,PemberitahuanAdminActivity.class));

                                    }

                                    catch (Exception e)
                                    {
                                        Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

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

                catch (Exception e)
                {
                    Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        btncetakstruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetakStruk();
            }
        });

    }


    public void getPesanan(final String id)
    {
        try
        {
            String url_getPesanan=Server.URL+"getDetailPesanan.php";
            RequestQueue queue= Volley.newRequestQueue(this);
            StringRequest request=new StringRequest(Request.Method.POST, url_getPesanan, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    detailPesananList.clear();

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        JSONArray jar=object.getJSONArray("data");

                        if(jar.length()>0)
                        {
                            for(int i=0;i<jar.length();i++)
                            {
                                JSONObject jsonObject=jar.getJSONObject(i);
                                Keranjang keranjang=new Keranjang();

                                keranjang.setIdmenu(jsonObject.getString("idMenu"));
                                keranjang.setIdpelanggan(jsonObject.getString("idPelanggan"));
                                keranjang.setNama(jsonObject.getString("nama"));
                                keranjang.setHarga(jsonObject.getDouble("harga"));
                                keranjang.setImage(jsonObject.getString("image"));
                                keranjang.setJenis(jsonObject.getString("jenis"));
                                keranjang.setJumlah(jsonObject.getInt("jumlah"));
                                keranjang.setSubtotal(jsonObject.getDouble("subTotal"));
                                keranjang.setCatatan(jsonObject.getString("catatan"));

                                detailPesananList.add(keranjang);

                                total+=jsonObject.getDouble("subTotal");

                            }

                            lbltotaldetailpesanan.setText(NumberFormat.getCurrencyInstance().format(total));

                            if(total >50000)
                            {
                                lbltotaldetailpesanan.setBackgroundResource(R.drawable.strike_line);
                                lbldiskontotaldetailpesanan.setVisibility(View.VISIBLE);
                                total=total-(total * 10 / 100);
                                lbldiskontotaldetailpesanan.setText(NumberFormat.getCurrencyInstance().format(total));
                                diskon=10;
                            }

                        }


                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    adapterDetailPesanan=new DetailPesananAdapter(DetailPesananActivity.this,detailPesananList);
                    rvdetailPesanan.setAdapter(adapterDetailPesanan);
                    adapterDetailPesanan.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void getPesananDitempat(final String idreservasi)
    {
        try
        {
            String url= Server.URL+"getpengajuanpesanan.php";
            RequestQueue queue= Volley.newRequestQueue(DetailPesananActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        ArrayList<MenuPengajuan>pengajuanList=new ArrayList<>();
                        JSONObject object=new JSONObject(response);
                        JSONArray jsonArray=object.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            for(int i=0; i < jsonArray.length();i++)
                            {
                                JSONObject jsonArrayObject=jsonArray.getJSONObject(i);

                                MenuPengajuan menuPengajuan=new MenuPengajuan();
                                menuPengajuan.setId_menu(jsonArrayObject.getString("id_menu"));
                                menuPengajuan.setNama(jsonArrayObject.getString("nama"));
                                menuPengajuan.setHarga(jsonArrayObject.getDouble("harga"));
                                menuPengajuan.setJumlah(jsonArrayObject.getInt("jumlah"));
                                menuPengajuan.setImage(jsonArrayObject.getString("image"));
                                menuPengajuan.setSubtotal(jsonArrayObject.getDouble("subtotal"));
                                menuPengajuan.setIdreservasi(jsonArrayObject.getString("id_reservasi"));
                                menuPengajuan.setStock(jsonArrayObject.getInt("stock"));

                                total+=jsonArrayObject.getDouble("subtotal");

                                pengajuanList.add(menuPengajuan);
                            }

                            lbltotaldetailpesanan.setText(NumberFormat.getCurrencyInstance().format(total));

                            if(total >50000)
                            {
                                lbltotaldetailpesanan.setBackgroundResource(R.drawable.strike_line);
                                lbldiskontotaldetailpesanan.setVisibility(View.VISIBLE);
                                total=total-(total * 10 / 100);
                                lbldiskontotaldetailpesanan.setText(NumberFormat.getCurrencyInstance().format(total));
                                diskon=10;
                            }

                            adapterDetailPesanan=new LihatMenuAdapter(DetailPesananActivity.this, pengajuanList, JENIS_VIEW);
                            rvdetailPesanan.setAdapter(adapterDetailPesanan);
                            adapterDetailPesanan.notifyDataSetChanged();

                        }


                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("idreservasi",idreservasi);
                    return params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void addTransaksi(final String iduser, final String jenis, final String total)
    {
        try
        {
            String url_addtransaksi=Server.URL+"addtransaksi.php";

            RequestQueue queue= Volley.newRequestQueue(DetailPesananActivity.this);

            StringRequest request=new StringRequest(Request.Method.POST, url_addtransaksi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(DetailPesananActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(DetailPesananActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (Exception e)
                    {
                        Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("iduser",iduser);
                    params.put("jenis",jenis);
                    params.put("total",total);

                    return params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public void deleteCheckout(final String id)
    {
        try
        {
            String url_delete=Server.URL+"deleteCheckout.php";
            RequestQueue queue=Volley.newRequestQueue(DetailPesananActivity.this);

            StringRequest request=new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            detailPesananList.clear();
                            adapterDetailPesanan.notifyDataSetChanged();
                            startActivity(new Intent(DetailPesananActivity.this,AdminActivity.class));
                            finish();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String>params=new HashMap<>();
                    params.put("idpelanggan",id);
                    return params;
                }
            };

            queue.add(request);
        }

        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void addNotifikasi(final String iduser, final String pesan)
    {
        try
        {
            String url_notifikasi=Server.URL+"addnotifikasi.php";
            RequestQueue queue=Volley.newRequestQueue(this);
            StringRequest request=new StringRequest(Request.Method.POST, url_notifikasi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("0"))
                        {
                            Toast.makeText(DetailPesananActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DetailPesananActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("iduser",iduser);
                    params.put("pesan",pesan);

                    return params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void deletePesananMeja(final String idreservasi)
    {
        try
        {
            String url=Server.URL+"deletepesananmeja.php";
            RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("idreservasi",idreservasi);
                    return params;
                }
            };

            queue.add(stringRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cetakStruk()
    {
        String html="<!DOCTYPE html>\n"+
       "<html>\n"+

        "<head>\n"+

        "<style>\n"+

        ".footer {\n"+
            "position: fixed;\n"+
            "left: 0;\n"+
            "bottom: 0;\n"+
            "width: 100%;\n"+
            "color: black;\n"+
            "margin-left:20px;\n"+
        "}\n"+

        "</style>\n"+

        "</head>\n"+

    "<body>\n"+

        "<table style='width:100%'>\n"+

        "<tr>\n"+

            "<td rowspan='3' style='height:150px;width:150px'>\n"+
                "<img src='https://i.postimg.cc/BZy2S5NW/index.jpg' style='display:block' width='100%' >\n"+
            "</td>\n"+

            "<td>\n"+
                "<h1 style='text-align:center'>Coffee Addict</h2>\n"+
            "</td>\n"+

            "<td style='text-align:right' width='20%'>\n"+
                "<h4>Struk Belanja</h4>\n"+
                "<p>Tanggal:<br>\n"+
            "20-02-1999</p>\n"+
            "</td>\n"+

        "</tr>"+

        "<tr>\n"+
            "<td>&nbsp;</td>\n"+
            "<td>&nbsp;</td>\n"+
        "</tr>\n"+

        "<tr>\n"+
            "<td>&nbsp;</td>\n"+
            "<td>&nbsp;</td>\n"+
        "</tr>\n"+

    "</table>\n"+

     "<table style='width: 100%; margin-top:30px;border-color:black;border-collapse:collapse' border='1px solid'>\n"+

            "<tr>\n"+
                "<th>Nama Menu</th>\n"+
                "<th>Jumlah</th>\n"+
                "<th>Harga</th>\n"+
                "<th>Subtotal</th>\n"+
            "</tr>\n"+

            "<tbody>\n";

        for(int i=0;i<detailPesananList.size();i++)
        {
            html+="<tr>\n"+
                    "<td>"+detailPesananList.get(i).getNama()+"</td>\n"+
                    "<td>"+detailPesananList.get(i).getJumlah()+"</td>\n"+
                    "<td>"+NumberFormat.getCurrencyInstance().format(detailPesananList.get(i).getHarga())+"</td>\n"+
                    "<td>"+ NumberFormat.getCurrencyInstance().format(detailPesananList.get(i).getSubtotal())+"</td>\n"+
                    "</tr>\n";
        }

                html+="<tr>\n"+
                    "<td colspan='3' style='text-align:center'>Diskon</td>\n"+
                    "<td>"+diskon+"%</td>\n"+
                "</tr>\n"+

                "<tr>\n"+
                    "<td colspan='3' style='text-align:center'>Total</td>\n"+
                    "<td>"+NumberFormat.getCurrencyInstance().format(total)+"</td>\n"+
                "</tr>\n"+
            "</tbody>\n"+
    "</table>\n"+



     "<div class='footer'>\n"+

            "<div style='width: 100%; display: table;'>\n"+

                "<div style='display: table-row; height: 100px;'>\n"+

                    "<div style='width: 50%; display: table-cell;'>\n"+

                        "<h2>Coffee Addict</h2>\n"+

                        "<p>\n"+
                            "Alamat: Jl.Raya H.Fisabilillah, Km 8 atas, Bukit Bestari<br>\n"+
                            "Kota Tanjung Pinang, Kepulauan Riau 29122<br>\n"+
                            "Telp:08199217865\n"+
                        "</p>\n"+

                    "</div>\n"+

                "</div>\n"+

            "</div>\n"+

        "</div>\n"+

    "</body>\n"+

"</html>";

        FileManager.getInstance().cleanTempFolder(getApplicationContext());
        final File savedPDFFile=FileManager.getInstance().createTempFile(getApplicationContext(),"pdf",false);
        PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {
                Uri pdfUri=Uri.fromFile(savedPDFFile);

                Intent intentPdfViewer=new Intent(getApplicationContext(),PdfViewerActivity.class);
                intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                startActivity(intentPdfViewer);


            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
