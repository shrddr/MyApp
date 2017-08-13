package com.example.user.myapp;

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

    public String toString() {
        return id + " " + name + " (" + prot + " " + fat + " " + carb + ")";
    }

    public String getProt() {
        return Float.toString(prot);
    }

    public String getFat() {
        return Float.toString(fat);
    }

    public String getCarb() {
        return Float.toString(carb);
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
