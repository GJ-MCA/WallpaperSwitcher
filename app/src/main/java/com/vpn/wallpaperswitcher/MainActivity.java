package com.vpn.wallpaperswitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnvMenu = findViewById(R.id.bnvMenu);

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragContainer, new HomeFragment()).commit();

        bnvMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()) {

                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.menu_settings:
                        fragment = new SettingsFragment();
                        break;

                    default:
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragContainer, fragment).commit();

                return true;
            }
        });
    }
}