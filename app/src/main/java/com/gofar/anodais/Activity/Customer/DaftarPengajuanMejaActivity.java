package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.print.PDFPrint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Activity.Admin.PdfViewerActivity;
import com.gofar.anodais.Activity.Admin.ReportActivity;
import com.gofar.anodais.Adapter.General.DaftarPengajuanMejaAdapter;
import com.gofar.anodais.Model.MenuPengajuan;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.Model.StrukMenu;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;
import com.tejpratapsingh.pdfcreator.activity.PDFViewerActivity;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DaftarPengajuanMejaActivity extends AppCompatActivity {

    RecyclerView rvdaftarreservasi;
    ArrayList<Reservasi> reservasiList=new ArrayList<>();
    ArrayList<StrukMenu>strukList=new ArrayList<>();
    RecyclerView.Adapter daftarreservasiadapter;
    RecyclerView.LayoutManager daftarLayoutManager;
    SearchView txtsearchnomeja;
    Button btnpesanmenu;
    int total;
    TextView lbltotal;

    String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pengajuan_meja);

        txtsearchnomeja=findViewById(R.id.txtsearchnomejacustomer);
        rvdaftarreservasi=findViewById(R.id.rvdaftarreservasi);
        lbltotal=findViewById(R.id.lbltotalpengajuanmeja);
        btnpesanmenu=findViewById(R.id.btnpesanmenu);

        daftarLayoutManager=new LinearLayoutManager(DaftarPengajuanMejaActivity.this);
        rvdaftarreservasi.setLayoutManager(daftarLayoutManager);
        rvdaftarreservasi.setHasFixedSize(true);

        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
        String iduser=preferences.getString("id",null);

        fillDataReservasi();


        btnpesanmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaftarPengajuanMejaActivity.this, PesanDitempatActivity.class));
            }
        });


        txtsearchnomeja.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.equals(""))
                {
                   fillDataReservasi();
                }

                else
                {
                    searchMeja(newText);
                }


                return true;
            }
        });

    }



    public void fillDataReservasi()
    {
        try
        {
            String url_getreservasi= Server.URL+"getpengajuanmejacustomer.php";
            RequestQueue queue=Volley.newRequestQueue(DaftarPengajuanMejaActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url_getreservasi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    reservasiList.clear();

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        JSONArray jar=object.getJSONArray("data");

                        if(jar.length()>0)
                        {
                            JSONObject jsonObject;

                            for(int i=0;i<jar.length();i++)
                            {
                                jsonObject=jar.getJSONObject(i);
                                Reservasi reservasi=new Reservasi();
                                reservasi.setNomeja(jsonObject.getString("nomeja"));
                                reservasi.setUsername(jsonObject.getString("username"));
                                reservasi.setTgl_sewa(jsonObject.getString("tglsewa"));
                                reservasi.setId_reservasi(jsonObject.getString("idreservasi"));
                                reservasiList.add(reservasi);
                            }

                            jar=object.getJSONArray("totalpesanan");
                            jsonObject=jar.getJSONObject(0);
                            total=jsonObject.getInt("total");
                            lbltotal.setText(NumberFormat.getCurrencyInstance().format(total));


                        }

                        daftarreservasiadapter=new DaftarPengajuanMejaAdapter(reservasiList, DaftarPengajuanMejaActivity.this,2);
                        rvdaftarreservasi.setAdapter(daftarreservasiadapter);
                        daftarreservasiadapter.notifyDataSetChanged();

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DaftarPengajuanMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DaftarPengajuanMejaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params=new HashMap<>();
                    SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(DaftarPengajuanMejaActivity.this);
                    String iduser=pref.getString("id",null);
                    params.put("iduser",iduser);

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



    public void searchMeja(final String nomeja)
    {
        try
        {
            String url_carimeja=Server.URL+"carireservasicustomer.php";
            RequestQueue queue=Volley.newRequestQueue(DaftarPengajuanMejaActivity.this);

            StringRequest request=new StringRequest(Request.Method.POST, url_carimeja, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    reservasiList.clear();

                    try
                    {
                          JSONObject object=new JSONObject(response);
                          JSONArray jsonArray=object.getJSONArray("data");

                          if(jsonArray.length()>0)
                          {
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject obj=jsonArray.getJSONObject(i);

                                    Reservasi reservasi=new Reservasi();
                                    reservasi.setId_reservasi(obj.getString("idreservasi"));
                                    reservasi.setUsername(obj.getString("username"));
                                    reservasi.setNomeja(obj.getString("nomeja"));
                                    reservasi.setTgl_sewa(obj.getString("tglsewa"));

                                    reservasiList.add(reservasi);

                                }

                                daftarreservasiadapter=new DaftarPengajuanMejaAdapter(reservasiList, DaftarPengajuanMejaActivity.this,2);
                                rvdaftarreservasi.setAdapter(daftarreservasiadapter);
                                daftarreservasiadapter.notifyDataSetChanged();
                          }

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(DaftarPengajuanMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DaftarPengajuanMejaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(DaftarPengajuanMejaActivity.this);
                    String iduser=preferences.getString("id",null);
                    params.put("nomeja",nomeja);
                    params.put("iduser",iduser);

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



    private void getStrukMeja(ArrayList<Reservasi>reservasiList)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String html=" <!DOCTYPE html>\n" +
                "<html>\n" +

                "<head>\n"+

                "<style>\n"+

                    "table{width: 100%; border-collapse:collapse}\n"+
                    "th{text-align:center}\n"+
                    "td{text-align:center;padding:10px;}\n"+

                "</style>\n"+

                "</head>\n"+

                "<body>\n"+

                "<table border='1'>\n"+

                "<tr>\n"+
                    "<th> Id Reservasi </trh> \n"+
                    "<th> Penyewa </th>\n"+
                    "<th> No Meja </th>\n"+
                    "<th> Tanggal Sewa </th>\n"+
                "</tr>\n";

        for(int i=0; i < reservasiList.size(); i ++)
        {
            try
            {
                Date dateTglSewa=sdf.parse(reservasiList.get(i).getTgl_sewa());

                sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                html+="<tr>\n"+
                            "<td>"+reservasiList.get(i).getId_reservasi()+"</td>\n"+
                            "<td>"+reservasiList.get(i).getUsername()+"</td>\n"+
                            "<td>"+reservasiList.get(i).getNomeja()+"</td>\n"+
                            "<td>"+sdf.format(dateTglSewa)+"</td>\n"+
                        "</tr>\n";
            }

            catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        html+="</body>"+
                "</html>";

        FileManager.getInstance().cleanTempFolder(getApplicationContext());
        final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);

        PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {
                Uri pdfUri = Uri.fromFile(savedPDFFile);

                Intent intentPdfViewer = new Intent(getApplicationContext(), PdfViewerActivity.class);
                intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                startActivity(intentPdfViewer);
            }

            @Override
            public void onError(Exception exception) {

                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }




    private void getStrukMenu()
    {
        try
        {

            String url=Server.URL+"printstrukmenu.php";
            RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            for(int i=0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonArrayObject= jsonArray.getJSONObject(i);

                                StrukMenu strukMenu=new StrukMenu();
                                strukMenu.setNo_meja(jsonArrayObject.getString("no_meja"));
                                strukMenu.setNama(jsonArrayObject.getString("nama"));
                                strukMenu.setHarga(jsonArrayObject.getInt("harga"));
                                strukMenu.setJumlah(jsonArrayObject.getInt("jumlah"));
                                strukMenu.setSubtotal(jsonArrayObject.getDouble("subtotal"));
                                strukMenu.setTgl_sewa(jsonArrayObject.getString("tgl_sewa"));

                                strukList.add(strukMenu);

                            }


                            String html =
                                    "<!DOCTYPE html>\n" +
                                            "<html>\n" +

                                            "<head>\n" +

                                                "<style>\n" +

                                                    "table{width: 100%; border-collapse:collapse}\n" +
                                                    "th{text-align:center}\n" +
                                                    "td{text-align:center;padding:10px;}\n" +

                                                "</style>\n" +

                                            "</head>\n" +

                                            "<body>\n" +

                                                "<table border='1'>\n" +

                                                "<tr>\n" +
                                                    "<th> No Meja </th> \n"+
                                                    "<th> Nama Menu </trh> \n" +
                                                    "<th> Jumlah </th>\n" +
                                                    "<th> Harga </th>\n" +
                                                    "<th> Subtotal </th>\n" +
                                                    "<th> Tgl Sewa </th>\n" +
                                                "</tr>\n";

                            for (int i = 0; i < strukList.size(); i++)
                            {

                                    try {

                                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date tglsewa=sdf.parse(strukList.get(i).getTgl_sewa());
                                        sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                                        html += "<tr>\n" +
                                                "<td>" + strukList.get(i).getNo_meja() + "</td>\n" +
                                                "<td>" + strukList.get(i).getNama() + "</td>\n" +
                                                "<td>" + strukList.get(i).getJumlah() + "</td>\n" +
                                                "<td>" + NumberFormat.getCurrencyInstance().format(strukList.get(i).getHarga()) + "</td>\n" +
                                                "<td>" + NumberFormat.getCurrencyInstance().format(strukList.get(i).getSubtotal()) + "</td>\n" +
                                                "<td>" + sdf.format(tglsewa) + "</td>\n" +
                                                "</tr>\n";
                                    }

                                    catch (Exception e)
                                    {
                                        Toast.makeText(DaftarPengajuanMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                            }

                            html += "</body>" +
                                    "</html>";

                            FileManager.getInstance().cleanTempFolder(getApplicationContext());
                            final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);

                            PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
                                @Override
                                public void onSuccess(File file) {
                                    Uri pdfUri = Uri.fromFile(savedPDFFile);

                                    Intent intentPdfViewer = new Intent(getApplicationContext(), PdfViewerActivity.class);
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

                    catch (JSONException e)
                    {
                        Toast.makeText(DaftarPengajuanMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date tglSewa;
                    int total=0;



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(DaftarPengajuanMejaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();

                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String id=preferences.getString("id",null);

                    params.put("iduser",id);

                    return  params;
                }
            };

            queue.add(stringRequest);

        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cetak_struk_menu,menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id)
        {
            case R.id.cetakstrukmeja:
                getStrukMeja(reservasiList);
                break;

            case R.id.cetakstrukmenu:
                getStrukMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
