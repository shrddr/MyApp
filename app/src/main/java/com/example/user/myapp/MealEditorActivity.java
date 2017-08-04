package com.example.user.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class MealEditorActivity extends AppCompatActivity {

    public static final String MESSAGE_ID = "com.example.myfirstapp.MESSAGE_ID";
    public static final String MESSAGE_DATE = "com.example.myfirstapp.MESSAGE_DATE";
    private static final int NEW_ID = -1;

    private MySQLiteOpenHelper mDbHelper;
    private int mealId;
    private String currentDayString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_input);

        mDbHelper = new MySQLiteOpenHelper(this);
        EditText editMealTime = (EditText)findViewById(R.id.editMealTime);
        EditText editMealName = (EditText)findViewById(R.id.editMealName);
        EditText editMealSize = (EditText)findViewById(R.id.editMealSize);

        Intent intent = getIntent();
        mealId = intent.getIntExtra(MESSAGE_ID, NEW_ID);
        currentDayString = intent.getStringExtra(MESSAGE_DATE);

        if (mealId == NEW_ID) {
            Date d = new Date();
            editMealTime.setText(DateFormat.getTimeFormat(this).format(d));
            editMealTime.setTag(d.getTime()/1000);
        }

        if (mealId != NEW_ID) {
            Meal m = mDbHelper.getMeal(mealId);
            editMealName.setText(m.name);
            editMealTime.setText(m.time);
            editMealSize.setText(Integer.toString(m.size));
        }

        new SetTime(editMealTime, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meal_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_confirm:
                confirm(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void confirm(View view) {
        EditText editMealName = (EditText)findViewById(R.id.editMealName);
        EditText editMealTime = (EditText)findViewById(R.id.editMealTime);
        EditText editMealSize = (EditText)findViewById(R.id.editMealSize);

        int mealSize = 0;
        try {
            mealSize = Integer.parseInt(editMealSize.getText().toString());
        }
        catch (NumberFormatException e) {
            return;
        }

        Meal m = new Meal(mealId,
                editMealName.getText().toString(),
                currentDayString,
                editMealTime.getText().toString(),
                mealSize
        );

        if (mealId == NEW_ID)
            mDbHelper.addMeal(m);
        else
            mDbHelper.editMeal(m);

        startActivity(new Intent(this, MainActivity.class));
    }
}
