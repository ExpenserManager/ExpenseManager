package com.example.expensermanager;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensermanager.databinding.ActivityStartBinding;

import jp.wasabeef.blurry.Blurry;

public class StartActivity extends AppCompatActivity implements LoginFragment.OnFragmentClosedListener {
    private ActivityStartBinding binding;

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
    }

    // when the fragment is being closed, the blur should be removed
    @Override
    public void onFragmentClosed() {
        Blurry.delete(binding.start);

        binding.loginButton.setVisibility(View.VISIBLE);
        binding.titleTextView.setVisibility(View.VISIBLE);
        binding.signupButton.setVisibility(View.VISIBLE);
    }
}
