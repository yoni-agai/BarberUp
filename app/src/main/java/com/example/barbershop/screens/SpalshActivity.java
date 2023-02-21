package com.example.barbershop.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.repositories.UserRepo;
import com.example.barbershop.screens.loginSignup.LoginActivity;
import com.example.barbershop.screens.main.MainActivity;

public class SpalshActivity extends AppCompatActivity {

    private final UserRepo mRepo = BaseApplication.getUserRepo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        if (mRepo.isUserLoggedIn()) {
            navigateToMain();
        } else {
            navigateToLogin();
        }
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
}