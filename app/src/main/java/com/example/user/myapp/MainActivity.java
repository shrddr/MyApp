package com.example.user.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> titles = new ArrayList();
        titles.add("a");

        Intent intent = getIntent();
        String message = intent.getStringExtra(MealEditorActivity.EXTRA_MESSAGE);
        if (message != null)
            titles.add(message);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    public void addMeal(View view) {
        Intent intent = new Intent(this, MealEditorActivity.class);
        startActivity(intent);
    }
}
