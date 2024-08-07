package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    ArrayList<String> imagePath;
    CustomAdapter adapter;
    ActivityMainBinding binding;
    RecyclerView recyclerView;
    private Animation scaleAnimation;
    private Animation bounceAnimation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scaleAnimation = AnimationUtils.loadAnimation(this, R.transition.scale);
        bounceAnimation = AnimationUtils.loadAnimation(this, R.transition.bounce);

        dbHelper = new DatabaseHelper(this);

        id = new ArrayList<>();
        category = new ArrayList<>();
        description = new ArrayList<>();
        amount = new ArrayList<>();
        date = new ArrayList<>();
        imagePath = new ArrayList<>();

        binding.searchView.clearFocus(); //cursor is in search view - only by clicking on it
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.cardView.setVisibility(View.GONE);
                binding.spinner.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.cardView.setVisibility(View.GONE);
                binding.spinner.setVisibility(View.GONE);
                filterList(newText, "description", null);
                return true;
            }
        });
        binding.searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.cardView.setVisibility(View.GONE);
                binding.spinner.setVisibility(View.GONE);
            } else {
                if (binding.searchView.getQuery().toString().isEmpty()) {
                    binding.cardView.setVisibility(View.VISIBLE);
                    binding.spinner.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView = findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseViewActivity.this));
        adapter = new CustomAdapter(ExpenseViewActivity.this, id, category, description, amount,date,imagePath,dbHelper);
        recyclerView.setAdapter(adapter);

        double total = calculateCurrentBalance();
        binding.currentBalanceMoney.setText("" + total);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayList spinnerList = storeCategories();
        spinnerList.add(0, "nothing selected");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(spinnerAdapter); //show categories in spinner

        Intent intent = getIntent();
        String selectedCategory = intent.getStringExtra("selectedCategory");
        if (selectedCategory != null) {
            //spinner gets selectedCategory
            int position = spinnerAdapter.getPosition(selectedCategory);
            spinner.setSelection(position);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String c = (String) spinnerList.get(position);
                filterList(null, "category", c);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Double d = dbHelper.totalAmountCategory("Lebensmittel");

        doAnimation(binding.backButton);
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseViewActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void doAnimation(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startAnimation(scaleAnimation);
                    break;
                case MotionEvent.ACTION_UP:
                    v.startAnimation(bounceAnimation);
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onResume() { //update date if user comes back to this activity
        super.onResume();
        storeDataInArrayLists();
        adapter.notifyDataSetChanged();
        double total = calculateCurrentBalance();
        binding.currentBalanceMoney.setText("" + total);
    }


    //method to filter the data with the spinner
    private void filterList(String newText, String type, String categoryFilter) {

       ArrayList<String> filteredListDescription = new ArrayList<>();
       ArrayList<String> filteredListId = new ArrayList<>();
       ArrayList<String> filteredListAmount = new ArrayList<>();
       ArrayList<String> filteredListDate = new ArrayList<>();
       ArrayList<String> filteredListImagePath = new ArrayList<>(); // adding the image path to guarantee that the same photo is shown either when "nothing selected" or specific category

       if(type.equals("description")) {
           for (int i = 0; i < description.size(); i++) {
               if (description.get(i).toLowerCase().contains(newText.toLowerCase())) {
                   filteredListDescription.add(description.get(i));
                   filteredListId.add(id.get(i));
                   filteredListAmount.add(amount.get(i));
                   filteredListDate.add(date.get(i));
                   filteredListImagePath.add(imagePath.get(i));
               }
           }
       }else if (type.equals("category")){
         Cursor cursor = dbHelper.filterDatabaseCategory(categoryFilter);
         Double categorySum = dbHelper.totalAmountCategory(categoryFilter);
         binding.currentBalanceMoney.setText(categorySum.toString());

           if(categoryFilter.equals("nothing selected")){
               // Reload all data
               filteredListDescription.addAll(description);
               filteredListId.addAll(id);
               filteredListAmount.addAll(amount);
               filteredListDate.addAll(date);
               filteredListImagePath.addAll(imagePath);

               adapter.setFilteredList(description, id, amount, date, imagePath);
               Double total = calculateCurrentBalance();
               binding.currentBalanceMoney.setText(total.toString());
               return;
           }

           if(cursor != null){
               while(cursor.moveToNext()){
                   String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                   String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                   String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                   String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                   String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                   String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));

                   filteredListId.add(id);
                   filteredListDescription.add(description);
                   filteredListAmount.add(amount);
                   filteredListDate.add(date);
                   filteredListImagePath.add(imagePath);
               }
               cursor.close();
           }
       }
        if(filteredListDescription.isEmpty()){
            Toast.makeText(this, "No items found!", Toast.LENGTH_LONG).show();
        }else{
            adapter.setFilteredList(filteredListDescription, filteredListId, filteredListAmount, filteredListDate, filteredListImagePath);
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
        String currentImagePath = "";

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            updatedDescription= data.getStringExtra("description");
            updatedAmount = data.getStringExtra("amount");
            givenID = data.getStringExtra("id");
            updateDate = data.getStringExtra("date");
            currentImagePath = data.getStringExtra("image_path");

            int position = id.indexOf(givenID); //database id starts with 1, listID starts with 0
            if (position != -1) {
                description.set(position, updatedDescription);
                amount.set(position, updatedAmount);
                date.set(position, updateDate);
                imagePath.set(position,currentImagePath);
                adapter.notifyItemChanged(position);
            }
            // Reload data after update
            storeDataInArrayLists();
            filterList(null, "category", (String) binding.spinner.getSelectedItem());
        }
    }

    protected void storeDataInArrayLists(){
        id.clear();
        category.clear();
        description.clear();
        amount.clear();
        date.clear();
        imagePath.clear();

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
                imagePath.add(cursor.getString(5));
            }
        }
    }

    //method to get the currentBalance which is shown in the "total" field
    public double calculateCurrentBalance(){
        double sum = 0;
        if(!amount.isEmpty()){
            for(int i = 0; i < amount.size(); i++){
                sum+= Integer.parseInt(amount.get(i));
            }
        }
        return sum;
    }

    //store all existing categories in an ArrayList
    public ArrayList<String> storeCategories(){
        ArrayList<String> allCategories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("category_table", new String[]{"category"}, null, null, null, null, null);

        if(cursor != null){
            while(cursor.moveToNext()){
                allCategories.add(cursor.getString(0));
            }
            cursor.close();
        }
        return allCategories;
    }
}