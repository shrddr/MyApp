package com.example.user.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 5;
    private static final String MEALS_TABLE_NAME = "meals";
    private static final String MEALS_COL_ID = "id";
    private static final String MEALS_COL_NAME = "name";
    private static final String MEALS_COL_DATE = "date";
    private static final String MEALS_COL_TIME = "time";
    private static final String MEALS_COL_SIZE = "size";
    private static final String PRODUCTS_TABLE_NAME = "products";
    private static final String PRODUCTS_COL_ID = "id";
    private static final String PRODUCTS_COL_NAME = "name";
    private static final String PRODUCTS_COL_PROT = "prot";
    private static final String PRODUCTS_COL_FAT = "fat";
    private static final String PRODUCTS_COL_CARB = "carb";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEALS_TABLE_NAME);
        db.execSQL(MEALS_TABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
        db.execSQL(PRODUCTS_TABLE_CREATE);
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
        values.put(MEALS_COL_SIZE, m.size);

        long newRowId = db.insert(MEALS_TABLE_NAME, null, values);
    }

    public void editMeal(Meal m) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MEALS_COL_NAME, m.name);
        values.put(MEALS_COL_DATE, m.date);
        values.put(MEALS_COL_TIME, m.time);
        values.put(MEALS_COL_SIZE, m.size);

        int count = db.update(
                MEALS_TABLE_NAME,
                values,
                MEALS_COL_ID + " = " + m.id,
                null);
    }

    public Meal getMeal(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                MEALS_COL_NAME,
                MEALS_COL_DATE,
                MEALS_COL_TIME,
                MEALS_COL_SIZE
        };

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
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_TIME)),
                              cursor.getInt(cursor.getColumnIndexOrThrow(MEALS_COL_SIZE))       );
            cursor.close();
            return m;
        }
        else
            return null;
    }

    public List<Meal> getMeals(String currentDay) {

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                MEALS_COL_ID,
                MEALS_COL_NAME,
                MEALS_COL_DATE,
                MEALS_COL_TIME,
                MEALS_COL_SIZE
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

        List<Meal> meals = new ArrayList();
        while(cursor.moveToNext()) {
            Meal m = new Meal(cursor.getInt(cursor.getColumnIndexOrThrow(MEALS_COL_ID)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_NAME)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_DATE)),
                              cursor.getString(cursor.getColumnIndexOrThrow(MEALS_COL_TIME)),
                              cursor.getInt(cursor.getColumnIndexOrThrow(MEALS_COL_SIZE))       );
            meals.add(m);
        }
        cursor.close();

        return meals;
    }

    public void updateProduct(Product p) {
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

    public List<Product> getProducts(String s) {
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



        List<Product> products = new ArrayList<>();
        while(cursor.moveToNext()) {
            Product p = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCTS_COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PRODUCTS_COL_NAME)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(PRODUCTS_COL_PROT)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(PRODUCTS_COL_FAT)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(PRODUCTS_COL_CARB)));
            products.add(p);
        }
        cursor.close();

        return products;
    }
}
