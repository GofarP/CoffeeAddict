package com.gofar.anodais.Activity.ui.main.Customer;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gofar.anodais.Fragment.Customer.FragPesanMakanan;
import com.gofar.anodais.Fragment.Customer.FragPesanMinuman;
import com.gofar.anodais.R;
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterPesanMenu extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_pesan_menu_text_1, R.string.tab_pesan_menu_text_2};
    private final Context mContext;

    public SectionsPagerAdapterPesanMenu(Context context, FragmentManager fm) {
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
               fragment=new FragPesanMakanan();
               break;

           case 1:
               fragment=new FragPesanMinuman();
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