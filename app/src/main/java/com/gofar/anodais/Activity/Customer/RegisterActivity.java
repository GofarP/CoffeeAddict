package com.gofar.anodais.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofar.anodais.Activity.Login.LoginActivity;
import com.gofar.anodais.R;
import com.gofar.anodais.Preference.Prefrences;
import com.gofar.anodais.conn.Server;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btnregister;
    EditText txtusernameregister,txtpasswordregister,txtconfirmpasswordregister, txtemailregister, txtnotelpregister;
    TextView lbllogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtusernameregister=findViewById(R.id.txtusernameregister);
        txtpasswordregister=findViewById(R.id.txtpasswordregister);
        txtconfirmpasswordregister=findViewById(R.id.txtconfirmpasswordregister);
        btnregister=findViewById(R.id.btnregister);
        txtemailregister=findViewById(R.id.txtemailregister);
        txtnotelpregister=findViewById(R.id.txtnotelpregister);

        lbllogin=findViewById(R.id.lbllogin);

        getSupportActionBar().hide();

        lbllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=txtemailregister.getText().toString().trim();
                String username=txtusernameregister.getText().toString().trim();
                String password=txtpasswordregister.getText().toString().trim();
                String notelp=txtnotelpregister.getText().toString().trim();
                String confirm=txtconfirmpasswordregister.getText().toString().trim();

                if(validasi())
                {
                    Toast.makeText(RegisterActivity.this, "Silahkan isi kolom yang masih kosong", Toast.LENGTH_SHORT).show();
                }

                else if(!password.equals(confirm))
                {
                    Toast.makeText(RegisterActivity.this, "Kedua password tidak cocok silahkan cocokan lagi", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    register(email,username,notelp,password);
                }
            }
        });
    }


    public void register(final String email, final String username, final String notelp, final String password)
    {
        String url_register = Server.URL + "register.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest stRegister = new StringRequest(Request.Method.POST, url_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    String TAG = RegisterActivity.class.getSimpleName();
                    String TAG_SUCCESS="success";
                    String TAG_MESSAGE = "message";
                    String TAG_ID = "id";

                    JSONObject jObj = new JSONObject(response);
                    int success;

                    success = jObj.getInt(TAG_SUCCESS);

                    if(success==1)
                    {
                        String id=jObj.getString("id");

                        Prefrences.setLoggedInUser(getBaseContext(),Prefrences.getRegisteredUser(getBaseContext()),"customer",id);
                        Prefrences.setLoggedInStatus(getBaseContext(),true);

                        Toast.makeText(RegisterActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, CustomerActivity.class));
                        requestQueue.stop();

                        finish();
                    }

                    else if(success==2)
                    {
                        Toast.makeText(RegisterActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                        requestQueue.stop();
                    }

                    else
                    {
                        Toast.makeText(RegisterActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                        requestQueue.stop();
                    }
                }

                catch (Exception e)
                {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println( e.getMessage());
                    requestQueue.stop();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email",email);
                params.put("username",username);
                params.put("notelp",notelp);
                params.put("password", password);
                params.put("role", "customer");

                return params;
            }
        };

        requestQueue.add(stRegister);
    }


    public  boolean validasi()
    {
        EditText[] et={txtemailregister,txtusernameregister,txtnotelpregister,txtpasswordregister,txtconfirmpasswordregister};

        boolean ok=false;

        for(EditText editText:et)
        {
            if(editText.getText().toString().trim().equals(""))
            {
                ok=true;
            }
        }

        return ok;
    }
}
