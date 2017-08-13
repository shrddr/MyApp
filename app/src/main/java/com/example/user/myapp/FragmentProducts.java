package com.example.user.myapp;


import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FragmentProducts extends Fragment implements View.OnClickListener {

    private MySQLiteOpenHelper mDbHelper;
    private ListView listViewProducts;
    private String currentFilter;

    public FragmentProducts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        setHasOptionsMenu(true);

        v.findViewById(R.id.buttonAdd).setOnClickListener(this);

        mDbHelper = new MySQLiteOpenHelper(getActivity());
        listViewProducts = (ListView)v.findViewById(R.id.listViewProducts);
        currentFilter = "";
        refreshList();

        return v;
    }

    @Override
    public void onResume() {
        refreshList();
        super.onResume();
    }

    private void refreshList() {
        List<Product> products = mDbHelper.getProducts(currentFilter);
        listViewProducts.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, products));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                Product p = new Product(Constants.NEW_ID);
                Intent i = new Intent(getActivity(), ActivityProductEditor.class);
                Bundle b = new Bundle();
                b.putParcelable(ActivityProductEditor.PRODUCT_PARCEL, p);
                i.putExtras(b);
                startActivityForResult(i, Constants.REQUEST_PRODUCT_EDITOR);
                break;
        }
    }


}
