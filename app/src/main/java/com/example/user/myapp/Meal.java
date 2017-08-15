package com.example.user.myapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

class Meal implements Parcelable {

    public int id;
    public String name;
    public String date;
    public String time;
    public int size;

    Meal(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public Meal(int id, String name, String date, String time, int size) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.size = size;
    }

    public Meal(Cursor c) {
        this.id = c.getInt(c.getColumnIndexOrThrow(MySQLiteOpenHelper.MEALS_COL_ID));
        this.name = c.getString(c.getColumnIndexOrThrow(MySQLiteOpenHelper.MEALS_COL_NAME));
        this.date = c.getString(c.getColumnIndexOrThrow(MySQLiteOpenHelper.MEALS_COL_DATE));
        this.time = c.getString(c.getColumnIndexOrThrow(MySQLiteOpenHelper.MEALS_COL_TIME));
        this.size = c.getInt(c.getColumnIndexOrThrow(MySQLiteOpenHelper.MEALS_COL_SIZE));
    }

    protected Meal(Parcel in) {
        id = in.readInt();
        name = in.readString();
        date = in.readString();
        time = in.readString();
        size = in.readInt();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
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
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(size);
    }

    public String toString() {
        return id + " " + date + " " + time + " " + name + " " + size;
    }
}
