package com.example.task15.Classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.task15.Fragments.ForgetPasswordFragment;
import com.example.task15.Fragments.LoginFragment;
import com.example.task15.Fragments.SignUpFragment;

public class ViewPagerAdapter2 extends FragmentPagerAdapter {

    String title1;
    Context context;
    public ViewPagerAdapter2(
            @NonNull FragmentManager fm, String title1 ,Context context) {
        super(fm);
    this.title1 = title1;
    this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.println(Log.ASSERT,"Here","inisde Fragment Adapter2");
        Toast.makeText(context, "position= "+position, Toast.LENGTH_SHORT).show();
        Fragment fragment = null;
        if (position == 0)
            fragment = new ForgetPasswordFragment();
        else
            fragment = new LoginFragment();

        notifyDataSetChanged();
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0) {
            title = title1;
        }
        return title;
    }
}
