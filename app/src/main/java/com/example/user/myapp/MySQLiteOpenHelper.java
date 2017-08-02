package com.example.user.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 2;
    private static final String MEALS_TABLE_NAME = "dictionary";
    private static final String MEALS_COL_ID = "id";
    private static final String MEALS_COL_NAME = "name";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + MEALS_TABLE_NAME + " (" +
                    "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name" + " TEXT);";

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

    public void addMeal(String s) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEALS_COL_NAME, s);

        long newRowId = db.insert(MEALS_TABLE_NAME, null, values);
    }

    public void editMeal(int id, String s) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEALS_COL_NAME, s);

        int count = db.update(
                MEALS_TABLE_NAME,
                values,
                MEALS_COL_ID + " = " + id,
                null);
    }

    public Meal getMeal(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {MEALS_COL_NAME};

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
            Meal m = new Meal(id, cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_NAME)));
            cursor.close();
            return m;
        }
        else
            return null;
    }

    public List<Meal> getMeals() {
        List<Meal> meals = new ArrayList();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                MEALS_COL_ID,
                MEALS_COL_NAME
        };

        String selection = MEALS_COL_ID + " > ?";
        String[] selectionArgs = { "-1" };

        String sortOrder =
                MEALS_COL_ID + " ASC";

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
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_NAME)));
            meals.add(m);
        }
        cursor.close();

        return meals;
    }
}
