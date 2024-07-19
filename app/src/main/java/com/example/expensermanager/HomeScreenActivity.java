package com.example.expensermanager;

import android.os.Bundle;

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
    }
}