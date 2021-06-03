package com.gofar.anodais.Fragment.Admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gofar.anodais.Adapter.General.MenuAdapter;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import pl.aprilapps.easyphotopicker.EasyImage;

import com.gofar.anodais.Model.Menu;

public class FragAddMenu extends Fragment {

    Button btnaddmenu;
    EditText txtnamamenu,txthargamenu,txtdeskripsi,txtstock;
    Spinner spnjenis,spnstatus;
    ImageView ivuploadmenuimage;
    final int CODE_REQUEST_PERMISSIONS=001;
    final int CODE_REQUEST_CAMERA=1;
    final int CODE_REQUEST_GALLERY = 2;
    File imageFile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_menu,container,false);
        btnaddmenu=(Button)view.findViewById(R.id.btnaddmenu);
        txthargamenu=(EditText)view.findViewById(R.id.txtharga);
        txtnamamenu=(EditText)view.findViewById(R.id.txtnamamenu);
        txtdeskripsi=(EditText)view.findViewById(R.id.txtdeskripsi);
        txtstock=(EditText)view.findViewById(R.id.txtstock);
        spnjenis=(Spinner)view.findViewById(R.id.spnjenis);
        spnstatus=(Spinner)view.findViewById(R.id.spnstatus);
        ivuploadmenuimage=(ImageView)view.findViewById(R.id.ivuploadmenu);

        imageperm();

        ivuploadmenuimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpload();
            }
        });

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getContext(),R.array.kategori,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnjenis.setAdapter(adapter);

        ArrayAdapter<CharSequence>adapterstatus=ArrayAdapter.createFromResource(getContext(),R.array.status,android.R.layout.simple_spinner_item);
        adapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnstatus.setAdapter(adapterstatus);


        btnaddmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validasi())
                {
                    Toast.makeText(getContext(), "Silahkan isi kolom yang masih kosong", Toast.LENGTH_SHORT).show();
                }

                else
                {
                        String url_addmenu = Server.URL + "addmenu.php";
                        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        StringRequest straddmenu = new StringRequest(Request.Method.POST, url_addmenu, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    String TAG = FragAddMenu.class.getSimpleName();
                                    String TAG_SUCCESS="success";
                                    String TAG_MESSAGE = "message";

                                    JSONObject jObj = new JSONObject(response);
                                    int success=jObj.getInt(TAG_SUCCESS);

                                    if(success==1)
                                    {
                                        Toast.makeText(getContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                                        ArrayList<Menu>menuList=new ArrayList<>();

                                        Menu menu=new Menu();

                                        menu.setNama(txtnamamenu.getText().toString());
                                        menu.setHarga(Double.parseDouble(txthargamenu.getText().toString()));
                                        menu.setStatus(spnstatus.getSelectedItem().toString());
                                        menu.setJenis(spnjenis.getSelectedItem().toString());
                                        menu.setDeskripsi(txtdeskripsi.getText().toString());
                                        menu.setStock(Integer.parseInt(txtstock.getText().toString()));

                                        menuList.add(menu);
                                        MenuAdapter menuadapter=new MenuAdapter(menuList,getActivity(),1);

                                        menuadapter.notifyDataSetChanged();

                                        txtnamamenu.setText("");
                                        txthargamenu.setText("");
                                        txtdeskripsi.setText("");
                                        txtstock.setText("");
                                        ivuploadmenuimage.setImageDrawable(null);

                                        requestQueue.stop();

                                    }

                                    else
                                    {
                                        Toast.makeText(getContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                        requestQueue.stop();
                                    }

                                }

                                catch (Exception e)
                                {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    System.out.println(e.getMessage());
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })

                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                String nama=txtnamamenu.getText().toString();
                                String harga=txthargamenu.getText().toString();
                                String status=spnstatus.getSelectedItem().toString();
                                String jenis=spnjenis.getSelectedItem().toString();
                                String deskripsi=txtdeskripsi.getText().toString();
                                String stock=txtstock.getText().toString();
                                String gambar=imageToString(((BitmapDrawable)ivuploadmenuimage.getDrawable()).getBitmap());

                                params.put("nama",nama);
                                params.put("harga",harga);
                                params.put("status",status);
                                params.put("jenis",jenis);
                                params.put("deskripsi",deskripsi);
                                params.put("stock",stock);
                                params.put("image",gambar);

                                return params;

                            }

                        };

                        requestQueue.add(straddmenu);
                }
            }
        });


        return view;
    }


    public void showDialogUpload()
    {
        String[] pilihan={"Kamera","Galeri"};
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setTitle("Upload Gambar Menu")
                .setItems(pilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which)
                        {
                            case 0:

                                EasyImage.openCameraForImage(getActivity(),CODE_REQUEST_CAMERA);

                                break;

                            case 1:
                                EasyImage.openGallery(getActivity(),CODE_REQUEST_GALLERY);
                                break;
                        }

                    }
                }).setCancelable(true);

        alert.show();
    }


    public void imageperm()
    {
        String[] permissions={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(getActivity(),permissions[0])==PackageManager.PERMISSION_GRANTED
           && ContextCompat.checkSelfPermission(getActivity(),permissions[1])==PackageManager.PERMISSION_GRANTED)
        {
        }

        else
        {
            ActivityCompat.requestPermissions(getActivity(),permissions,CODE_REQUEST_PERMISSIONS);
        }
    }


    public boolean validasi()
    {
        EditText[] et={txtnamamenu,txthargamenu,txtdeskripsi};

        boolean restrict=false;

        for(EditText editText:et)
        {
            if(editText.getText().toString().trim().equals(""))
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageperm();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(getActivity(), "Image Picking Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                switch (type)
                {
                    case CODE_REQUEST_CAMERA:

                    case CODE_REQUEST_GALLERY:

                        Glide.with(getActivity()).load(imageFiles.get(0)).centerCrop().into(ivuploadmenuimage);
                        break;
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Toast.makeText(getActivity(), "Pengambilan gambar dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
