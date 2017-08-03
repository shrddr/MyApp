package com.example.user.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Calendar currentDay;
    private String currentDayString;
    private MySQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new MySQLiteOpenHelper(this);

        currentDay = Calendar.getInstance();
        currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());

        refreshDisplay();

        EditText editTextDate = (EditText)findViewById(R.id.editTextDate);
        new SetDate(editTextDate, this);
    }

    private void refreshDisplay() {

        EditText editTextDate = (EditText)findViewById(R.id.editTextDate);
        editTextDate.setTag(currentDay.getTime().getTime()/1000);
        editTextDate.setText(DateFormat.getDateFormat(this).format(currentDay.getTime()));

        List<Meal> meals = mDbHelper.getMeals(currentDayString);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meals));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal m = (Meal)parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, MealEditorActivity.class);
                intent.putExtra(MealEditorActivity.MESSAGE_ID, m.id);
                intent.putExtra(MealEditorActivity.MESSAGE_DATE, currentDayString);
                startActivity(intent);
            }
        });
    }

    public void cleanDB(View view) {
        mDbHelper.cleanDB();
        refreshDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    public void addMeal(View view) {
        Intent intent = new Intent(this, MealEditorActivity.class);
        intent.putExtra(MealEditorActivity.MESSAGE_DATE, currentDayString);
        startActivity(intent);
    }

    public void prevDay(View view) {
        currentDay.add(Calendar.DATE, -1);
        currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
        refreshDisplay();
    }

    public void nextDay(View view) {
        currentDay.add(Calendar.DATE, 1);
        currentDayString = new SimpleDateFormat("YYYY-MM-dd", Locale.US).format(currentDay.getTime());
        refreshDisplay();
    }
}
