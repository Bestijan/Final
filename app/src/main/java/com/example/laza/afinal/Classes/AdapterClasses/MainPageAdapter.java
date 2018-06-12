package com.example.laza.afinal.Classes.AdapterClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.laza.afinal.Activities.MainActivity.GoogleMapFragment;
import com.example.laza.afinal.Activities.MainActivity.MyPlacesFragment;
import com.example.laza.afinal.Activities.MainActivity.RoutesFragment;

public class MainPageAdapter extends FragmentStatePagerAdapter {

    private static int num = 3;

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GoogleMapFragment.newInstance(position);
            case 1:
                return RoutesFragment.newInstance(position);
            case 2:
                return MyPlacesFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return num;
    }

}
