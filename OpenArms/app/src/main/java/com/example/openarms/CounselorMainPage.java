package com.example.openarms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CounselorMainPage extends AppCompatActivity {
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main_page);
        bottomNav = findViewById(R.id.bottom_navigation);
        Fragment initialSelectedFragment = new StudentHomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                initialSelectedFragment).commit();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new CounselorHomeFragment();
                        break;
                    case R.id.nav_community:
                        selectedFragment = new CounselorCommunityFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new CounselorProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,
                        selectedFragment).commit();
                return true;
            }
        });
    }
}