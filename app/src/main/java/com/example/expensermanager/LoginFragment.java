package com.example.expensermanager;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.example.expensermanager.databinding.FragmentLoginBinding;

public class LoginFragment extends DialogFragment {

    private FragmentLoginBinding binding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        binding = FragmentLoginBinding.inflate(inflater);

        builder.setView(binding.getRoot());

        Dialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }



        binding.loginButton.setOnClickListener(v -> {
            // Handle login
            String username = binding.usernameEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            if (validateCredentials(username, password)) {
                // Perform login action
                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Dismiss the dialog on successful login
            } else {
                // Show error message
                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });


        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog == null)
            return; // Prevents NullPointerException

        // Disable positive button until input is valid
        binding.loginButton.setEnabled(false);

        // Add TextChangeListener to EditTexts
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.loginButton.setEnabled(validateCredentials(
                        binding.usernameEditText.getText().toString(),
                        binding.passwordEditText.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        binding.usernameEditText.addTextChangedListener(textWatcher);
        binding.passwordEditText.addTextChangedListener(textWatcher);
    }

    private boolean validateCredentials(String username, String password) {
        // Add your validation logic here
        return !username.isEmpty() && !password.isEmpty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
