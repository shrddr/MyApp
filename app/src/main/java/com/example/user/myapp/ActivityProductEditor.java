package com.example.user.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class ActivityProductEditor extends AppCompatActivity {

    public static final String PRODUCT_PARCEL = "com.example.myfirstapp.PRODUCT_PARCEL";
    private Product p;
    private EditText eName, eProt, eFat, eCarb;
    private MySQLiteOpenHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_editor);

        mDbHelper = new MySQLiteOpenHelper(this);

        eName = (EditText)findViewById(R.id.editTextName);
        eProt = (EditText)findViewById(R.id.editTextProt);
        eFat = (EditText)findViewById(R.id.editTextFat);
        eCarb = (EditText)findViewById(R.id.editTextCarb);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            p = b.getParcelable(PRODUCT_PARCEL);
            eName.setText(p.name);
            eProt.setText(p.getProt());
            eFat.setText(p.getFat());
            eCarb.setText(p.getCarb());
        }
        else
            p = new Product(0, "", 0, 0, 0);
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
                p.name = eName.getText().toString();
                try {
                    p.prot = Float.parseFloat(eProt.getText().toString());
                    p.fat = Float.parseFloat(eFat.getText().toString());
                    p.carb = Float.parseFloat(eCarb.getText().toString());
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
