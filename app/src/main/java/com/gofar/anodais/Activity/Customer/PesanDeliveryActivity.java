package com.gofar.anodais.Activity.Customer;

import android.os.Bundle;

import com.gofar.anodais.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.gofar.anodais.Activity.ui.main.Customer.SectionsPagerAdapterPesanMenu;

public class PesanDeliveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_menu);
        SectionsPagerAdapterPesanMenu sectionsPagerAdapterPesanMenu = new SectionsPagerAdapterPesanMenu(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapterPesanMenu);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }
}