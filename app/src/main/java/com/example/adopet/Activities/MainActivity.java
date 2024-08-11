package com.example.adopet.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.adopet.Fragments.HomeFragment;
import com.example.adopet.Fragments.UserFragment;
import com.example.adopet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView main_NAV_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_NAV_bottom = findViewById(R.id.main_NAV_bottom);
        HomeFragment fragmentHome = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_LAY_page, fragmentHome)
                .commit();


        main_NAV_bottom.setItemActiveIndicatorColor(getColorStateList(R.color.colorBackgroundLight));

        main_NAV_bottom.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                selectedFragment = new HomeFragment();
            }
            else if (itemId == R.id.user) {
                selectedFragment = new UserFragment();
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_LAY_page, selectedFragment)
                        .commit();
            }


            return true;
        });

        main_NAV_bottom.setOnItemReselectedListener(item -> {
            // DO NOTHING IF RESELECTED
        });

    }
}