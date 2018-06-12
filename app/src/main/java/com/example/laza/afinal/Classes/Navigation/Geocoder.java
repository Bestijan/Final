package com.example.laza.afinal.Classes.Navigation;

import android.location.Address;

import com.example.laza.afinal.Classes.MyApplicationContext;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by LAZA on 3/7/2018.
 */

public class Geocoder {

    Address address;

    public Geocoder(LatLng point){
        android.location.Geocoder geocoder;
        address = new Address(Locale.getDefault());
        geocoder = new android.location.Geocoder(MyApplicationContext.getContext(), Locale.getDefault());
        try {
            address = geocoder.getFromLocation(point.latitude, point.longitude, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getAddress(){
        return address.getAddressLine(0);
    }

}
