package com.example.user.myapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Overrides the onFocusChange event of an TextView to open up a TimePickerDialog.
 * The onTimeSet event of the picker fills the TextView with the selected time.
 */
class SetTime implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    private Context ctx;
    private TextView textView;
    private Calendar myCalendar;

    public SetTime(TextView editText, Context ctx){
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
            dt = DateFormat.getTimeFormat(ctx).parse(this.textView.getText().toString());
        } catch (ParseException e) {
            dt = new Date();
        }
        myCalendar.setTime(dt);
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        new TimePickerDialog(ctx, this, hour, minute, DateFormat.is24HourFormat(ctx)).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);
        this.textView.setTag(myCalendar.getTime().getTime()/1000);
        this.textView.setText(DateFormat.getTimeFormat(ctx).format(myCalendar.getTime()));
    }

}
