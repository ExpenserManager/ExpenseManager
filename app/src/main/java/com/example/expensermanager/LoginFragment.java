package com.example.expensermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.expensermanager.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    // interface to communicate with activity
    public interface OnFragmentClosedListener{
        void onFragmentClosed();
    }

    private FragmentLoginBinding binding;
    private OnFragmentClosedListener fragmentClosedListener;

    // set callback listener
    public void setOnFragmentClosedListener(OnFragmentClosedListener listener) {
        this.fragmentClosedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.usernameEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();

                if (validateCredentials(username, password)) {
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
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

    private boolean validateCredentials(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }
}
