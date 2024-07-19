package com.example.expensermanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensermanager.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

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

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Username, Email and Password are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if passwords are equal
                if (!password.equals(confirmPassword)) {
                    binding.passwordEditText.setText("");
                    binding.passwordConfirmEditText.setText("");

                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                } else {
                    boolean isInserted = dbHelper.insertUserData(username, password);
                    if (isInserted) {
                        Toast.makeText(SignUpActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                        showWelcomeDialog(username);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                        binding.usernameEditText.setText("");
                    }
                }
            }
        });
    }

    private void showWelcomeDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome");
        builder.setMessage("Welcome to the Expense Manager " + username + "!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SignUpActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
