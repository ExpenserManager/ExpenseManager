package com.example.expensermanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

   private static final String DB_NAME = "expenser_manager.db";
   private static final int DB_VERSION = 1;
   private static final String TABLE_NAME = "expense_manager";

   //columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";

    //static queries for deleting database
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS" + TABLE_NAME;


    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryCreate =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " PRIMARY KEY AUTOINCREMENT ," +
                        COLUMN_CATEGORY + "TEXT," + COLUMN_DESCRIPTION +
                        "TEXT" + COLUMN_AMOUNT + "INTEGER)";
        db.execSQL(queryCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
