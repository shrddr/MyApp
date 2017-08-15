package com.example.user.myapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

class Product implements Parcelable {

    public int id;
    public String name;
    public float prot;
    public float fat;
    public float carb;

    public Product(int id) {
        this.id = id;
    }

    public Product(int id, String name, float prot, float fat, float carb) {
        this.id = id;
        this.name = name;
        this.prot = prot;
        this.fat = fat;
        this.carb = carb;
    }

    public Product (Cursor c) {
        this.id = c.getInt(c.getColumnIndexOrThrow(MySQLiteOpenHelper.PRODUCTS_COL_ID));
        this.name = c.getString(c.getColumnIndexOrThrow(MySQLiteOpenHelper.PRODUCTS_COL_NAME));
        this.prot = c.getFloat(c.getColumnIndexOrThrow(MySQLiteOpenHelper.PRODUCTS_COL_PROT));
        this.fat = c.getFloat(c.getColumnIndexOrThrow(MySQLiteOpenHelper.PRODUCTS_COL_FAT));
        this.carb = c.getFloat(c.getColumnIndexOrThrow(MySQLiteOpenHelper.PRODUCTS_COL_CARB));
    }

    public String toString() {
        return id + " " + name + " (" + prot + " " + fat + " " + carb + ")";
    }

    public String getProt() { return String.format("%.0f", prot); }

    public String getFat() {
        return String.format("%.0f", fat);
    }

    public String getCarb() {
        return String.format("%.0f", carb);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeFloat(prot);
        dest.writeFloat(fat);
        dest.writeFloat(carb);
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        prot = in.readFloat();
        fat = in.readFloat();
        carb = in.readFloat();
    }


}
