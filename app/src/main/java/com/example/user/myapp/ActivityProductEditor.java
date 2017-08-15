package com.example.user.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class ActivityProductEditor extends AppCompatActivity {

    private Product p;
    private EditText etName, etProt, etFat, etCarb;
    private MySQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_editor);

        mDbHelper = new MySQLiteOpenHelper(this);

        etName = (EditText)findViewById(R.id.editTextName);
        etProt = (EditText)findViewById(R.id.editTextProt);
        etFat = (EditText)findViewById(R.id.editTextFat);
        etCarb = (EditText)findViewById(R.id.editTextCarb);

        p = this.getIntent().getExtras().getParcelable(Constants.PRODUCT_PARCEL);
        if (p != null && p.id != Constants.NEW_ID) {
            etName.setText(p.name);
            etProt.setText(p.getProt());
            etFat.setText(p.getFat());
            etCarb.setText(p.getCarb());
        }
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
                p.name = etName.getText().toString();
                try {
                    p.prot = Float.parseFloat(etProt.getText().toString());
                    p.fat = Float.parseFloat(etFat.getText().toString());
                    p.carb = Float.parseFloat(etCarb.getText().toString());
                }
                catch (NumberFormatException e) {
                    return true;
                }

                mDbHelper.updateProduct(p);
                finish();

                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
