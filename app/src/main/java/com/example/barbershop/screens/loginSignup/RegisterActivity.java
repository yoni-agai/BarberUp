package com.example.barbershop.screens.loginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.repositories.UserRepo;
import com.example.barbershop.interfaces.ILoginCallback;
import com.example.barbershop.screens.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    UserRepo mRepo = BaseApplication.getUserRepo();
    TextInputEditText mEditTextEmail, mEditTextFullName, mEditTextPassword, mEditTextConfirmPassword;
    Button mRegisterBtn;
    ProgressBar mProgressBar;
    TextView mClickToLogin;
    String mEmail, mFullName, mPassword, mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

        mClickToLogin.setOnClickListener(v -> {
            navigateToLogin();
        });

        mRegisterBtn.setOnClickListener(v -> {
            mProgressBar.setVisibility(View.VISIBLE);
            extractUserInput();

            if (!isEmptyFields(mEmail, mFullName, mPassword, mConfirmPassword)) {
                return;
            }

            mProgressBar.setVisibility(View.GONE);
            mRepo.performRegister(mEmail, mPassword, mFullName, new ILoginCallback() {

                @Override
                public void onSuccess() {
                    Toast.makeText(RegisterActivity.this, "Account Created.",
                            Toast.LENGTH_SHORT).show();
                    navigateToMain();
                }

                @Override
                public void onError() {
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void initViews() {
        this.mEditTextEmail = findViewById(R.id.email);
        this.mEditTextFullName = findViewById(R.id.fullName);
        this.mEditTextPassword = findViewById(R.id.password);
        this.mEditTextConfirmPassword = findViewById(R.id.confirm_password);
        this.mProgressBar = findViewById(R.id.progress_bar);
        this.mRegisterBtn = findViewById(R.id.btn_register);
        this.mClickToLogin = findViewById(R.id.login_now);
    }

    private void extractUserInput() {
        mEmail = String.valueOf(mEditTextEmail.getText());
        mFullName = String.valueOf(mEditTextFullName.getText());
        mPassword = String.valueOf(mEditTextPassword.getText());
        mConfirmPassword = String.valueOf(mEditTextConfirmPassword.getText());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validPassword(String password,String confirmPassword) {
        return password.trim().equals(confirmPassword.trim());
    }

    private boolean isEmptyFields(String email, String fullName, String password, String confirmPassword) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(RegisterActivity.this, "Enter Fullname", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!validPassword(password,confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}