package com.gofar.anodais.Activity.ui.main.Admin;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gofar.anodais.Fragment.Admin.FragPembatalan;
import com.gofar.anodais.Fragment.Admin.FragPesanDitempat;
import com.gofar.anodais.Fragment.Admin.FragPesanan;
import com.gofar.anodais.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterPemberitahuan extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_notif_1, R.string.tab_notif_2, R.string.tab_notif_3};
    private final Context mContext;

    public SectionsPagerAdapterPemberitahuan(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag=new Fragment();

        switch (position)
        {
            case 0:
                frag=new FragPembatalan();
                break;

            case 1:
                frag=new FragPesanan();
                break;

            case 2:
                frag=new FragPesanDitempat();
                break;
        }

        return frag;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}