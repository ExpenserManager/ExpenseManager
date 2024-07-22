package com.example.expensermanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensermanager.databinding.ActivityStartBinding;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class StartActivity extends AppCompatActivity implements LoginFragment.OnFragmentClosedListener {
    private ActivityStartBinding binding;
    ArrayList<String> category;
    ArrayList<String> description;
    ArrayList<String> amount;
    ArrayList<String> date;
    ArrayList<String> id;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.loginButton.setOnClickListener(v -> {
            if (savedInstanceState == null) {

                // blur effect for background as soon as login button is pressed
                Blurry.with(this).radius(15)
                        .sampling(2)
                        .onto(binding.start);

                // for a cleaner look
                binding.loginButton.setVisibility(View.INVISIBLE);
                binding.titleTextView.setVisibility(View.INVISIBLE);
                binding.signupButton.setVisibility(View.INVISIBLE);

                LoginFragment loginFragment = new LoginFragment();
                loginFragment.setOnFragmentClosedListener(this);        // place the listener on this fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.signupButton.setOnClickListener(v -> {
           Intent intent = new Intent(this, SignUpActivity.class);
           startActivity(intent);
        });

      dbHelper = new DatabaseHelper(this);

     //inserting default categories if table is empty
        if(isCategoryTableEmpty()) {
            dbHelper.insertCategory(dbHelper, "Gesundheit", "green", "category_table");
            dbHelper.insertCategory(dbHelper, "Lebensmitel", "red", "category_table");
            dbHelper.insertCategory(dbHelper, "Bildung", "blue", "category_table");
            dbHelper.insertCategory(dbHelper, "Freizeit", "yellow", "category_table");
            dbHelper.insertCategory(dbHelper, "Haustier", "black", "category_table");
            dbHelper.insertCategory(dbHelper, "Wohnen", "white", "category_table");
            dbHelper.insertCategory(dbHelper, "Transport", "lightgreen", "category_table");
            dbHelper.insertCategory(dbHelper, "Kleidung", "pink", "category_table");
            dbHelper.insertCategory(dbHelper, "Kosmetik", "pink", "category_table");
        }

        id = new ArrayList<>();
        category = new ArrayList<>();
        description = new ArrayList<>();
        amount = new ArrayList<>();
        date = new ArrayList<>();

    }

    // when the fragment is being closed, the blur should be removed
    @Override
    public void onFragmentClosed() {
        Blurry.delete(binding.start);

        binding.loginButton.setVisibility(View.VISIBLE);
        binding.titleTextView.setVisibility(View.VISIBLE);
        binding.signupButton.setVisibility(View.VISIBLE);
    }

    public boolean isCategoryTableEmpty(){ //check if the database already has categories
   //implementation of this method from ChatGPT
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    Cursor cursor = null;
    boolean isEmpty = true;

    try {
        // get number of datasets
        cursor = db.rawQuery("SELECT COUNT(*) FROM category_table", null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            isEmpty = (count == 0);
        }
    } finally { //d
        if (cursor != null) {
            cursor.close();
        }
    }
    return isEmpty;
    }
}
