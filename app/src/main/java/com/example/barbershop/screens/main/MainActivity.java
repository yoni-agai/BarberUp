package com.example.barbershop.screens.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.repositories.UserRepo;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    UserRepo mRepo = BaseApplication.getUserRepo();
    NavigationBarView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = findViewById(R.id.bottom_navigation);
        mBottomNav.setOnItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_schedule:
                        selectedFragment = new ScheduleFragment();
                        break;
                    case R.id.nav_info:
                        selectedFragment = new InfoFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };

    public void navigateToScheduleTab() {
        mBottomNav.setSelectedItemId(R.id.nav_schedule);
    }
}