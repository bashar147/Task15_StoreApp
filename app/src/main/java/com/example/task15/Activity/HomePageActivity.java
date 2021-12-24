package com.example.task15.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.example.task15.Fragments.BottomNavFragments.BottomBookFragment;
import com.example.task15.Fragments.BottomNavFragments.BottomHomeFragment;
import com.example.task15.Fragments.BottomNavFragments.BottomNoteficationFragment;
import com.example.task15.Fragments.BottomNavFragments.BottomPersonFragment;
import com.example.task15.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class HomePageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    AnimatedBottomBar bottomNavigationView;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    BottomHomeFragment firstFragment = new BottomHomeFragment();
    BottomBookFragment secondFragment = new BottomBookFragment();
    BottomPersonFragment thirdFragment = new BottomPersonFragment();
    BottomNoteficationFragment forthFragment = new BottomNoteficationFragment();
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        replaceFragment(new BottomHomeFragment());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomePageActivity.this);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                switch (i1) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, forthFragment).commit();
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

//        if (ActivityCompat.checkSelfPermission(HomePageActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            getLocation();
//        }else {
//            ActivityCompat.requestPermissions(HomePageActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bot_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                break;
            case R.id.bot_nav_book:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                break;
            case R.id.bot_nav_person:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                break;
            case R.id.bot_nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, forthFragment).commit();
                return true;
        }
        return false;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}