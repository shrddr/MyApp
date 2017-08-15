package com.example.user.myapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityProductPicker extends AppCompatActivity {

    private MySQLiteOpenHelper mDbHelper;
    private String currentFilter;
    private ProductCursorAdapter pca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_picker);

        mDbHelper = new MySQLiteOpenHelper(this);

        currentFilter = "";
        ListView listViewProducts = (ListView)findViewById(R.id.listViewProducts);
        mDbHelper = new MySQLiteOpenHelper(this);
        Cursor c = mDbHelper.getProductCursor(currentFilter);
        pca = new ProductCursorAdapter(this, c);
        listViewProducts.setAdapter(pca);
    }

    private void refreshList() {
        Cursor c = mDbHelper.getProductCursor(currentFilter);
        pca.changeCursor(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_products, menu);
        super.onCreateOptionsMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText){
                currentFilter = newText;
                refreshList();
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                //
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ProductCursorAdapter extends CursorAdapter {

        ProductCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_product_picker, parent, false);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvProt = (TextView) view.findViewById(R.id.tvProt);
            TextView tvFat = (TextView) view.findViewById(R.id.tvFat);
            TextView tvCarb = (TextView) view.findViewById(R.id.tvCarb);

            final Product p = new Product(cursor);

            tvName.setText(p.name);
            tvProt.setText(p.getProt());
            tvFat.setText(p.getFat());
            tvCarb.setText(p.getCarb());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resultCode = Constants.DIALOG_SUCCESS;

                    /*Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putParcelable(Constants.PRODUCT_PARCEL, p);
                    i.putExtras(b);
                    setResult(resultCode, i);
                    finish();*/

                    Intent i = new Intent();
                    i.putExtra(Constants.PRODUCT_PARCEL, p);
                    setResult(resultCode, i);
                    finish();
                }
            });

        }
    }
}
