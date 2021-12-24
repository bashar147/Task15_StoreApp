package com.example.task15.Classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.task15.Fragments.ForgetPasswordFragment;
import com.example.task15.Fragments.LoginFragment;
import com.example.task15.Fragments.SignUpFragment;
import com.example.task15.MainActivity;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerAdapter
        extends FragmentPagerAdapter {

    String title1 , title2 , title3;
    public ViewPagerAdapter(
            @NonNull FragmentManager fm , String title1,String title2 , String title3)
    {
        super(fm);
        this.title1 = title1;
        this.title2 = title2;
        this.title3 = title3;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = new LoginFragment();
        }
        else if (position == 1){
            fragment = new LoginFragment();
            }
        else if (position == 2) {
            fragment = new SignUpFragment();

        }
       return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
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
        return title;
    }
}
