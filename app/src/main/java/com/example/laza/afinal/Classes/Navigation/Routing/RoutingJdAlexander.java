package com.example.laza.afinal.Classes.Navigation.Routing;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.Navigation.MediatorMap;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by LAZA on 11/3/2017.
 */

public class RoutingJdAlexander implements RoutingListener{

    MediatorMap mediatorMap;

    public RoutingJdAlexander(MediatorMap context){
        this.mediatorMap = context;
    }

    public void findRoute(String key, ArrayList<LatLng> waypoints) {
        com.directions.route.Routing routing;

        if (waypoints.size() > 1) {
            routing = new com.directions.route.Routing.Builder()
                    .travelMode(com.directions.route.Routing.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(waypoints)
                    .key(key)
                    .build();
            routing.execute();
        }
        else mediatorMap.OnError(MyApplicationContext.getContext().getResources().getString(R.string.not_enough_waypoints));
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        mediatorMap.OnError(e.getMessage());
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        mediatorMap.onRouteSuccess(arrayList, i);
    }


    @Override
    public void onRoutingCancelled() {
    }

}
