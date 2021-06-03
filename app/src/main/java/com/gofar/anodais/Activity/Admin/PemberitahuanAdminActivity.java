package com.gofar.anodais.Activity.Admin;

import android.os.Bundle;

import com.gofar.anodais.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.gofar.anodais.Activity.ui.main.Admin.SectionsPagerAdapterPemberitahuan;

public class PemberitahuanAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan_admin);
        SectionsPagerAdapterPemberitahuan sectionsPagerAdapterPemberitahuan = new SectionsPagerAdapterPemberitahuan(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapterPemberitahuan);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }
}