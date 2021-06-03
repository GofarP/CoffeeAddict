package com.gofar.anodais.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pl.aprilapps.easyphotopicker.EasyImage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class
EditMenuActivity extends AppCompatActivity {

    final int CODE_REQUEST_CAMERA=1;
    final int CODE_REQUEST_GALLERY = 2;

    ImageView ivedit;
    EditText txteditnamamenu,txteditharga,txteditdeskripsi,txteditstock;
    Spinner spneditjenis,spneditstatus;
    Button btneditmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        final Bundle bundle=getIntent().getExtras();

          txteditnamamenu=findViewById(R.id.txteditnamamenu);
          txteditharga=findViewById(R.id.txteditharga);
          txteditdeskripsi=findViewById(R.id.txteditdeskripsi);
          txteditstock=findViewById(R.id.txteditstock);
          spneditjenis=findViewById(R.id.spneditjenis);
          spneditstatus=findViewById(R.id.spneditstatus);
          btneditmenu=findViewById(R.id.btneditmenu);
          ivedit=findViewById(R.id.iveditmenu);


        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(EditMenuActivity.this,R.array.kategori,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spneditjenis.setAdapter(adapter);

        ArrayAdapter<CharSequence>adapterstatus=ArrayAdapter.createFromResource(EditMenuActivity.this,R.array.status,android.R.layout.simple_spinner_item);
        adapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spneditstatus.setAdapter(adapterstatus);

        txteditnamamenu.setText(bundle.getString("nama"));
        txteditharga.setText(String.valueOf(bundle.getDouble("harga")));
        txteditdeskripsi.setText(bundle.getString("deskripsi"));
        txteditstock.setText(String.valueOf(bundle.getInt("stock")));

        int getjenis=adapter.getPosition(bundle.getString("jenis"));
        int getstatus=adapterstatus.getPosition(bundle.getString("status"));

        spneditjenis.setSelection(getjenis);
        spneditstatus.setSelection(getstatus);

        ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpload();
            }
        });

        btneditmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                    if(validasi())
                    {
                        Toast.makeText(EditMenuActivity.this, "Silahkan isi kolom yang kosong", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        updateMenu(bundle.getString("id"));
                    }

                }

                catch (Exception e)
                {
                    Toast.makeText(EditMenuActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public boolean validasi()
    {

        boolean restrict=false;

        EditText[] editText={txteditdeskripsi,txteditharga,txteditnamamenu,txteditstock};

        for(EditText et:editText)
        {
            if(et.equals(""))
            {
                restrict=true;
            }
        }

        return restrict;
    }


    public String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] imageType=baos.toByteArray();

        return Base64.encodeToString(imageType, Base64.DEFAULT);
    }

    public void showDialogUpload()
    {
        String[] pilihan={"Kamera","Galeri"};
        AlertDialog.Builder alert=new AlertDialog.Builder(EditMenuActivity.this);
        alert.setTitle("Upload Gambar Menu").setItems(pilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which)
                        {
                            case 0:

                                EasyImage.openCameraForImage(EditMenuActivity.this,CODE_REQUEST_CAMERA);

                                break;

                            case 1:
                                EasyImage.openGallery(EditMenuActivity.this,CODE_REQUEST_GALLERY);
                                break;
                        }

                    }
                }).setCancelable(true);

        alert.show();
    }



    public void updateMenu(final String id)
    {
        try
        {
            String url_updatemenu = Server.URL + "updatemenu.php";
            RequestQueue requestQueue = Volley.newRequestQueue(EditMenuActivity.this);
            StringRequest strupdate=new StringRequest(Request.Method.POST, url_updatemenu, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        String TAG_SUCCESS="success";
                        String TAG_MESSAGE = "message";

                        JSONObject obj=new JSONObject(response);
                        int success=obj.getInt(TAG_SUCCESS);
                        String msg=obj.getString(TAG_MESSAGE);

                        if(success==1)
                        {
                            Toast.makeText(EditMenuActivity.this, msg, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditMenuActivity.this,MenuActivity.class));
                            finish();
                        }

                        else
                        {
                            Toast.makeText(EditMenuActivity.this, obj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e)
                    {
                        Toast.makeText(EditMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditMenuActivity.this, "error:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();

                    String gambar= (ivedit.getDrawable()==null) ? "" : imageToString(((BitmapDrawable)ivedit.getDrawable()).getBitmap()) ;

                    params.put("nama",txteditnamamenu.getText().toString());
                    params.put("harga",txteditharga.getText().toString());
                    params.put("status",spneditstatus.getSelectedItem().toString());
                    params.put("jenis",spneditjenis.getSelectedItem().toString());
                    params.put("deskripsi",txteditdeskripsi.getText().toString());
                    params.put("stock",txteditstock.getText().toString());
                    params.put("id",id);
                    params.put("image",gambar);

                    return params;
                }
            };

            requestQueue.add(strupdate);
        }

        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, EditMenuActivity.this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(EditMenuActivity.this, "Image Picker Errpr"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                switch (type)
                {
                    case CODE_REQUEST_CAMERA:
                    case CODE_REQUEST_GALLERY:
                        Glide.with(EditMenuActivity.this).load(imageFiles.get(0)).centerCrop().into(ivedit);
                        break;
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Toast.makeText(EditMenuActivity.this, "Image Picking Canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
