package com.example.user.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 6;
    private static final String MEALS_TABLE_NAME = "meals";
    static final String MEALS_COL_ID = "_id";
    static final String MEALS_COL_NAME = "name";
    static final String MEALS_COL_DATE = "date";
    static final String MEALS_COL_TIME = "time";
    static final String MEALS_COL_SIZE = "size";
    private static final String PRODUCTS_TABLE_NAME = "products";
    static final String PRODUCTS_COL_ID = "_id";
    static final String PRODUCTS_COL_NAME = "name";
    static final String PRODUCTS_COL_PROT = "prot";
    static final String PRODUCTS_COL_FAT = "fat";
    static final String PRODUCTS_COL_CARB = "carb";

    private static final String MEALS_TABLE_CREATE =
            "CREATE TABLE " + MEALS_TABLE_NAME + " (" +
                    MEALS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    MEALS_COL_NAME + " TEXT, " +
                    MEALS_COL_DATE + " TEXT, " +
                    MEALS_COL_TIME + " TEXT, " +
                    MEALS_COL_SIZE + " INT);";

    private static final String PRODUCTS_TABLE_CREATE =
            "CREATE TABLE " + PRODUCTS_TABLE_NAME + " (" +
                    PRODUCTS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    PRODUCTS_COL_NAME + " TEXT, " +
                    PRODUCTS_COL_PROT + " REAL, " +
                    PRODUCTS_COL_FAT + " REAL, " +
                    PRODUCTS_COL_CARB + " REAL);";

    MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MEALS_TABLE_CREATE);
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEALS_TABLE_NAME);
        db.execSQL(MEALS_TABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
        db.execSQL(PRODUCTS_TABLE_CREATE);
    }

    void cleanDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MEALS_TABLE_NAME, null, null);
    }

    void updateMeal(Meal m) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEALS_COL_NAME, m.name);
        values.put(MEALS_COL_DATE, m.date);
        values.put(MEALS_COL_TIME, m.time);
        values.put(MEALS_COL_SIZE, m.size);

        if (m.id == Constants.NEW_ID)
            db.insert(
                    MEALS_TABLE_NAME,
                    null,
                    values);
        else
            db.update(
                    MEALS_TABLE_NAME,
                    values,
                    MEALS_COL_ID + " = " + m.id,
                    null);
    }

    void deleteMeal(int id) {
        getWritableDatabase().delete(
                MEALS_TABLE_NAME,
                MEALS_COL_ID + " = " + id,
                null);
    }

    Cursor getMealCursor(String currentDay) {
        return getReadableDatabase().query(
                MEALS_TABLE_NAME,                       // The table to query
                null,                                   // The columns to return
                MEALS_COL_DATE + " = ?",                              // The columns for the WHERE clause
                new String[] { currentDay },                          // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                MEALS_COL_TIME + " ASC"                               // The sort order
        );
    }

    void updateProduct(Product p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCTS_COL_NAME, p.name);
        values.put(PRODUCTS_COL_PROT, p.prot);
        values.put(PRODUCTS_COL_FAT, p.fat);
        values.put(PRODUCTS_COL_CARB, p.carb);

        if (p.id == Constants.NEW_ID)
            db.insert(
                    PRODUCTS_TABLE_NAME,
                    null,
                    values);
        else
            db.update(
                    PRODUCTS_TABLE_NAME,
                    values,
                    PRODUCTS_COL_ID + " = " + p.id,
                    null);
    }

    void deleteProduct(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                PRODUCTS_TABLE_NAME,
                PRODUCTS_COL_ID + " = " + id,
                null);
    }

    Cursor getProductCursor(String s) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        if (s.isEmpty()) {
            cursor = db.query(
                    PRODUCTS_TABLE_NAME,                       // The table to query
                    null,                                   // The columns to return
                    null,                              // The columns for the WHERE clause
                    null,                          // The values for the WHERE clause
                    null,                                   // don't group the rows
                    null,                                   // don't filter by row groups
                    PRODUCTS_COL_NAME + " ASC"                               // The sort order
            );
        }
        else {
            cursor = db.query(
                    PRODUCTS_TABLE_NAME,                       // The table to query
                    null,                                   // The columns to return
                    PRODUCTS_COL_NAME + " LIKE ?",                              // The columns for the WHERE clause
                    new String[] { "%"+s+"%" },                          // The values for the WHERE clause
                    null,                                   // don't group the rows
                    null,                                   // don't filter by row groups
                    PRODUCTS_COL_NAME + " ASC"                               // The sort order
            );
        }

        return cursor;
    }
}
