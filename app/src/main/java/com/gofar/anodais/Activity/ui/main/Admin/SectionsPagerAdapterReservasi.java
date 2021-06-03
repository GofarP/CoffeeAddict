package com.gofar.anodais.Activity.ui.main.Admin;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gofar.anodais.Fragment.Admin.FragAddMeja;
import com.gofar.anodais.Fragment.Admin.FragDaftarPengajuanMeja;
import com.gofar.anodais.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterReservasi extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_reservasi_text_1, R.string.tab_reservasi_text_2};
    private final Context mContext;

    public SectionsPagerAdapterReservasi(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment=null;

        switch (position)
        {
            case 0:
                fragment= new FragAddMeja();
                break;

            case 1:
                fragment= new FragDaftarPengajuanMeja();
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}