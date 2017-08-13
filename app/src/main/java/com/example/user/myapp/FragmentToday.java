package com.example.user.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragmentToday extends Fragment implements View.OnClickListener {

    private Calendar currentDay;
    private String currentDayString;
    private MySQLiteOpenHelper mDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_today, container, false);

        v.findViewById(R.id.buttonLeft).setOnClickListener(this);
        v.findViewById(R.id.buttonRight).setOnClickListener(this);
        v.findViewById(R.id.buttonAdd).setOnClickListener(this);
        v.findViewById(R.id.buttonClear).setOnClickListener(this);

        mDbHelper = new MySQLiteOpenHelper(getActivity());

        currentDay = Calendar.getInstance();
        currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());

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
                Intent intent = new Intent(getActivity(), ActivityMealEditor.class);
                intent.putExtra(ActivityMealEditor.MESSAGE_DATE, currentDayString);
                startActivity(intent);
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

        List<Meal> meals = mDbHelper.getMeals(currentDayString);

        ListView listView = (ListView)v.findViewById(R.id.listView);
        listView.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, meals));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal m = (Meal)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ActivityMealEditor.class);
                intent.putExtra(ActivityMealEditor.MESSAGE_ID, m.id);
                intent.putExtra(ActivityMealEditor.MESSAGE_DATE, currentDayString);
                startActivity(intent);
            }
        });
    }
}
