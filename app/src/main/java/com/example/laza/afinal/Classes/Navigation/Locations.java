package com.example.laza.afinal.Classes.Navigation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by LAZA on 11/26/2017.
 */

public class Locations {

    LatLng currentLocation;
    LatLng destinationLocation;
    LatLng waypointLocation;
    LatLng lastLocation;

    private static Locations locations;

    private Locations(){
    }

    public static Locations getLocations(){
        if (locations == null)
            return locations = new Locations();
        return locations;
    }

    public void setCurrentLocation(LatLng currentLocation){
        this.currentLocation = currentLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) { this.destinationLocation = destinationLocation; }

    public void setWaypointLocation(LatLng waypointLocation){
        this.waypointLocation = waypointLocation;
    }

    public void setLastLocation(LatLng lastLocation){
        this.lastLocation = lastLocation;
    }

    public LatLng getCurrentLocation(){
        return this.currentLocation;
    }

    public LatLng getDestinationLocation(){
        return this.destinationLocation;
    }

    public LatLng getWaypointLocation(){
        return this.waypointLocation;
    }

    public LatLng getLastLocation(){
        return this.lastLocation;
    }
}
