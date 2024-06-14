package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensermanager.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class ExpenseViewActivity extends AppCompatActivity {

    private EditText categoryEditText;
    private EditText descriptionEditText;
    private EditText amountEditText;
    private TextView resultTextView;
    private Button addButton;
    private DatabaseHelper dbHelper;

    private Button deleteButton;

    //ArrayLists for displaying the data in the recyclerView

    ArrayList<String> id, category, description, amount;
    CustomAdapter adapter;

    ActivityMainBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
/*
        categoryEditText = findViewById(R.id.categoryEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        resultTextView = findViewById(R.id.resultTextView);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.button);

        dbHelper = new DatabaseHelper(this);


        addButton.setOnClickListener(view -> {
            String category = categoryEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            double amount = Double.parseDouble(amountEditText.getText().toString());

            dbHelper.insertData(dbHelper, category, description, amount);

            // Display the inserted data
            String result = "Category: " + category + "\nDescription: " + description + "\nAmount: " + amount;
            resultTextView.setText(result);
        });

        deleteButton.setOnClickListener(view -> {


            dbHelper.deleteData(dbHelper, "2");

        });*/


        dbHelper = new DatabaseHelper(ExpenseViewActivity.this);
        id = new ArrayList<>();
        category = new ArrayList<>();
        description = new ArrayList<>();
        amount = new ArrayList<>();

        storeDataInArrayLists();

        adapter = new CustomAdapter(ExpenseViewActivity.this, id, category, description, amount);
        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(ExpenseViewActivity.this));



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