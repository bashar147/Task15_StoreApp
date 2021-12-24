package com.example.task15.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.task15.Fragments.ForgetPasswordFragment;
import com.example.task15.Fragments.LoginFragment;
import com.example.task15.Fragments.SignUpFragment;

public class HomePageTabAdapter extends FragmentPagerAdapter {

    String title1 , title2 , title3 , title4;
    public HomePageTabAdapter(
            @NonNull FragmentManager fm , String title1, String title2 , String title3 , String title4)
    {
        super(fm);
        this.title1 = title1;
        this.title2 = title2;
        this.title3 = title3;
        this.title4 = title4;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = new LoginFragment();
        }
        else if (position == 1){
            fragment = new ForgetPasswordFragment();
        }
        else if (position == 2) {
            fragment = new SignUpFragment();
            }
        else if (position == 3) {
            fragment = new SignUpFragment();
                 }
            return fragment;
    }

    @Override
    public int getCount()
    {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = null;
        if (position == 0)
            title = title1;
        else if (position == 1){
            title = title2;}
        else if (position == 2)
            title = title3;
        else if (position == 3)
            title = title4;
        return title;
    }
}

