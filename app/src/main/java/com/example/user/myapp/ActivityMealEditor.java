package com.example.user.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class ActivityMealEditor extends AppCompatActivity
        implements View.OnClickListener {

    private MySQLiteOpenHelper mDbHelper;
    private Meal m;
    private EditText etTime, etName, etSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_editor);

        mDbHelper = new MySQLiteOpenHelper(this);

        etTime = (EditText)findViewById(R.id.editMealTime);
        etName = (EditText)findViewById(R.id.editMealName);
        etSize = (EditText)findViewById(R.id.editMealSize);

        m = this.getIntent().getExtras().getParcelable(Constants.MEAL_PARCEL);

        if (m.id == Constants.NEW_ID) {
            Date d = new Date();
            etTime.setText(DateFormat.getTimeFormat(this).format(d));
            etTime.setTag(d.getTime()/1000);
        }
        else {
            etName.setText(m.name);
            etTime.setText(m.time);
            etSize.setText(Integer.toString(m.size));
        }

        new SetTime(etTime, this);
        etName.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_confirm:
                confirm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editMealName: {
                Intent i = new Intent(this, ActivityProductPicker.class);
                startActivityForResult(i, Constants.REQUEST_PRODUCT_PICKER);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Constants.DIALOG_SUCCESS) return;
        Product p = data.getExtras().getParcelable(Constants.PRODUCT_PARCEL);
        etName.setText(p.name);
    }

    public void confirm() {

        try {
            m.name = etName.getText().toString();
            m.size = Integer.parseInt(etSize.getText().toString());
            m.time = etTime.getText().toString();
        }
        catch (NumberFormatException e) {
            return;
        }

        mDbHelper.updateMeal(m);

        startActivity(new Intent(this, ActivityMain.class));
    }
}
