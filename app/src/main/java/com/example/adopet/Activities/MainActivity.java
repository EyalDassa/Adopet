package com.example.adopet.Activities;

import static com.example.adopet.Utilities.DbManager.loadUserDetails;
import static com.example.adopet.Utilities.DbManager.sortPets;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.adopet.Fragments.HomeFragment;
import com.example.adopet.Fragments.UserFragment;
import com.example.adopet.R;
import com.example.adopet.Utilities.DbManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView main_NAV_bottom;
    private HomeFragment fragmentHome;
    private UserFragment fragmentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_NAV_bottom = findViewById(R.id.main_NAV_bottom);
        fragmentHome = new HomeFragment();
        loadUserDetails(user -> {
            getSupportFragmentManager().beginTransaction().add(R.id.main_LAY_page, fragmentHome).commit();
        });
        fragmentUser = new UserFragment();
        DbManager.getInstance().sortCategories();
        sortPets();

        main_NAV_bottom.setItemActiveIndicatorColor(getColorStateList(R.color.colorBackgroundLight));

        main_NAV_bottom.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                selectedFragment = fragmentHome;
            }
            else if (itemId == R.id.user) {
                selectedFragment = fragmentUser;
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