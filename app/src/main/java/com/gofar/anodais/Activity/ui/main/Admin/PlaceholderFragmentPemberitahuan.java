package com.gofar.anodais.Activity.ui.main.Admin;

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
public class PlaceholderFragmentPemberitahuan extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModelPemberitahuan pageViewModelPemberitahuan;

    public static PlaceholderFragmentPemberitahuan newInstance(int index) {
        PlaceholderFragmentPemberitahuan fragment = new PlaceholderFragmentPemberitahuan();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModelPemberitahuan = ViewModelProviders.of(this).get(PageViewModelPemberitahuan.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModelPemberitahuan.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pemberitahuan, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModelPemberitahuan.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}