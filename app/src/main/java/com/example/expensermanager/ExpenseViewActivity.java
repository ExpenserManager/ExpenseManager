package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

       /* //inserting data to test the recycler view
        dbHelper.insertData(dbHelper, "categoryexample1", "test1", 40.0);
        dbHelper.insertData(dbHelper, "categoryexample2", "test2", 40.0);
        dbHelper.insertData(dbHelper, "categoryexample3", "test3", 40.0);*/

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

    //onActivityResult -> implementation from ChatGPT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String updatedDescription = "";
        String updatedAmount = "";
        String givenID = "";

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            if(data != null) {
               updatedDescription= data.getStringExtra("description");
               updatedAmount = data.getStringExtra("amount");
               givenID = data.getStringExtra("id");
            }
            int position = id.indexOf(givenID); //database id starts with 1, listID starts with 0
            if (position != -1) {
                description.set(position, updatedDescription);
                amount.set(position, updatedAmount);
                adapter.notifyItemChanged(position);
            }
        }
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