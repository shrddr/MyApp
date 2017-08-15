package com.example.user.myapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

class ProductCursorAdapter extends CursorAdapter {

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
