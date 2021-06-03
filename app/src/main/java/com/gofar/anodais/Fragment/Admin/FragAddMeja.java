package com.gofar.anodais.Fragment.Admin;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.gofar.anodais.Activity.Admin.ReservasiActivity;
import com.gofar.anodais.Adapter.General.MejaAdapter;
import com.gofar.anodais.Model.Meja;
import com.gofar.anodais.Model.Reservasi;
import com.gofar.anodais.R;
import com.gofar.anodais.conn.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.aprilapps.easyphotopicker.EasyImage;

public class FragAddMeja extends Fragment {

    RecyclerView.Adapter mejaadapter;
    RecyclerView.LayoutManager mejalayoutManager;
    final ArrayList<Reservasi> itemList = new ArrayList<Reservasi>();
    RecyclerView rvaddmeja;
    SearchView txtsearchmeja;
    ImageView ivtambahmeja;

    final int CODE_REQUEST_PERMISSIONS=001;
    final int CODE_REQUEST_CAMERA=1;
    final int CODE_REQUEST_GALLERY = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_add_meja_layout,container,false);

        FloatingActionButton fabaddmeja=view.findViewById(R.id.fabaddmeja);

        rvaddmeja=view.findViewById(R.id.rvaddmeja);
        txtsearchmeja=view.findViewById(R.id.txtsearchmeja);


        mejalayoutManager=new LinearLayoutManager(getActivity());
        rvaddmeja.setLayoutManager(mejalayoutManager);
        rvaddmeja.setHasFixedSize(true);

        fillDataMeja();

        imageperm();

        txtsearchmeja.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.equals("")) fillDataMeja(); else carimeja(newText);

                return true;
            }
        });

        fabaddmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              dialogAdd();

            }
        });

        return view;
    }





    public void fillDataMeja()
    {
        try
        {
            RequestQueue queue=Volley.newRequestQueue(getActivity());
            String url_selectmeja= Server.URL+"getmeja.php";
            JsonArrayRequest jar=new JsonArrayRequest(url_selectmeja, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    itemList.clear();

                    for(int i=0;i<response.length();i++)
                    {
                        try
                        {
                            JSONObject obj=response.getJSONObject(i);
                            Reservasi reservasi=new Reservasi();
                            reservasi.setId_meja(obj.getString("id_meja"));
                            reservasi.setNomeja(obj.getString("no_meja"));
                            reservasi.setMejaImage(obj.getString("images"));
                            reservasi.setJenis(obj.getString("jenis"));

                            itemList.add(reservasi);

                        }

                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(),e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }

                    mejaadapter=new MejaAdapter(itemList,getActivity(),1);
                    rvaddmeja.setAdapter(mejaadapter);
                    mejaadapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jar);
        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void dialogAdd()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View dialogView=inflater.inflate(R.layout.popuptambahmeja,null);

        builder.setView(dialogView);

        final AlertDialog closedialog=builder.create();

        final TextView txttambahmeja=dialogView.findViewById(R.id.txttambahmeja);
        final TextView txtjenis=dialogView.findViewById(R.id.txtjenismeja);

        Button btnaddmeja=dialogView.findViewById(R.id.btntambahmeja);

        ivtambahmeja=dialogView.findViewById(R.id.ivtambahmeja);

        ivtambahmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpload();
            }
        });



        btnaddmeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeja=txttambahmeja.getText().toString().trim();
                String jenismeja=txtjenis.getText().toString().trim();

                if(nomeja.equals("")||jenismeja.equals("")||ivtambahmeja.getDrawable().equals(null))
                {
                    Toast.makeText(getActivity(), "Silahkan isi nomeja dan foto yang mau ditambahkan", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    String image=imageToString(((BitmapDrawable)ivtambahmeja.getDrawable()).getBitmap());;

                    addMeja(nomeja,image,jenismeja);

                }

                closedialog.dismiss();


            }
        });

        closedialog.show();


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
        androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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



    public void carimeja(final String cari)
    {
        try
        {
            final String url_carimeja=Server.URL+"carimeja.php";
            RequestQueue queue=Volley.newRequestQueue(getActivity());

            StringRequest request=new StringRequest(Request.Method.POST, url_carimeja, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try
                    {
                        ArrayList<Reservasi> item=new ArrayList<>();

                        JSONObject obj=new JSONObject(response);
                        JSONArray jsonArray=obj.getJSONArray("data");

                        if(jsonArray.length()>0)
                        {
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    Reservasi reservasi=new Reservasi();
                                    reservasi.setId_meja(jsonObject.getString("id_meja"));
                                    reservasi.setNomeja(jsonObject.getString("no_meja"));
                                    reservasi.setMejaImage(jsonObject.getString("images"));
                                    reservasi.setJenis(jsonObject.getString("jenis"));

                                    item.add(reservasi);
                            }

                            mejaadapter=new MejaAdapter(item,getActivity(),1);
                            rvaddmeja.setAdapter(mejaadapter);
                            mejaadapter.notifyDataSetChanged();
                        }

                    }

                    catch (Exception e)
                    {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("nomeja",cari);
                    return params;
                }
            };

            queue.add(request);
        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void addMeja(final String nomeja, final String images, final String jenis)
    {
        try
        {

                String url_addmeja= Server.URL+"addmeja.php";
                RequestQueue queue= Volley.newRequestQueue(getActivity());
                StringRequest request=new StringRequest(Request.Method.POST, url_addmeja, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject obj=new JSONObject(response);
                            int success=obj.getInt("success");

                            if(success==1)
                            {

                                Reservasi reservasi=new Reservasi();
                                reservasi.setNomeja(nomeja);
                                reservasi.setMejaImage(images);
                                reservasi.setJenis(jenis);

                                itemList.add(reservasi);
                                mejaadapter.notifyDataSetChanged();

                                fillDataMeja();

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            }

                            else
                            {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("nomeja",nomeja);
                        params.put("images",images);
                        params.put("jenis",jenis);
                        return params;
                    }
                };

                queue.add(request);

        }

        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(getActivity(), "Image Picking Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                switch (type)
                {
                    case CODE_REQUEST_CAMERA:
                    case CODE_REQUEST_GALLERY:

                        Glide.with(getActivity()).load(imageFiles.get(0)).centerCrop().into(ivtambahmeja);
                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }


    public void imageperm()
    {
        String[] permissions={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(getActivity(),permissions[0])== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(),permissions[1])==PackageManager.PERMISSION_GRANTED)
        {

        }

        else
        {
            ActivityCompat.requestPermissions(getActivity(),permissions,CODE_REQUEST_PERMISSIONS);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageperm();
    }


}
