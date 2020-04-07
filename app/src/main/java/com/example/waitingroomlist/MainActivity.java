package com.example.waitingroomlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waitingroomlist.R;
import com.example.waitingroomlist.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BOTTOM NAVIGATION SETUP/////////////////////////////////////////////////////////////////////////////////
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PatientFragment()).commit();

//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_patients:
//                        Toast.makeText(MainActivity.this, "Patients", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_stats:
//                        Toast.makeText(MainActivity.this, "Stats", Toast.LENGTH_SHORT).show();
//                        Intent intentStats = new Intent(MainActivity.this,StatisticsActivity.class);
//                        intentStats.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        intentStats.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intentStats);
//                        overridePendingTransition(0,0); //0 for no animation
//                        break;
//                    case R.id.action_nearby:
//                        Toast.makeText(MainActivity.this, "Calendar", Toast.LENGTH_SHORT).show();
//                        break;                }
//                return true;
//            }
//        });
        //^BOTTOM NAVIGATION SETUP/////////////////////////////////////////////////////////////////////////////////

        /**
        //TABS LAYOUT SETUP/////////////////////////////////////////////////////////////////////////////////
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Create an adapter that knows which fragment should be shown on each page
        PatientFragmentPagerAdapter adapter = new PatientFragmentPagerAdapter(this,getSupportFragmentManager());
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //^TABS LAYOUT SETUP/////////////////////////////////////////////////////////////////////////////////
         **/



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment= null;

                    switch (menuItem.getItemId()) {
                        case R.id.action_patients:
                            selectedFragment = new PatientFragment();
                            break;
                        case R.id.action_stats:
                            selectedFragment = new StatisticsFragment();
                            break;
                        case R.id.action_nearby:
                            selectedFragment = new PatientFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}
