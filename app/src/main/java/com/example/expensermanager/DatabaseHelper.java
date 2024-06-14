package com.example.expensermanager;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
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
    private static final double COLUMN_AMOUNT = 0.0;
    DatabaseHelper dbHelper = new DatabaseHelper(context);

    //static queries for deleting database
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;





    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryCreate =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_CATEGORY + " TEXT NOT NULL, "
                        + COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_AMOUNT + " REAL NOT NULL);";

        db.execSQL(queryCreate); //executing the query - create database

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        //if it is required to upgrade the database --> rebuild the database
        //updating: add column or row
    }


    public void insertData(DatabaseHelper dbHelper, String category, String description, double amount){

        //data repository gets in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Create map of values - column names are the keys of the map elements
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(String.valueOf(COLUMN_AMOUNT), amount);


        //null if the ContentnValues map is empty - no insertion
        long newRowId = db.insert(TABLE_NAME, null, values);

        //db.insert() returns -1 if the insertion has failed
        if(newRowId == -1){
            Toast.makeText(context, "insertion failed", Toast.LENGTH_SHORT);
        }else{
            Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT);

        }



    }

}
