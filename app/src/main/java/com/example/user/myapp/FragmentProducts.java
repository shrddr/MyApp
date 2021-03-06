package com.example.user.myapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentProducts extends Fragment implements View.OnClickListener {

    private MySQLiteOpenHelper mDbHelper;
    private String currentFilter;
    private ProductCursorAdapter pca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        setHasOptionsMenu(true);

        v.findViewById(R.id.buttonAdd).setOnClickListener(this);

        currentFilter = "";
        mDbHelper = new MySQLiteOpenHelper(getActivity());
        ListView listViewProducts = (ListView) v.findViewById(R.id.listViewProducts);
        Cursor c = mDbHelper.getProductCursor(currentFilter);
        pca = new ProductCursorAdapter(getContext(), c, this);
        listViewProducts.setAdapter(pca);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd: addProduct();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_products, menu);
        super.onCreateOptionsMenu(menu, inflater);

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
    }

    @Override
    public void onResume() {
        refreshList();
        super.onResume();
    }

    private void refreshList() {
        Cursor c = mDbHelper.getProductCursor(currentFilter);
        pca.changeCursor(c);
    }

    void addProduct() {
        Product p = new Product(Constants.NEW_ID);
        editProduct(p);
    }

    void editProduct(Product p) {
        Intent i = new Intent(getActivity(), ActivityProductEditor.class);
        i.putExtra(Constants.PRODUCT_PARCEL, p);
        startActivityForResult(i, Constants.REQUEST_PRODUCT_EDITOR);
    }

    void deleteProduct(int id) {
        mDbHelper.deleteProduct(id);
        refreshList();
    }

    private class ProductCursorAdapter extends CursorAdapter {

        private FragmentProducts parent;

        ProductCursorAdapter(Context context, Cursor cursor, FragmentProducts parent) {
            super(context, cursor, 0);
            this.parent = parent;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
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
                    parent.editProduct(p);
                }
            });

            Button bDelete = (Button) view.findViewById(R.id.bDelete);
            bDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    parent.deleteProduct(p.id);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
