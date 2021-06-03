package com.gofar.anodais.Activity.ui.main.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gofar.anodais.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentPesanMenu extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModelPesanMenu pageViewModelPesanMenu;

    public static PlaceholderFragmentPesanMenu newInstance(int index) {
        PlaceholderFragmentPesanMenu fragment = new PlaceholderFragmentPesanMenu();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModelPesanMenu = ViewModelProviders.of(this).get(PageViewModelPesanMenu.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModelPesanMenu.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pesan_menu, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModelPesanMenu.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}