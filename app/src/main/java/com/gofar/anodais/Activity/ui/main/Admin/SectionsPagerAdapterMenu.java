package com.gofar.anodais.Activity.ui.main.Admin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gofar.anodais.Fragment.Admin.FragMakanan;
import com.gofar.anodais.Fragment.Admin.FragAddMenu;
import com.gofar.anodais.Fragment.Admin.FragMinuman;
import com.gofar.anodais.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterMenu extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_menu_text_1, R.string.tab_menu_text_2,R.string.tab_menu_text_3};
    private final Context mContext;

    public SectionsPagerAdapterMenu(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;

        switch(position)
        {
            case 0:
                fragment=new FragAddMenu();
                break;

            case 1:
                fragment=new FragMakanan();
                break;

            case 2:
                fragment=new FragMinuman();
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
        // Show 3 total pages.
        return 3;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}