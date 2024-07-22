package com.example.expensermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensermanager.databinding.ActivityHomeScreenBinding;


public class HomeScreenActivity extends AppCompatActivity {


    private ActivityHomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(binding.fragmentContainerViewBarchart.getId(), BarchartFragment.class, null)
                    .commit();
        }

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });



    }
}