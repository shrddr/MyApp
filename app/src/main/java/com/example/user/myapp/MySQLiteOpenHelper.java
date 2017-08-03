package com.example.user.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 2;
    private static final String MEALS_TABLE_NAME = "dictionary";
    private static final String MEALS_COL_ID = "id";
    private static final String MEALS_COL_NAME = "name";
    private static final String MEALS_COL_DATE = "date";
    private static final String MEALS_COL_TIME = "time";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + MEALS_TABLE_NAME + " (" +
                    "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name" + " TEXT, " +
                    "date" + " TEXT, " +
                    "time" + " TEXT);";

    MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void cleanDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MEALS_TABLE_NAME, null, null);
    }

    public void addMeal(Meal m) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEALS_COL_NAME, m.name);
        values.put(MEALS_COL_DATE, m.date);
        values.put(MEALS_COL_TIME, m.time);

        long newRowId = db.insert(MEALS_TABLE_NAME, null, values);
    }

    public void editMeal(Meal m) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEALS_COL_NAME, m.name);
        values.put(MEALS_COL_DATE, m.date);
        values.put(MEALS_COL_TIME, m.time);

        int count = db.update(
                MEALS_TABLE_NAME,
                values,
                MEALS_COL_ID + " = " + m.id,
                null);
    }

    public Meal getMeal(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = { MEALS_COL_NAME, MEALS_COL_DATE, MEALS_COL_TIME };

        Cursor cursor = db.query(
                MEALS_TABLE_NAME,                    // The table to query
                projection,                               // The columns to return
                MEALS_COL_ID + " = " + id,           // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if (cursor.moveToFirst()) {
            Meal m = new Meal(id,
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_NAME)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_DATE)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_TIME)));
            cursor.close();
            return m;
        }
        else
            return null;
    }

    public List<Meal> getMeals(String currentDay) {
        List<Meal> meals = new ArrayList();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                MEALS_COL_ID,
                MEALS_COL_NAME,
                MEALS_COL_DATE,
                MEALS_COL_TIME
        };

        String selection = MEALS_COL_DATE + " = ?";
        String[] selectionArgs = { currentDay };

        String sortOrder =
                MEALS_COL_TIME + " ASC";

        Cursor cursor = db.query(
                MEALS_TABLE_NAME,                         // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {
            Meal m = new Meal(cursor.getInt(cursor.getColumnIndexOrThrow(MEALS_COL_ID)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_NAME)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_DATE)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_TIME)));
            meals.add(m);
        }
        cursor.close();

        String sql = "SELECT DATE('1501769675')";
        Cursor mycursor = db.rawQuery(sql, null);
        mycursor.moveToFirst();
        int c = mycursor.getColumnCount();
        String s = mycursor.getString(0);
        mycursor.close();

        return meals;
    }
}
