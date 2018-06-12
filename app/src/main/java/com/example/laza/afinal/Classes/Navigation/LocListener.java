package com.example.laza.afinal.Classes.Navigation;

import android.annotation.SuppressLint;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by LAZA on 11/10/2017.
 */

public class LocListener implements LocationListener {

    MediatorMap mediatorMap;

    public LocListener(MediatorMap mediatorMap) {
        this.mediatorMap = mediatorMap;
    }

    @Override
    public void onLocationChanged(Location loc) {
        LatLng latLng;
        latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        mediatorMap.setCurrentLocation(latLng);
    }


    @SuppressLint("MissingPermission")
    public void setRequestLocation(GoogleApiClient googleApiClient) {

        //Context context = MyApplicationContext.getContext();
        Location loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (loc != null) {
            LatLng location = new LatLng(loc.getLatitude(), loc.getLongitude());
            mediatorMap.setCurrentLocation(location);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
}
