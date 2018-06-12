package com.example.laza.afinal.Classes.AdapterClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.laza.afinal.Activities.AuthActivity.RegisterFragment;
import com.example.laza.afinal.Activities.AuthActivity.SignInFragment;


/**
 * Created by LAZA on 10/23/2017.
 */

public class SignInRegisterPageAdapter extends FragmentPagerAdapter {

    private static int num = 2;

    public SignInRegisterPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SignInFragment.newInstance(position);
            default:
                return RegisterFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
