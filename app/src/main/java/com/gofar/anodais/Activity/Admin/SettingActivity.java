package com.gofar.anodais.Activity.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.gofar.anodais.Model.StatusToko;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    Button btnaddadmin,btnchangepassword,btnchangeusername,btntutuptoko;
    EditText txteditusername,txteditpassword,txtconfirmpassword,txtnewuseradmin,txtnewpasswordadmin,txtconfirmneweditpasswordadmin;
    TextView lblstatustoko;
    int state=0;
    public boolean ok;
    TextView lblcek;
    ArrayList<StatusToko>statusList=new ArrayList<>();
    StatusToko statusToko=new StatusToko();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cekStatusToko();

        btnaddadmin=findViewById(R.id.btntambahadmin);
        btnchangepassword=findViewById(R.id.btneditpasswordadmin);
        btnchangeusername=findViewById(R.id.btneditusernameadmin);
        btntutuptoko=findViewById(R.id.btntutuptoko);

        txteditusername=findViewById(R.id.txteditusernameadmin);
        txteditpassword=findViewById(R.id.txteditpassaddmin);
        txtconfirmneweditpasswordadmin=findViewById(R.id.txtconfirmeditpassadmin);

        txtnewuseradmin=findViewById(R.id.txtnewuseradmin);
        txtnewpasswordadmin=findViewById(R.id.txtnewpasswordadmin);
        txtconfirmpassword=findViewById(R.id.txtnewconfirmpasswordadmin);

        lblstatustoko=findViewById(R.id.lblstatustoko);

        btntutuptoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahStatusToko();
            }
        });


        btnchangeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txteditusername.getText().toString().trim().equals(""))
                {
                    Toast.makeText(SettingActivity.this, "Silahkan isi username yang mau diubah", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    state=1;
                    confirmPassDialog();
                }

            }
        });

        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editpasswordadminbaru=txteditpassword.getText().toString().trim();
                String confirmeditpasswordadminbaru=txtconfirmneweditpasswordadmin.getText().toString();

                if(editpasswordadminbaru.equals("")||confirmeditpasswordadminbaru.equals(""))
                {
                    Toast.makeText(SettingActivity.this, "Lengkapi kolom ganti password yang masih kosong", Toast.LENGTH_SHORT).show();
                }

                else if(!editpasswordadminbaru.equals(confirmeditpasswordadminbaru))
                {
                    Toast.makeText(SettingActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    state=2;
                    confirmPassDialog();
                }

            }
        });


        btnaddadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newadminusername=txtnewuseradmin.getText().toString().trim();
                String newadminpassword=txtnewpasswordadmin.getText().toString().trim();
                String confirmnewadminpassword=txtconfirmpassword.getText().toString().trim();

                if(newadminusername.equals("")||newadminpassword.equals("")||confirmnewadminpassword.equals(""))
                {
                    Toast.makeText(SettingActivity.this, "Silahkan lengkapi kolom penambahan admin baru yang masih kosong", Toast.LENGTH_SHORT).show();
                }

                else if(!newadminpassword.equals(confirmnewadminpassword))
                {
                    Toast.makeText(SettingActivity.this, "Kedua password penambahan admin tidak cocok", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    state=3;
                    confirmPassDialog();
                }
            }
        });

    }


    public void confirmPassDialog()
    {
        final AlertDialog.Builder alert=new AlertDialog.Builder(SettingActivity.this);
        LayoutInflater inflater=getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.popupauthpassword,null);
        final AlertDialog dialog=alert.create();
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final TextView txtkonfirmasipasword=dialogView.findViewById(R.id.txtconfirmchange);
        Button btnkonfirmasipassword=dialogView.findViewById(R.id.btnconfirmchange);

        btnkonfirmasipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
                String username = prefs.getString("Username_logged_in", null);


                String password=txtkonfirmasipasword.getText().toString().trim();

                if(password.equals(""))
                {
                    Toast.makeText(SettingActivity.this, "Silahkan isi password anda", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    authpassword(username,password);
                    dialog.dismiss();
                }


            }
        });

        dialog.show();
    }


    public boolean authpassword(final String email, final String password)
    {

        String url_cek=Server.URL+"login.php";
        RequestQueue queue=Volley.newRequestQueue(this);

        StringRequest streq=new StringRequest(Request.Method.POST,url_cek,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject obj=new JSONObject(response);
                    ok=true;

                    if(obj.getString("success").equals("1"))
                    {
                        switch (state)
                        {
                            case 1:
                                changeadminuser();
                                break;

                            case 2:
                                changeadminpassword();
                                break;

                            case 3:
                                addadmin();
                                break;

                            default:
                                Toast.makeText(SettingActivity.this, "State is empty", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else
                    {
                        Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }

                catch (JSONException e)
                {
                    Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                catch (Exception e)
                {
                    Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);

                return params;

            }

        };

        queue.add(streq);

        return ok;

    }



    public void changeadminuser()
    {

        try
        {
            String url_change=Server.URL+"updateuser.php";
            final RequestQueue queue=Volley.newRequestQueue(SettingActivity.this);

            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String id=prefs.getString("id",null);

            StringRequest req=new StringRequest(Request.Method.POST, url_change, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject obj=new JSONObject(response);

                        int success;

                        success = obj.getInt("success");

                        if(success==1)
                        {
                            SharedPreferences.Editor prefeditor=prefs.edit();
                            prefeditor.putString("Username_logged_in",txteditusername.getText().toString().trim());
                            prefeditor.commit();
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            txteditusername.setText("");
                            queue.stop();
                        }

                        else if(success==0)
                        {
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch(Exception e)
                    {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("email",txteditusername.getText().toString().trim());
                    params.put("id",id);
                    return params;
                }
            };

            queue.add(req);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    public void changeadminpassword()
    {
        try
        {
            String urlgantipass=Server.URL+"updatepassworduser.php";
            RequestQueue requestQueue=Volley.newRequestQueue(SettingActivity.this);
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            final String id = prefs.getString("id", null);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, urlgantipass, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject obj=new JSONObject(response);
                        int success=obj.getInt("success");

                        if(success==1)
                        {
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            txteditpassword.setText("");
                            txtconfirmpassword.setText("");
                        }

                        else
                        {
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    String passwordbaru=txteditpassword.getText().toString();
                    params.put("id",id);
                    params.put("password",passwordbaru);

                    return params;
                }
            };

            requestQueue.add(stringRequest);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void addadmin()
    {
        String urladd=Server.URL+"register.php";
        RequestQueue requestQueue=Volley.newRequestQueue(SettingActivity.this);

        try
        {
            StringRequest request=new StringRequest(Request.Method.POST, urladd, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject obj=new JSONObject(response);

                        if(obj.getInt("success")==1)
                        {
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            txtnewuseradmin.setText("");
                            txtnewpasswordadmin.setText("");
                            txtconfirmneweditpasswordadmin.setText("");
                        }

                        else
                        {
                            Toast.makeText(SettingActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();

                    String newadminemail=txtnewuseradmin.getText().toString().trim();
                    String newadminpassword=txtnewpasswordadmin.getText().toString().trim();

                    params.put("email",newadminemail);
                    params.put("username","(NULL)");
                    params.put("notelp","(NULL)");
                    params.put("password",newadminpassword);
                    params.put("role","admin");

                    return params;
                }
            };

            requestQueue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void cekStatusToko()
    {
        try
        {
            String url=Server.URL+"cekstatustoko.php";
            RequestQueue queue=Volley.newRequestQueue(SettingActivity.this);
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try
                    {
                        JSONObject jsonObject=response.getJSONObject(0);

                        String status =jsonObject.getString("status").equals("Buka") ? "Buka" : "Tutup";

                        StatusToko statusToko=new StatusToko();
                        statusToko.setStatus(status);
                        statusList.add(statusToko);

                        lblstatustoko.setText(statusList.get(0).getStatus());

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonArrayRequest);
        }

        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void ubahStatusToko()
    {
        try
        {
            String url=Server.URL+"ubahstatustoko.php";
            RequestQueue queue=Volley.newRequestQueue(SettingActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            lblstatustoko.setText(statusList.get(0).getStatus());
                            Toast.makeText(SettingActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(SettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SettingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params=new HashMap<>();

                    String setStatusToko=statusList.get(0).getStatus().equals("Buka") ? "Tutup" : "Buka";

                    params.put("status",setStatusToko);

                    statusToko.setStatus(setStatusToko);

                    statusList.set(0, statusToko);

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
