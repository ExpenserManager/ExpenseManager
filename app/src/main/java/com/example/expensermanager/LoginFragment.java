package com.example.expensermanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.expensermanager.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    // interface to communicate with activity
    public interface OnFragmentClosedListener {
        void onFragmentClosed();
    }

    private FragmentLoginBinding binding;
    private OnFragmentClosedListener fragmentClosedListener;
    private DatabaseHelper dbHelper;

    // set callback listener
    public void setOnFragmentClosedListener(OnFragmentClosedListener listener) {
        this.fragmentClosedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbHelper = new DatabaseHelper(getContext());

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        binding.loginButton.setEnabled(false);
        binding.loginButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darker_gray));

        binding.usernameEditText.addTextChangedListener(textWatcher);
        binding.passwordEditText.addTextChangedListener(textWatcher);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.usernameEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();

                if (!validateUsername(username)) {
                    Toast.makeText(getContext(), "Username not found", Toast.LENGTH_SHORT).show();
                    binding.usernameEditText.setText("");
                    binding.passwordEditText.setText("");
                } else if (!validatePassword(username, password)) {
                    Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                    binding.passwordEditText.setText("");
                } else {
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), HomeScreenActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    private void closeFragment() {
        if (fragmentClosedListener != null) {
            fragmentClosedListener.onFragmentClosed();
        }
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    private boolean validateUsername(String username) {
        return dbHelper.isUsernameExists(username);
    }

    private boolean validatePassword(String username, String password) {
        return dbHelper.isPasswordCorrect(username, password);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String username = binding.usernameEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            boolean enableButton = !username.isEmpty() && !password.isEmpty();
            binding.loginButton.setEnabled(enableButton);

            if (enableButton) {
                binding.loginButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
            } else {
                binding.loginButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darker_gray));
            }
        }
    };
}
