package com.gofar.anodais.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gofar.anodais.Activity.Login.LoginActivity;
import com.gofar.anodais.R;
import com.gofar.anodais.Preference.Prefrences;

public class AdminActivity extends AppCompatActivity {

    CardView cv1,cv2,cv3,cv4,cv5;
    ImageView ivlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().hide();

        cv1=findViewById(R.id.cardView);
        cv2=findViewById(R.id.cardView2);
        cv3=findViewById(R.id.cardView3);
        cv4=findViewById(R.id.cardView4);
        cv5=findViewById(R.id.cardView5);
        ivlogout=findViewById(R.id.ivlogout);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,MenuActivity.class));
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,ReservasiActivity.class));
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,SettingActivity.class));
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,ReportActivity.class));
            }
        });

        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, PemberitahuanAdminActivity.class));
            }
        });

        ivlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Apakah anda ingin logout?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Prefrences.clearLoggedInUser(getBaseContext());
                        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                        finish();
                    }
                })

                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menulogout:
                Prefrences.clearLoggedInUser(getBaseContext());
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
