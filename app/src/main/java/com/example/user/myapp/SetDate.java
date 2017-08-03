package com.example.user.myapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Overrides the onFocusChange event of an TextView to open up a DatePickerDialog.
 * The onDateSet event of the picker fills the TextView with the selected date.
 */
class SetDate implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private Context ctx;
    private TextView textView;
    private Calendar myCalendar;

    public SetDate(TextView editText, Context ctx){
        this.ctx = ctx;
        this.textView = editText;
        this.textView.setOnFocusChangeListener(this);
        this.myCalendar = Calendar.getInstance();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) return;

        Date dt;
        try {
            dt = DateFormat.getDateFormat(ctx).parse(this.textView.getText().toString());
        } catch (ParseException e) {
            dt = new Date();
        }
        myCalendar.setTime(dt);
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(ctx, 0, this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
        this.textView.setTag(myCalendar.getTime().getTime()/1000);
        this.textView.setText(DateFormat.getDateFormat(ctx).format(myCalendar.getTime()));
    }

}
