package com.gofar.anodais.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pl.aprilapps.easyphotopicker.EasyImage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditMejaActivity extends AppCompatActivity {

    Button btneditnomeja;
    EditText txtnomeja,txtjenismeja;
    ImageView iveditnomeja;

    final int CODE_REQUEST_CAMERA=1;
    final int CODE_REQUEST_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meja);

        btneditnomeja=findViewById(R.id.btneditnomeja);
        txtnomeja=findViewById(R.id.txteditnomeja);
        txtjenismeja=findViewById(R.id.txteditjenismeja);
        iveditnomeja=findViewById(R.id.iveditnomeja);


        final Bundle bundle=getIntent().getExtras();

        txtnomeja.setText(bundle.getString("nomeja"));

        iveditnomeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpload();
            }
        });

        btneditnomeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtnomeja.getText().toString().equals(""))
                {
                    Toast.makeText(EditMejaActivity.this, "Silahkan isi no meja", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    String nomeja=txtnomeja.getText().toString().trim();
                    String idmeja=bundle.getString("idmeja");
                    String gambar=(iveditnomeja.getDrawable()==null)?"" : imageToString(((BitmapDrawable)iveditnomeja.getDrawable()).getBitmap());
                    String jenis=txtjenismeja.getText().toString();

                    updateNoMeja(idmeja,nomeja,gambar,jenis);
                }
            }
        });

    }

    public void updateNoMeja(final String idmeja, final String nomeja, final String gambar,final String jenis)
    {
        try
        {
            String url_editnomeja= Server.URL+"updatemeja.php";
            RequestQueue queue= Volley.newRequestQueue(EditMejaActivity.this);
            StringRequest request=new StringRequest(Request.Method.POST, url_editnomeja, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        JSONObject object=new JSONObject(response);

                        if(object.getString("success").equals("1"))
                        {
                            Toast.makeText(EditMejaActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(EditMejaActivity.this,ReservasiActivity.class);
                            startActivity(intent);
                        }

                        else
                        {
                            Toast.makeText(EditMejaActivity.this, object.getString("messages"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(EditMejaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditMejaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();

                    params.put("idmeja",idmeja);
                    params.put("nomeja",nomeja);
                    params.put("jenis",jenis);
                    params.put("images",gambar);

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


    public void showDialogUpload()
    {
        String[] pilihan={"Kamera","Galeri"};
        AlertDialog.Builder alert=new AlertDialog.Builder(EditMejaActivity.this);
        alert.setTitle("Upload Gambar Menu").setItems(pilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                switch (which)
                {
                    case 0:

                        EasyImage.openCameraForImage(EditMejaActivity.this,CODE_REQUEST_CAMERA);

                        break;

                    case 1:
                        EasyImage.openGallery(EditMejaActivity.this,CODE_REQUEST_GALLERY);
                        break;
                }

            }
        }).setCancelable(true);

        alert.show();
    }


    public String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageType=baos.toByteArray();

        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, EditMejaActivity.this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(EditMejaActivity.this, "Error on picking image "+e, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                switch (type)
                {
                    case CODE_REQUEST_CAMERA:
                    case CODE_REQUEST_GALLERY:
                        Glide.with(EditMejaActivity.this).load(imageFiles.get(0)).into(iveditnomeja);
                        break;
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }
}
