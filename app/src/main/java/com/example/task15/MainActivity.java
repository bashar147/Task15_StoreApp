package com.example.task15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.task15.Activity.HomePageActivity;
import com.example.task15.Classes.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static ViewPager viewPager ,forgetPAger;
    public static Context contextOfApplication;

    ViewPagerAdapter viewPagerAdapter ;

    public static TabLayout tabLayout;
    public static TabLayout forgetTab;

    public static int FORGET_PASS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forgetPAger = findViewById(R.id.forgetPager);
        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        forgetTab = findViewById(R.id.forget_tab_layout);
        contextOfApplication = getApplicationContext();
        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager()
        ,"Login","","SignUp");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        else {

        }
    }

}