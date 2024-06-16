package com.example.expensermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensermanager.databinding.ActivityMainBinding;

public class UpdateDataActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private DatabaseHelper dbHelper;
    String description;
    String amount;
    String id;
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
        EditText descriptionField = findViewById(R.id.description);
        EditText amountField = findViewById(R.id.amount);
        Button updateButton = findViewById(R.id.update_button);


        Intent intent = getIntent();
        if(intent != null){
                description = intent.getStringExtra("description");
                amount = intent.getStringExtra("amount");
                id = intent.getStringExtra("id");

           descriptionField.setText(description);
           amountField.setText(amount);
        }


        dbHelper = new DatabaseHelper(UpdateDataActivity.this);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescription = descriptionField.getText().toString();
                String newAmount = amountField.getText().toString();
                dbHelper.updateData(id, newDescription, newAmount); //update database

              //give data back to ExpenseViewActivity
                Intent goBackIntent = new Intent();
                goBackIntent.putExtra("id", id);
                goBackIntent.putExtra("description", newDescription);
                goBackIntent.putExtra("amount", newAmount);
                setResult(RESULT_OK, goBackIntent);
                finish();

            }
        });



    }
}