package com.example.expensermanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "expenser_manager.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "expense_manager";

    //columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";

    private static final String TABLE2_NAME = "category_table";
    private static final String COLUMN_ID_CATEGORY_TABLE = "id";
    private static final String COLUMN_CATEGORY_CATEGORY_TABLE = "category";
    private static final String COLUMN_COLOR_CATEGORY_TABLE = "color";

    //static queries for deleting database
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_CATEGORY_TABLE =
            "DROP TABLE IF EXISTS " + TABLE2_NAME;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryCreate = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT, %s REAL NOT NULL, %s TEXT);", TABLE_NAME, COLUMN_ID, COLUMN_CATEGORY, COLUMN_DESCRIPTION, COLUMN_AMOUNT, COLUMN_DATE);
        //
        String queryCreateCategoryTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT);", TABLE2_NAME, COLUMN_ID_CATEGORY_TABLE, COLUMN_CATEGORY_CATEGORY_TABLE, COLUMN_COLOR_CATEGORY_TABLE);

        db.execSQL(queryCreate); //executing the query - create database
        //
        db.execSQL(queryCreateCategoryTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES_CATEGORY_TABLE);
        db.execSQL(SQL_DELETE_ENTRIES);
        //if it is required to upgrade the database --> rebuild the database
        //updating: add column or row
    }


    public void insertData(DatabaseHelper dbHelper, String category, String description, double amount, String date, String tableName) {

        //data repository gets in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create map of values - column names are the keys of the map elements
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(String.valueOf(COLUMN_AMOUNT), amount);
        values.put(COLUMN_DATE, date);


        //null if the ContentnValues map is empty - no insertion
        long newRowId = db.insert(tableName, null, values);

        //db.insert() returns -1 if the insertion has failed
        if (newRowId == -1) {
            Toast.makeText(context, "insertion failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show();

        }
    }

    public void insertCategory(DatabaseHelper dbHelper, String categoryName, String color, String tableName) {

        //data repository gets in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_CATEGORY_TABLE, categoryName);
        values.put(COLUMN_COLOR_CATEGORY_TABLE, color);

        long newRowId = db.insert(tableName, null, values);
        if (newRowId == -1) {
            Toast.makeText(context, "insertion failed", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT);
        }
    }


    public void deleteData(DatabaseHelper dbHelper, String id, String tableName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + tableName + " WHERE _id=" + id; // SQL Query - deleting row via id
        db.execSQL(query);

    }

    public void updateData(String id, String description, String amount, String date, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        db.update(tableName, values, "_id=?", new String[]{id});

    }

    public Cursor filterDatabaseCategory(String filter){
        SQLiteDatabase db =  this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATEGORY + " = ?";
        return db.rawQuery(query, new String[]{filter});
    }


    Cursor readAllData(String tableName) {
        String query = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null); //execute query
        }

        return cursor;
        //cursor contains all database data

    }


}
