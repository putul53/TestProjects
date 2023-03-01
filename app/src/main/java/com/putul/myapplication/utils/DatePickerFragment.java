package com.putul.myapplication.utils;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener ondateSet;

    public DatePickerFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day;
    private int fare;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        fare = args.getInt("fare");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        calendar.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        return datePickerDialog;
    }

}
