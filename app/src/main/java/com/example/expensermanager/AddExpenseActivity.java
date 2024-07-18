package com.example.expensermanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensermanager.databinding.ActivityAddExpenseBinding;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    ActivityAddExpenseBinding binding;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        dbHelper = new DatabaseHelper(this);

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                binding.date.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                            }
                        },year,month, day);

                datePickerDialog.show();

            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertFromInput();
            }
        });


    }

    private void insertFromInput(){
        String description = binding.description.getText().toString();
        String value = binding.amount.getText().toString();
        String date = binding.date.getText().toString();
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();

        Log.d("InsertFromInput", "Description: " + description);
        Log.d("InsertFromInput", "Amount: " + value);
        Log.d("InsertFromInput", "Date: " + date);
        dbHelper.insertData(dbHelper, "Lebensmittel", description, Double.parseDouble(value) ,date, "expense_manager");
    }



}