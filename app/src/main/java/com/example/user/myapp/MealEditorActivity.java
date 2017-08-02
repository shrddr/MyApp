package com.example.user.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MealEditorActivity extends AppCompatActivity {

    public static final String MESSAGE_ID = "com.example.myfirstapp.MESSAGE_ID";

    private static int BAD_ID = -1;

    private MySQLiteOpenHelper mDbHelper;

    private Boolean editing = false;
    private int editId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_input);

        mDbHelper = new MySQLiteOpenHelper(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra(MESSAGE_ID, BAD_ID);
        if (id != BAD_ID) {
            editing = true;
            editId = id;
            EditText editText = (EditText)findViewById(R.id.editText2);

            Meal m = mDbHelper.getMeal(id);
            editText.setText(m.name);
        }
    }

    public void confirm(View view) {

        EditText editText = (EditText) findViewById(R.id.editText2);
        String s = editText.getText().toString();

        if (editing)
            mDbHelper.editMeal(editId, s);
        else
            mDbHelper.addMeal(s);

        startActivity(new Intent(this, MainActivity.class));
    }
}
