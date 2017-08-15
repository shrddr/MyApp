package com.example.user.myapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentToday extends Fragment implements View.OnClickListener {

    private Calendar currentDay;
    private String currentDayString;
    private MySQLiteOpenHelper mDbHelper;
    private TodayCursorAdapter tca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        v.findViewById(R.id.buttonLeft).setOnClickListener(this);
        v.findViewById(R.id.buttonRight).setOnClickListener(this);
        v.findViewById(R.id.buttonAdd).setOnClickListener(this);
        v.findViewById(R.id.buttonClear).setOnClickListener(this);

        currentDay = Calendar.getInstance();
        currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
        mDbHelper = new MySQLiteOpenHelper(getActivity());
        ListView listView = (ListView) v.findViewById(R.id.listViewToday);
        Cursor c = mDbHelper.getMealCursor(currentDayString);
        tca = new TodayCursorAdapter(getContext(), c, this);
        listView.setAdapter(tca);

        refreshView(v);

        EditText editTextDate = (EditText)v.findViewById(R.id.editTextDate);
        new SetDate(editTextDate, getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLeft:
                currentDay.add(Calendar.DATE, -1);
                currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
                refreshView(getView());
                break;
            case R.id.buttonRight:
                currentDay.add(Calendar.DATE, 1);
                currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
                refreshView(getView());
                break;
            case R.id.buttonAdd:
                addMeal();
                break;
            case R.id.buttonClear:
                mDbHelper.cleanDB();
                refreshView(getView());
                break;
        }
    }

    private void refreshView(View v) {
        EditText editTextDate = (EditText)v.findViewById(R.id.editTextDate);
        editTextDate.setTag(currentDay.getTime().getTime()/1000);
        editTextDate.setText(DateFormat.getDateFormat(getActivity()).format(currentDay.getTime()));

        Cursor c = mDbHelper.getMealCursor(currentDayString);
        tca.changeCursor(c);
    }

    void addMeal() {
        Meal m = new Meal(Constants.NEW_ID, currentDayString);
        editMeal(m);
    }

    void editMeal(Meal m) {
        Intent i = new Intent(getActivity(), ActivityMealEditor.class);
        i.putExtra(Constants.MEAL_PARCEL, m);
        startActivityForResult(i, Constants.REQUEST_MEAL_EDITOR);
    }

    void deleteMeal(int id) {
        mDbHelper.deleteMeal(id);
        refreshView(getView());
    }

    private class TodayCursorAdapter extends CursorAdapter {

        private FragmentToday parent;

        TodayCursorAdapter(Context context, Cursor cursor, FragmentToday parent) {
            super(context, cursor, 0);
            this.parent = parent;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_today, parent, false);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvProt = (TextView) view.findViewById(R.id.tvProt);
            TextView tvFat = (TextView) view.findViewById(R.id.tvFat);
            TextView tvCarb = (TextView) view.findViewById(R.id.tvCarb);

            final Meal m = new Meal(cursor);

            tvTime.setText(m.time);
            tvName.setText(m.name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.editMeal(m);
                }
            });

            Button bDelete = (Button) view.findViewById(R.id.bDelete);
            bDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    parent.deleteMeal(m.id);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
