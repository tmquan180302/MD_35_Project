package com.poly.hopa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.poly.hopa.ui.FavoriteFragment;
import com.poly.hopa.ui.HomeFragment;
import com.poly.hopa.ui.SearchFragment;
import com.poly.hopa.ui.UserFragment;


public class MainActivity extends AppCompatActivity {
    ActionBar toolbar;
    private long Pressed;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragment()).commit();
        }
        addEvent();
        addControl();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    if (item.getItemId() == R.id.home) {
                        selectedFragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.search) {
                        selectedFragment = new SearchFragment();
                    } else if (item.getItemId() == R.id.favorite) {
                        selectedFragment = new FavoriteFragment();
                    } else if (item.getItemId() == R.id.user) {
                        selectedFragment = new UserFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    private void addEvent() {
    }

    private void addControl() {
        toolbar = getSupportActionBar();
    }

    @Override
    public void onBackPressed() {
        if (Pressed + 2000 > System.currentTimeMillis()) {
            mToast.cancel();
            moveTaskToBack(true);
        } else {
            mToast = Toast.makeText(getApplicationContext(), "ấn 2 lần để thoát ứng dụng", Toast.LENGTH_SHORT);
            mToast.show();
        }
        Pressed = System.currentTimeMillis();
    }



}