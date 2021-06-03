package com.gofar.anodais.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Activity.Admin.AdminActivity;
import com.gofar.anodais.Activity.Customer.CustomerActivity;
import com.gofar.anodais.Activity.Customer.RegisterActivity;
import com.gofar.anodais.R;
import com.gofar.anodais.Preference.Prefrences;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String role = prefs.getString("role", null);

        if (Prefrences.getLoggedInStatus(getBaseContext()) && role.equals("admin"))
        {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        }

        else if(Prefrences.getLoggedInStatus(getBaseContext())&& role.equals("customer"))
        {
            startActivity(new Intent(this, CustomerActivity.class));
            finish();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        final EditText txtemaillogin,txtpasswordlogin;
        Button btnlogin;
        TextView lblregister;

        txtemaillogin=findViewById(R.id.txtemaillogin);
        txtpasswordlogin=findViewById(R.id.txtpasswordlogin);
        btnlogin=findViewById(R.id.btnlogin);
        lblregister=findViewById(R.id.lblregister);
        final String tag_json_obj = "json_obj_req";

        lblregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=txtemaillogin.getText().toString().trim();
                final String password=txtpasswordlogin.getText().toString().trim();

                if(email.equals("")||password.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Silahkan isi dulu kolom yang sedang kosong", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    login(email,password);
                }

            }
        });


    }


    public void login(final String email, final String password)
    {
        try
        {
            String url_login = Server.URL + "login.php";
            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest strReq = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    String TAG = LoginActivity.class.getSimpleName();
                    String TAG_USERNAME = "username";
                    String TAG_EMAIL = "email";
                    String TAG_ROLE ="role";
                    String TAG_success="success";
                    String TAG_MESSAGE = "message";
                    String TAG_ID = "id";

                    Log.d(TAG, "Response: " + response.toString());

                    try
                    {
                        JSONObject jObj = new JSONObject(response);
                        int success;

                        success = jObj.getInt(TAG_success);

                        if (success == 1 && jObj.getString(TAG_ROLE).equals("admin"))
                        {
                            setPreferences(jObj.getString(TAG_EMAIL),jObj.getString(TAG_ROLE),jObj.getString(TAG_ID));
                            startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                            requestQueue.stop();
                        }

                        else if(success==1 && jObj.getString(TAG_ROLE).equals("customer"))
                        {
                            setPreferences(jObj.getString(TAG_EMAIL),jObj.getString(TAG_ROLE),jObj.getString(TAG_ID));
                            startActivity(new Intent(LoginActivity.this,CustomerActivity.class));
                            requestQueue.stop();
                        }

                        else
                        {
                            Toast.makeText(LoginActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            requestQueue.stop();
                        }


                    }

                    catch (JSONException e)
                    {
                        // JSON error
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        // JSON print consoloe
                        System.out.println(e.getMessage());
                    }

                }
            },new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams()
                {
                    // Posting parameters ke post url
                    Map<String, String> params = new HashMap<>();

                    params.put("email",email);
                    params.put("password", password);

                    return params;
                }

            };
            requestQueue.add(strReq);

        }


        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setPreferences(String username,String role,String id)
    {
        Prefrences.setLoggedInUser(getBaseContext(),username,role,id);
        Prefrences.setLoggedInStatus(getBaseContext(),true);
    }


}
