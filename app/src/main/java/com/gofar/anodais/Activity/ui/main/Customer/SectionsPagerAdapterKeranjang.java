package com.gofar.anodais.Activity.ui.main.Customer;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gofar.anodais.Fragment.Customer.FragCheckOut;
import com.gofar.anodais.Fragment.Customer.FragKeranjang;
import com.gofar.anodais.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterKeranjang extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_keranjang_1, R.string.tab_keranjang_2};
    private final Context mContext;

    public SectionsPagerAdapterKeranjang(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
       Fragment frag=new Fragment();

       switch (position)
       {
           case 0:
               frag=new FragKeranjang();
               break;

           case 1:
               frag=new FragCheckOut();
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
        return 2;
    }
}