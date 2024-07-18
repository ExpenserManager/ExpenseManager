package com.example.expensermanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensermanager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateDataActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private DatabaseHelper dbHelper;
    String description;
    String amount;
    String id;
    String date;
    DatePicker datePicker;
    EditText dateFieldText;
    ImageButton deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_update_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DatabaseHelper(UpdateDataActivity.this);
        EditText descriptionField = findViewById(R.id.description);
        EditText amountField = findViewById(R.id.amount);
        Button updateButton = findViewById(R.id.update_button);
        EditText dateField = findViewById(R.id.date);

        Spinner spinner = findViewById(R.id.spinner2);

        ArrayList<String> spinnerList = dbHelper.getAllCategories();
        spinner.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerList)); //show categories in spinner



        Intent intent = getIntent();
        if(intent != null){
                description = intent.getStringExtra("description");
                amount = intent.getStringExtra("amount");
                id = intent.getStringExtra("id");
                date = intent.getStringExtra("date");

           descriptionField.setText(description);
           amountField.setText(amount);
           dateField.setText(date);
        }



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescription = descriptionField.getText().toString();
                String newAmount = amountField.getText().toString();
                String newDate = dateField.getText().toString();
                dbHelper.updateData(id, newDescription, newAmount, newDate, "expense_manager"); //update database

              //give data back to ExpenseViewActivity
                Intent goBackIntent = new Intent();
                goBackIntent.putExtra("id", id);
                goBackIntent.putExtra("description", newDescription);
                goBackIntent.putExtra("amount", newAmount);
                goBackIntent.putExtra("date", newDate);
                setResult(RESULT_OK, goBackIntent);
                finish();

            }
        });


        dateFieldText = findViewById(R.id.date);
        dateFieldText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateDataActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateFieldText.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                            }
                        },year,month, day);

                datePickerDialog.show();

            }
        });

        deleteButton =  findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteData(dbHelper, id, "expense_manager" );
                Intent intent = new Intent();
                intent.putExtra("deleteID", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });




    }
}