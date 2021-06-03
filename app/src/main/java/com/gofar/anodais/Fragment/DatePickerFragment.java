package com.gofar.anodais.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

    private onDateClickListener onDateClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int tahun=calendar.get(Calendar.YEAR);
        int bulan=calendar.get(Calendar.MONTH);
        int hari=calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                onDateClickListener.onDateSet(view,year,month,dayOfMonth);

            }
        }, tahun,bulan,hari);
    }

    public void setOnDateClickListener(onDateClickListener onDateClickListener)
    {
        if(onDateClickListener != null)
        {
            this.onDateClickListener=onDateClickListener;
        }
    }

    public interface onDateClickListener
    {
        void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth);
    }
}
