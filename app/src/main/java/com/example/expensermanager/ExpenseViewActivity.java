package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensermanager.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class ExpenseViewActivity extends AppCompatActivity {

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    private DatabaseHelper dbHelper;

    //ArrayLists for displaying the data in the recyclerView
    ArrayList<String> id, category, description, amount;
    CustomAdapter adapter;

    ActivityMainBinding binding;

    RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        dbHelper.insertData(dbHelper, "categoryexample", "test1", 40.0);
        id = new ArrayList<>();
        category = new ArrayList<>();
        description = new ArrayList<>();
        amount = new ArrayList<>();


        storeDataInArrayLists();

        recyclerView = findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseViewActivity.this));
        adapter = new CustomAdapter(ExpenseViewActivity.this, id, category, description, amount,dbHelper);
        recyclerView.setAdapter(adapter);

    }

    void storeDataInArrayLists(){
        Cursor cursor = dbHelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                category.add(cursor.getString(1));
                description.add(cursor.getString(2));
                amount.add(cursor.getString(3));
            }
        }
    }


}