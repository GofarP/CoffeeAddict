package com.gofar.anodais.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.gofar.anodais.Model.Menu;

import android.print.PDFPrint;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Model.Transaksi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    Button btncetakmenu, btncetaktransaksi;
    RadioButton rbcetakmenuid,rbcetakmenusemua,rbcetaktransaksisemua,rbcetaktransaksiid,rbcetaktransaksibulanan;
    RadioGroup rgtransaksi;
    EditText txtidMenu,txtidtransaksi;
    Spinner spnbulan;
    int totalTransaksi=0,makanan=0,minuman=0;
    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    ArrayList<Menu> menuList = new ArrayList<Menu>();
    ArrayList<Transaksi> transaksiList=new ArrayList<Transaksi>();

    Date tgl=new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btncetakmenu=findViewById(R.id.btncetakreportmenu);
        btncetaktransaksi=findViewById(R.id.btncetakreporttransaksi);
        rbcetakmenuid=findViewById(R.id.rbcetakmenuid);
        rbcetakmenusemua=findViewById(R.id.rbcetakmenusemua);

        rbcetaktransaksiid=findViewById(R.id.rbcetaktransaksiid);
        rbcetaktransaksisemua=findViewById(R.id.rbcetaktransaksisemua);
        rbcetaktransaksibulanan=findViewById(R.id.rbcetaktransaksibulan);

        rgtransaksi=findViewById(R.id.rgreporttransaksi);

        txtidMenu=findViewById(R.id.txtreportidmenu);
        txtidtransaksi=findViewById(R.id.txtreportidtransaksi);

        spnbulan=findViewById(R.id.spnbulan);

        ArrayList<String>listBulan=new ArrayList<>();

        for(int i=0; i< 12; i++)
        {
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("MMMM");
            calendar.set(Calendar.MONTH, i);
            String namaBulan=sdf.format(calendar.getTime());

            listBulan.add(namaBulan);
        }

        ArrayAdapter<String> bulanAdapter=new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, listBulan);

        spnbulan.setAdapter(bulanAdapter);

        btncetakmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rbcetakmenusemua.isChecked())
                {
                    cetakMenuSemua();
                }

                else if(rbcetakmenuid.isChecked())
                {
                    cetakMenuID();
                }


            }
        });


        btncetaktransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(rbcetaktransaksisemua.isChecked())
              {
                  cetakSemuaTransaksi();
              }

              else if(rbcetaktransaksiid.isChecked())
              {
                  cetakTransaksiID();
              }

              else if(rbcetaktransaksibulanan.isChecked())
              {
                  cetakTransaksiBulanan();
              }

            }
        });


    }




    public void cetakMenuSemua()
    {
        String url= Server.URL+"/report/allmenu.php";
        RequestQueue queue= Volley.newRequestQueue(ReportActivity.this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                menuList.clear();

                for(int i=0; i< response.length(); i++)
                {

                    try
                    {
                        JSONObject object=response.getJSONObject(i);
                        Menu menu=new Menu();
                        menu.setId(object.getString("id_menu"));
                        menu.setNama(object.getString("nama"));
                        menu.setJenis(object.getString("jenis"));
                        menu.setHarga(object.getDouble("harga"));
                        menu.setStatus(object.getString("status"));

                        menuList.add(menu);

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                String html="<!DOCTYPE html>\n"+
                                "<html>\n"+
                                    "<head>\n"+
                                        "<meta charset='utf-8'\n>"+

                                        "<style>\n"+
                                           " .footer {\n"+
                                                    "position: fixed;\n"+
                                                    "left: 0;\n"+
                                                    "bottom: 0;\n"+
                                                    "width: 100%;\n"+
                                                    "color: black;\n"+
                                                "}\n"+
                                        "</style>\n"+

                                    "</head>\n"+

                                "<body>\n"+

                                  "<table style='width:100%'>\n"+
                                        "<tr>\n"+
                                            "<td rowspan='3' style='height:150px;width:150px'>\n"+
                                                "<img src='https://i.postimg.cc/BZy2S5NW/index.jpg' style='display:block' width='100%'>\n"+
                                            "</td>\n"+

                                            "<td>\n"+
                                                "<h1 style='text-align:center'>Coffee Addict</h2>\n"+
                                            "</td>\n"+

                                            "<td style='text-align:right' width='20%'>\n"+
                                                "<h4>Laporan Menu</p>\n"+
                                                "<p>Tanggal: <br>\n"+
                                                sdf.format(tgl)+"</p>\n"+
                                            "</td>\n"+
                                        "</tr>\n"+
                                  "</table>\n"+


                                "<table style='width: 100%;margin-top:30px;border-color:black;border-collapse:collapse' border='1px solid'>\n"+

                                    "<thead>\n"+

                                        "<tr>\n"+
                                            "<th>ID</th>\n"+
                                            "<th>Nama Menu</th>\n"+
                                            "<th>jenis</th>\n"+
                                            "<th>Harga</th>\n"+
                                            "<th>Status</th>\n"+
                                        "</tr>\n"+

                                    "</thead>\n"+

                                    "<tbody>\n"+
                                        "<tr>\n";

                                        for(int i=0;i<menuList.size();i++)
                                        {
                                            html+="<tr>\n"+
                                                     "<td>"+menuList.get(i).getId()+"</td>\n"+
                                                     "<td>"+menuList.get(i).getNama()+"</td>\n"+
                                                     "<td>"+menuList.get(i).getJenis()+"</td>\n"+
                                                     "<td>"+NumberFormat.getCurrencyInstance().format(menuList.get(i).getHarga())+"</td>\n"+
                                                    "<td>"+menuList.get(i).getJenis()+"</td>\n"+
                                                    "</tr>\n";

                                                    if(menuList.get(i).getJenis().equals("Makanan"))
                                                    {
                                                        makanan+=1;
                                                    }

                                                    else
                                                    {
                                                        minuman+=1;
                                                    }
                                        }

                                       html+= "</tr>\n"+
                                    "</tbody>\n"+
                                "</table>\n"+


                                "<div class='footer'>\n"+

                                    "<div style='width: 100%; display: table;'>\n"+

                                        "<div style='display: table-row; height: 100px;'>\n"+

                                            "<div style='width: 50%; display: table-cell;'>\n"+
                                                "<h3 style='margin-left:20px'>Jumlah Menu</h3>\n"+
                                                "<p style='margin-left:20px'>Makanan:"+makanan+"</p>\n"+
                                                "<p style='margin-left:20px'>Minuman:"+minuman+"</p>\n"+
                                            "</div>\n"+

                                            "<div style='display: table-cell;'>\n"+
                                                "<p style='text-align:right;margin-right:20%'>Tertanda </p>\n"+
                                               "<br>\n"+
                                                "<p style='text-align:right;margin-right:21%'>Pemilik</p>\n"+
                                            "</div>\n"+

                                        "</div>\n"+
                                    "</div>\n"+

                                "</div>\n"+
                            "</body>\n"+
                        "</html>";



                FileManager.getInstance().cleanTempFolder(ReportActivity.this);

                final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                PDFUtil.generatePDFFromHTML(ReportActivity.this, savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
                    @Override
                    public void onSuccess(File file) {
                        Uri pdfUri = Uri.fromFile(savedPDFFile);

                        Intent intentPdfViewer = new Intent(ReportActivity.this, PdfViewerActivity.class);
                        intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                        startActivity(intentPdfViewer);

                        makanan=0;
                        minuman=0;
                    }

                    @Override
                    public void onError(Exception exception) {

                        Toast.makeText(ReportActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);

    }


    private void cetakMenuID()
    {
        String url=Server.URL+"report/getmenubyid.php";
        RequestQueue requestQueue= Volley.newRequestQueue(ReportActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {

                        menuList = new ArrayList<>();
                        JSONObject obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("data");

                        if (jsonArray.length() > 0) {

                            menuList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Menu menu = new Menu();
                                    menu.setId(object.getString("id_menu"));
                                    menu.setNama(object.getString("nama"));
                                    menu.setJenis(object.getString("jenis"));
                                    menu.setHarga(object.getDouble("harga"));
                                    menu.setStatus(object.getString("status"));

                                    menuList.add(menu);

                                } catch (JSONException e) {
                                    Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            String html="<!DOCTYPE html>\n"+
                                    "<html>\n"+
                                    "<head>\n"+
                                    "<meta charset='utf-8'\n>"+

                                    "<style>\n"+
                                    " .footer {\n"+
                                    "position: fixed;\n"+
                                    "left: 0;\n"+
                                    "bottom: 0;\n"+
                                    "width: 100%;\n"+
                                    "color: black;\n"+
                                    "}\n"+
                                    "</style>\n"+

                                    "</head>\n"+

                                    "<body>\n"+

                                    "<table style='width:100%'>\n"+
                                    "<tr>\n"+
                                    "<td rowspan='3' style='height:150px;width:150px'>\n"+
                                    "<img src='https://i.postimg.cc/BZy2S5NW/index.jpg' style='display:block' width='100%'>\n"+
                                    "</td>\n"+

                                    "<td>\n"+
                                    "<h1 style='text-align:center'>Coffee Addict</h2>\n"+
                                    "</td>\n"+

                                    "<td style='text-align:right' width='20%'>\n"+
                                    "<h4>Laporan Menu</p>\n"+
                                    "<p>Tanggal: <br>\n"+
                                    sdf.format(tgl)+"</p>\n"+
                                    "</td>\n"+
                                    "</tr>\n"+
                                    "</table>\n"+


                                    "<table style='width: 100%;margin-top:30px;border-color:black;border-collapse:collapse' border='1px solid'>\n"+

                                    "<thead>\n"+

                                    "<tr>\n"+
                                    "<th>ID</th>\n"+
                                    "<th>Nama Menu</th>\n"+
                                    "<th>jenis</th>\n"+
                                    "<th>Harga</th>\n"+
                                    "<th>Status</th>\n"+
                                    "</tr>\n"+

                                    "</thead>\n"+

                                    "<tbody>\n"+
                                    "<tr>\n";

                            for(int i=0;i<menuList.size();i++)
                            {
                                html+="<tr>\n"+
                                        "<td>"+menuList.get(i).getId()+"</td>\n"+
                                        "<td>"+menuList.get(i).getNama()+"</td>\n"+
                                        "<td>"+menuList.get(i).getJenis()+"</td>\n"+
                                        "<td>"+NumberFormat.getCurrencyInstance().format(menuList.get(i).getHarga())+"</td>\n"+
                                        "<td>"+menuList.get(i).getJenis()+"</td>\n"+
                                        "</tr>\n";


                                        if(menuList.get(i).getJenis().equals("Makanan"))
                                        {
                                            makanan+=1;
                                        }

                                        else
                                        {
                                            minuman+=1;
                                        }
                            }

                            html+= "</tr>\n"+
                                    "</tbody>\n"+
                                    "</table>\n"+


                                    "<div class='footer'>\n"+

                                    "<div style='width: 100%; display: table;'>\n"+

                                    "<div style='display: table-row; height: 100px;'>\n"+

                                    "<div style='width: 50%; display: table-cell;'>\n"+
                                    "<h3 style='margin-left:20px'>Jumlah Menu</h3>\n"+
                                    "<p style='margin-left:20px'>Makanan:"+makanan+"</p>\n"+
                                    "<p style='margin-left:20px'>Minuman:"+minuman+"</p>\n"+
                                    "</div>\n"+

                                    "<div style='display: table-cell;'>\n"+
                                    "<p style='text-align:right;margin-right:20%'>Tertanda </p>\n"+
                                    "<br>\n"+
                                    "<p style='text-align:right;margin-right:21%'>Pemilik</p>\n"+
                                    "</div>\n"+

                                    "</div>\n"+
                                    "</div>\n"+

                                    "</div>\n"+
                                    "</body>\n"+
                                    "</html>";

                            FileManager.getInstance().cleanTempFolder(ReportActivity.this);

                            final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                            PDFUtil.generatePDFFromHTML(ReportActivity.this, savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
                                @Override
                                public void onSuccess(File file) {
                                    Uri pdfUri = Uri.fromFile(savedPDFFile);

                                    Intent intentPdfViewer = new Intent(ReportActivity.this, PdfViewerActivity.class);
                                    intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                                    startActivity(intentPdfViewer);

                                    makanan=0;
                                    minuman=0;
                                }

                                @Override
                                public void onError(Exception exception) {

                                    Toast.makeText(ReportActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }


                }

                catch (JSONException e)
                {
                    Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("idmenu",txtidMenu.getText().toString().trim());

                return params;

            }
        };

        requestQueue.add(stringRequest);
    }



    private void cetakSemuaTransaksi()
    {
        try
        {
            String url=Server.URL+"/report/alltransaksi.php";
            RequestQueue queue=Volley.newRequestQueue(ReportActivity.this);
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    transaksiList.clear();
                    totalTransaksi=0;

                    for(int i=0; i< response.length(); i++)
                    {

                        try
                        {
                            JSONObject object=response.getJSONObject(i);

                            Transaksi transaksi=new Transaksi();
                            transaksi.setIdTransaksi(object.getString("id_transaksi"));
                            transaksi.setNamaPelanggan(object.getString("username"));
                            transaksi.setJenis(object.getString("jenis"));
                            transaksi.setTotal(object.getDouble("total"));
                            transaksi.setTanggal(object.getString("tanggal_transaksi"));

                            totalTransaksi+=object.getDouble("total");

                            transaksiList.add(transaksi);

                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    String html="<!DOCTYPE html>\n"+
                            "<html>\n"+
                            "<head>\n"+
                            "<meta charset='utf-8'\n>"+

                            "<style>\n"+
                            " .footer {\n"+
                            "position: fixed;\n"+
                            "left: 0;\n"+
                            "bottom: 0;\n"+
                            "width: 100%;\n"+
                            "color: black;\n"+
                            "}\n"+
                            "</style>\n"+

                            "</head>\n"+

                            "<body>\n"+

                            "<table style='width:100%'>\n"+
                            "<tr>\n"+
                            "<td rowspan='3' style='height:150px;width:150px'>\n"+
                            "<img src='https://i.postimg.cc/BZy2S5NW/index.jpg' style='display:block' width='100%'>\n"+
                            "</td>\n"+

                            "<td>\n"+
                            "<h1 style='text-align:center'>Coffee Addict</h2>\n"+
                            "</td>\n"+

                            "<td style='text-align:right' width='20%'>\n"+
                            "<h4>Laporan Menu</p>\n"+
                            "<p>Tanggal: <br>\n"+
                            sdf.format(tgl)+"</p>\n"+
                            "</td>\n"+
                            "</tr>\n"+
                            "</table>\n"+


                            "<table style='width: 100%;margin-top:30px;border-color:black;border-collapse:collapse' border='1px solid'>\n"+

                            "<thead>\n"+

                            "<tr>\n"+
                            "<th>ID</th>\n"+
                            "<th>Nama Menu</th>\n"+
                            "<th>jenis</th>\n"+
                            "<th>Harga</th>\n"+
                            "<th>Status</th>\n"+
                            "</tr>\n"+

                            "</thead>\n"+

                            "<tbody>\n"+
                            "<tr>\n";


                    for(int i=0;i<transaksiList.size();i++)
                    {
                        try
                        {
                            sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            Date tglTransaksi=sdf.parse(transaksiList.get(i).getTanggal());
                            sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                            html+="<tr>\n"+
                                    "<td>"+transaksiList.get(i).getIdTransaksi()+"</td>\n"+
                                    "<td>"+transaksiList.get(i).getNamaPelanggan()+"</td>\n"+
                                    "<td>"+transaksiList.get(i).getJenis()+"</td>\n"+
                                    "<td>"+ NumberFormat.getCurrencyInstance().format(transaksiList.get(i).getTotal())+"</td>\n"+
                                    "<td>"+sdf.format(tglTransaksi)+"</td>\n"+
                                    "</tr>\n";
                        }

                        catch (Exception e)
                        {
                            Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    html+= "</tr>\n"+
                            "</tbody>\n"+
                            "</table>\n"+


                            "<div class='footer'>\n"+

                            "<div style='width: 100%; display: table;'>\n"+

                            "<div style='display: table-row; height: 100px;'>\n"+

                            "<div style='width: 50%; display: table-cell;'>\n"+
                            "</div>\n"+

                            "<div style='display: table-cell;'>\n"+
                            "<p style='text-align:right;margin-right:20%'>Tertanda </p>\n"+
                            "<br>\n"+
                            "<p style='text-align:right;margin-right:21%'>Pemilik</p>\n"+
                            "</div>\n"+

                            "</div>\n"+
                            "</div>\n"+

                            "</div>\n"+
                            "</body>\n"+
                            "</html>";

                    FileManager.getInstance().cleanTempFolder(ReportActivity.this);

                    final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                    PDFUtil.generatePDFFromHTML(ReportActivity.this, savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
                        @Override
                        public void onSuccess(File file) {
                            Uri pdfUri = Uri.fromFile(savedPDFFile);

                            Intent intentPdfViewer = new Intent(ReportActivity.this, PdfViewerActivity.class);
                            intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                            startActivity(intentPdfViewer);
                        }

                        @Override
                        public void onError(Exception exception) {

                            Toast.makeText(ReportActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonArrayRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void cetakTransaksiBulanan()
    {
        try
        {
            String url=Server.URL+"/report/transaksibulanan.php";
            RequestQueue queue=Volley.newRequestQueue(ReportActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        transaksiList.clear();

                        totalTransaksi=0;

                        JSONObject obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("data");

                        for(int i=0; i< jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            Transaksi transaksi=new Transaksi();
                            transaksi.setIdTransaksi(jsonObject.getString("id_transaksi"));
                            transaksi.setNamaPelanggan(jsonObject.getString("nama"));
                            transaksi.setJenis(jsonObject.getString("jenis"));
                            transaksi.setTotal(jsonObject.getDouble("total"));
                            transaksi.setTanggal(jsonObject.getString("tanggal_transaksi"));

                            transaksiList.add(transaksi);

                            totalTransaksi+=jsonObject.getDouble("total");
                        }

                        String html="<!DOCTYPE html>\n"+
                                "<html>\n"+
                                "<head>\n"+
                                "<meta charset='utf-8'\n>"+

                                "<style>\n"+
                                " .footer {\n"+
                                "position: fixed;\n"+
                                "left: 0;\n"+
                                "bottom: 0;\n"+
                                "width: 100%;\n"+
                                "color: black;\n"+
                                "}\n"+
                                "</style>\n"+

                                "</head>\n"+

                                "<body>\n"+

                                "<table style='width:100%'>\n"+
                                "<tr>\n"+
                                "<td rowspan='3' style='height:150px;width:150px'>\n"+
                                "<img src='https://i.postimg.cc/BZy2S5NW/index.jpg' style='display:block' width='100%'>\n"+
                                "</td>\n"+

                                "<td>\n"+
                                "<h1 style='text-align:center'>Coffee Addict</h2>\n"+
                                "</td>\n"+

                                "<td style='text-align:right' width='20%'>\n"+
                                "<h4>Laporan Menu</p>\n"+
                                "<p>Tanggal: <br>\n"+
                                sdf.format(tgl)+"</p>\n"+
                                "</td>\n"+
                                "</tr>\n"+
                                "</table>\n"+


                                "<table style='width: 100%;margin-top:30px;border-color:black;border-collapse:collapse' border='1px solid'>\n"+

                                "<thead>\n"+

                                "<tr>\n"+
                                "<th>ID</th>\n"+
                                "<th>Nama Menu</th>\n"+
                                "<th>jenis</th>\n"+
                                "<th>Harga</th>\n"+
                                "<th>Status</th>\n"+
                                "</tr>\n"+

                                "</thead>\n"+

                                "<tbody>\n"+
                                "<tr>\n";


                        for(int i=0;i<transaksiList.size();i++)
                        {
                            try
                            {
                                sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date tglTransaksi=sdf.parse(transaksiList.get(i).getTanggal());
                                sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                                html+="<tr>\n"+
                                        "<td>"+transaksiList.get(i).getIdTransaksi()+"</td>\n"+
                                        "<td>"+transaksiList.get(i).getNamaPelanggan()+"</td>\n"+
                                        "<td>"+transaksiList.get(i).getJenis()+"</td>\n"+
                                        "<td>"+ NumberFormat.getCurrencyInstance().format(transaksiList.get(i).getTotal())+"</td>\n"+
                                        "<td>"+sdf.format(tglTransaksi)+"</td>\n"+
                                        "</tr>\n";
                            }

                            catch (Exception e)
                            {
                                Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        html+= "</tr>\n"+
                                "</tbody>\n"+
                                "</table>\n"+


                                "<div class='footer'>\n"+

                                "<div style='width: 100%; display: table;'>\n"+

                                "<div style='display: table-row; height: 100px;'>\n"+

                                "<div style='width: 50%; display: table-cell;'>\n"+
                                "</div>\n"+

                                "<div style='display: table-cell;'>\n"+
                                "<p style='text-align:right;margin-right:20%'>Tertanda </p>\n"+
                                "<br>\n"+
                                "<p style='text-align:right;margin-right:21%'>Pemilik</p>\n"+
                                "</div>\n"+

                                "</div>\n"+
                                "</div>\n"+

                                "</div>\n"+
                                "</body>\n"+
                                "</html>";

                        FileManager.getInstance().cleanTempFolder(ReportActivity.this);

                        final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                        PDFUtil.generatePDFFromHTML(ReportActivity.this, savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
                            @Override
                            public void onSuccess(File file) {
                                Uri pdfUri = Uri.fromFile(savedPDFFile);

                                Intent intentPdfViewer = new Intent(ReportActivity.this, PdfViewerActivity.class);
                                intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                                startActivity(intentPdfViewer);
                            }

                            @Override
                            public void onError(Exception exception) {

                                Toast.makeText(ReportActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });



                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("bulan",String.valueOf(spnbulan.getSelectedItemPosition()+1));

                    return  params;
                }
            };

            queue.add(request);

        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void cetakTransaksiID()
    {
        try
        {
            String url=Server.URL+"/report/gettransaksibyid.php";
            RequestQueue queue=Volley.newRequestQueue(ReportActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("data");

                        for(int i=0; i< jsonArray.length(); i++ )
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            Transaksi transaksi=new Transaksi();

                            transaksi.setIdTransaksi(jsonObject.getString("id_transaksi"));
                            transaksi.setNamaPelanggan(jsonObject.getString("nama"));
                            transaksi.setJenis(jsonObject.getString("jenis"));
                            transaksi.setTotal(jsonObject.getDouble("total"));
                            transaksi.setTanggal(jsonObject.getString("tanggal_transaksi"));

                            transaksiList.add(transaksi);

                            totalTransaksi+=jsonObject.getDouble("total");
                        }

                        String html="<!DOCTYPE html>\n"+
                                "<html>\n"+
                                "<head>\n"+
                                "<meta charset='utf-8'\n>"+

                                "<style>\n"+
                                " .footer {\n"+
                                "position: fixed;\n"+
                                "left: 0;\n"+
                                "bottom: 0;\n"+
                                "width: 100%;\n"+
                                "color: black;\n"+
                                "}\n"+
                                "</style>\n"+

                                "</head>\n"+

                                "<body>\n"+

                                "<table style='width:100%'>\n"+
                                "<tr>\n"+
                                "<td rowspan='3' style='height:150px;width:150px'>\n"+
                                "<img src='https://i.postimg.cc/BZy2S5NW/index.jpg' style='display:block' width='100%'>\n"+
                                "</td>\n"+

                                "<td>\n"+
                                "<h1 style='text-align:center'>Coffee Addict</h2>\n"+
                                "</td>\n"+

                                "<td style='text-align:right' width='20%'>\n"+
                                "<h4>Laporan Menu</p>\n"+
                                "<p>Tanggal: <br>\n"+
                                sdf.format(tgl)+"</p>\n"+
                                "</td>\n"+
                                "</tr>\n"+
                                "</table>\n"+


                                "<table style='width: 100%;margin-top:30px;border-color:black;border-collapse:collapse' border='1px solid'>\n"+

                                "<thead>\n"+

                                "<tr>\n"+
                                "<th>ID</th>\n"+
                                "<th>Nama Menu</th>\n"+
                                "<th>jenis</th>\n"+
                                "<th>Harga</th>\n"+
                                "<th>Status</th>\n"+
                                "</tr>\n"+

                                "</thead>\n"+

                                "<tbody>\n"+
                                "<tr>\n";


                        for(int i=0;i<transaksiList.size();i++)
                        {
                            try
                            {
                                sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date tglTransaksi=sdf.parse(transaksiList.get(i).getTanggal());
                                sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                                html+="<tr>\n"+
                                        "<td>"+transaksiList.get(i).getIdTransaksi()+"</td>\n"+
                                        "<td>"+transaksiList.get(i).getNamaPelanggan()+"</td>\n"+
                                        "<td>"+transaksiList.get(i).getJenis()+"</td>\n"+
                                        "<td>"+ NumberFormat.getCurrencyInstance().format(transaksiList.get(i).getTotal())+"</td>\n"+
                                        "<td>"+sdf.format(tglTransaksi)+"</td>\n"+
                                        "</tr>\n";
                            }

                            catch (Exception e)
                            {
                                Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        html+= "</tr>\n"+
                                "</tbody>\n"+
                                "</table>\n"+


                                "<div class='footer'>\n"+

                                "<div style='width: 100%; display: table;'>\n"+

                                "<div style='display: table-row; height: 100px;'>\n"+

                                "<div style='width: 50%; display: table-cell;'>\n"+
                                "</div>\n"+

                                "<div style='display: table-cell;'>\n"+
                                "<p style='text-align:right;margin-right:20%'>Tertanda </p>\n"+
                                "<br>\n"+
                                "<p style='text-align:right;margin-right:21%'>Pemilik</p>\n"+
                                "</div>\n"+

                                "</div>\n"+
                                "</div>\n"+

                                "</div>\n"+
                                "</body>\n"+
                                "</html>";

                        FileManager.getInstance().cleanTempFolder(ReportActivity.this);

                        final File savedPDFFile = FileManager.getInstance().createTempFile(getApplicationContext(), "pdf", false);
                        PDFUtil.generatePDFFromHTML(ReportActivity.this, savedPDFFile, html, new PDFPrint.OnPDFPrintListener() {
                            @Override
                            public void onSuccess(File file) {
                                Uri pdfUri = Uri.fromFile(savedPDFFile);

                                Intent intentPdfViewer = new Intent(ReportActivity.this, PdfViewerActivity.class);
                                intentPdfViewer.putExtra(PdfViewerActivity.PDF_FILE_URI, pdfUri);

                                startActivity(intentPdfViewer);
                            }

                            @Override
                            public void onError(Exception exception) {

                                Toast.makeText(ReportActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });



                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(ReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params=new HashMap<>();
                    params.put("id",txtidtransaksi.getText().toString().trim());

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
}
