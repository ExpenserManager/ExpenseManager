package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.expensermanager.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExpenseViewActivity extends AppCompatActivity {

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    private DatabaseHelper dbHelper;

    //ArrayLists for displaying the data in the recyclerView
    ArrayList<String> id;

    public ArrayList<String> getCategory() {
        return category;
    }

    ArrayList<String> category;
    ArrayList<String> description;
    ArrayList<String> amount;
    ArrayList<String> date;
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



//      //inserting data to test the recycler view
        //dbHelper.insertData(dbHelper, "Lebensmittel", "TEst", 100.0, "18/06/24", "expense_manager");
//        dbHelper.insertData(dbHelper, "Gesundheit", "Apotheke", 30.0, "19/06/24", "expense_manager");
//        dbHelper.insertData(dbHelper, "Tierarzt", "Katze", 50.0, "10/04/24", "expense_manager");
//
//        dbHelper.insertCategory(dbHelper, "Lebensmittel", "red", "category_table");
//        dbHelper.insertCategory(dbHelper, "Gesundheit", "blue", "category_table");




        id = new ArrayList<>();
        category = new ArrayList<>();
        description = new ArrayList<>();
        amount = new ArrayList<>();
        date = new ArrayList<>();

        binding.searchView.clearFocus(); //cursor is in search view - only by clicking on it
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, "description", null);
                return true;
            }
        });

        storeDataInArrayLists();

        recyclerView = findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseViewActivity.this));
        adapter = new CustomAdapter(ExpenseViewActivity.this, id, category, description, amount,date, dbHelper);
        recyclerView.setAdapter(adapter);

        double total = calculateCurrentBalance();
        binding.currentBalanceMoney.setText("" + total);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayList spinnerList = category;
        spinnerList.add(0,"nothing selected");
        spinner.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,spinnerList)); //show categories in spinner

        //onItemSelectedListener

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String c = category.get(position);
                filterList(null, "category", c);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //test
        binding.buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseViewActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() { //update date if user comes back to this activity
        super.onResume();
        storeDataInArrayLists();
        adapter.notifyDataSetChanged();
    }

    private void filterList(String newText, String type, String categoryFilter) {
       ArrayList<String> filteredListDescription = new ArrayList<>();
       ArrayList<String> filteredListId = new ArrayList<>();
       ArrayList<String> filteredListAmount = new ArrayList<>();
       ArrayList<String> filteredListDate = new ArrayList<>();

       if(type.equals("description")) {
           for (int i = 0; i < description.size(); i++) {
               if (description.get(i).toLowerCase().contains(newText.toLowerCase())) {
                   filteredListDescription.add(description.get(i));
                   filteredListId.add(id.get(i));
                   filteredListAmount.add(amount.get(i));
                   filteredListDate.add(date.get(i));
               }
           }
       }else if (type.equals("category")){
           Cursor cursor = dbHelper.filterDatabaseCategory(categoryFilter);

           if(categoryFilter.equals("nothing selected")){
               storeDataInArrayLists();
               adapter.setFilteredList(description, id, amount, date);
               return;
           }

           if(cursor != null){
               while(cursor.moveToNext()){
                   String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                   String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                   String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                   String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                   String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                   filteredListId.add(id);
                   filteredListDescription.add(description);
                   filteredListAmount.add(amount);
                   filteredListDate.add(date);
               }
               cursor.close();
           }
       }
        if(filteredListDescription.isEmpty() && !categoryFilter.equals("nothing selected")){
            Toast.makeText(this, "No items found!", Toast.LENGTH_LONG).show();
        }else{
            adapter.setFilteredList(filteredListDescription, filteredListId, filteredListAmount, filteredListDate);
        }
    }

    //onActivityResult -> implementation from ChatGPT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String updatedDescription = "";
        String updatedAmount = "";
        String givenID = "";
        String updateDate = "";

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            if(data != null) {
               updatedDescription= data.getStringExtra("description");
               updatedAmount = data.getStringExtra("amount");
               givenID = data.getStringExtra("id");
               updateDate = data.getStringExtra("date");
            }
            int position = id.indexOf(givenID); //database id starts with 1, listID starts with 0
            if (position != -1) {
                description.set(position, updatedDescription);
                amount.set(position, updatedAmount);
                date.set(position, updateDate);
                adapter.notifyItemChanged(position);
            }
        }
    }

    protected void storeDataInArrayLists(){
        Cursor cursor = dbHelper.readAllData("expense_manager");
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data found!", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                category.add(cursor.getString(1));
                description.add(cursor.getString(2));
                amount.add(cursor.getString(3));
                date.add(cursor.getString(4));
            }
        }
    }

    public double calculateCurrentBalance(){
        double sum = 0;

        if(!amount.isEmpty()){
            for(int i = 0; i < amount.size(); i++){
                sum+= Integer.parseInt(amount.get(i));
            }
        }
        return sum;
    }





}