package com.example.expensermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensermanager.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.usernameEditText.getText().toString();
                String email = binding.emailEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();
                String confirmPassword = binding.passwordConfirmEditText.getText().toString();

                // check if passwords are equal
                if (!password.equals(confirmPassword)) {
                    binding.passwordEditText.setText("");
                    binding.passwordConfirmEditText.setText("");

                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                } else {
                    // TODO: save in db
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
