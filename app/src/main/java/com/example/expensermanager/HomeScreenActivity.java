package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.expensermanager.databinding.ActivityHomeScreenBinding;

public class HomeScreenActivity extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;
    private Animation scaleAnimation;
    private Animation bounceAnimation;

    DatabaseHelper dbHelper;

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
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
        // values in the xmls from ChatGPT
        scaleAnimation = AnimationUtils.loadAnimation(this, R.transition.scale);
        bounceAnimation = AnimationUtils.loadAnimation(this, R.transition.bounce);

        doAnimation(binding.addButton);
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

        doAnimation(binding.qrButton);
        binding.qrButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(HomeScreenActivity.this, "QR Button Clicked", Toast.LENGTH_SHORT).show();
                Log.d("HomeScreenActivity", "QR Button Clicked");
                Intent intent = new Intent(HomeScreenActivity.this, QR_Reader.class);
                startActivity(intent);
            }
        });
        doAnimation(binding.calendarButton);
        binding.calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to calendarView
            }
        });

        dbHelper = new DatabaseHelper(this);
        Double total = dbHelper.calculateTotal();
        String totalText = "-" + total.toString();
        binding.currentBalanceMoney.setText(totalText);

        doAnimation(binding.cardView);
        binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ExpenseViewActivity.class);
                startActivity(intent);
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
}
