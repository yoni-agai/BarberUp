package com.example.barbershop.screens.loginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.repositories.UserRepo;
import com.example.barbershop.Utils;
import com.example.barbershop.interfaces.ILoginCallback;
import com.example.barbershop.interfaces.ITextValidator;
import com.example.barbershop.screens.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private UserRepo mRepo = BaseApplication.getUserRepo();
    private TextInputEditText mEditTextEmail, mEditTextPassword;
    private Button mLoginBtn;
    private ProgressBar mProgressBar;
    private TextView mRegisterText;
    private String mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        mRegisterText.setOnClickListener(v -> navigateToRegister());

        mLoginBtn.setOnClickListener(v -> {
            mProgressBar.setVisibility(View.VISIBLE);
            mEditTextEmail.setError(null);
            mEditTextPassword.setError(null);

            mEmail = extractEmailInput(error -> {
                mEditTextEmail.setError(error);
            });

            mPassword = extractPasswordInput(error -> {
                mEditTextPassword.setError(error);
            });

            if (mEmail == null || mPassword == null) {
                return;
            }

            performSignIn(mEmail, mPassword);
        });
    }

    private void initViews() {
        this.mEditTextEmail = findViewById(R.id.email);
        this.mEditTextPassword = findViewById(R.id.password);
        this.mLoginBtn = findViewById(R.id.btn_login);
        this.mProgressBar = findViewById(R.id.progress_bar);
        this.mRegisterText = findViewById(R.id.register_now);
    }

    private String extractEmailInput(ITextValidator listener) {
        CharSequence emailInput = mEditTextEmail.getText();

        if (String.valueOf(emailInput).equals("admin1")) {
            return String.valueOf(emailInput);
        }

        if (!Utils.isValidEmail(emailInput)) {
            listener.onError("Email is not valid.");
            return null;
        }

        return String.valueOf(emailInput);
    }

    private String extractPasswordInput(ITextValidator listener) {
        CharSequence passwordInput = mEditTextPassword.getText();

        if (String.valueOf(passwordInput).equals("admin1")) {
            return String.valueOf(passwordInput);
        }

        if (passwordInput == null || !Utils.isValidPassword(passwordInput)) {
            listener.onError("Password should contain at least 8 letters.");
            return null;
        }

        return String.valueOf(mEditTextPassword.getText());
    }

    private void navigateToRegister() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void performSignIn(String email, String password) {
        mProgressBar.setVisibility(View.GONE);
        mRepo.performSignIn(email, password, new ILoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "Login Successful.",
                                Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onError() {
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
            }
        });
    }
}