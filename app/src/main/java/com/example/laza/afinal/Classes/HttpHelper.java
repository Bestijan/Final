package com.example.laza.afinal.Classes;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.Volley.VolleyHttpHelper;
import com.example.laza.afinal.Classes.Firebase.FcmMessagingService;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;


/**
 * Created by LAZA on 1/24/2018.
 */

public class HttpHelper {

    VolleyHttpHelper volleyHttpHelper;
    IVolleyResponse responseListener;
    String ip;
    Context context;

    public HttpHelper(IVolleyResponse responseListener){
        this.responseListener = responseListener;
        this.ip = MyApplicationContext.getContext().getResources().getString(R.string.ip);
        this.context = MyApplicationContext.getContext();
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return Radius * c;
    }

    public void GetEventPlace(LatLng current, LatLng dest){
        double distance = CalculationByDistance(current, dest);

        volleyHttpHelper = new VolleyHttpHelper()
                .setResponseListener(this.responseListener)
                .setLatLng(current)
                .setDistance(distance)
                .setNames(SharedPreferencesHelper.getAccount().getRouteStrings(MyApplicationContext.getContext()));

        volleyHttpHelper.send(this.ip + this.context.getResources().getString(R.string.get_event_place),
                this.responseListener);
    }

    public void SendRoute(RouteHolder routeHolder){
        volleyHttpHelper = new VolleyHttpHelper()
                .setResponseListener(this.responseListener)
                .setUsername(SharedPreferencesHelper.getAccount().getUsername(MyApplicationContext.getContext()))
                .setStart(routeHolder.getStart())
                .setDestination(routeHolder.getDestination())
                .setPlaceEvents(routeHolder.getPlaceEventIds())
                .setBitmap(routeHolder.getRouteBitmap());

        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.save_route), this.responseListener);
    }

    public void SendEvent(String event, Bitmap bitmap, LatLng eventLocation){

        LatLng latLng = eventLocation;
        volleyHttpHelper = new VolleyHttpHelper()
                .setResponseListener(this.responseListener)
                .setUsername(SharedPreferencesHelper.getAccount()
                        .getUsername(MyApplicationContext.getContext()))
                .setPE(context.getResources().getString(R.string.event))
                .setMI(context.getResources().getString(R.string.MARK))
                .setName(event)
                .setLatLng(latLng)
                .setBitmap(bitmap);

        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.upload_event_place),
                this.responseListener);

        //SendNotification(event, latLng.latitude, latLng.longitude);
    }

    public void SendPlace(String mi, String place, Bitmap bitmap, LatLng placeLocation){
        LatLng latLng = placeLocation;
        volleyHttpHelper = new VolleyHttpHelper()
                .setResponseListener(this.responseListener)
                .setUsername(SharedPreferencesHelper.getAccount()
                        .getUsername(MyApplicationContext.getContext()))
                .setPE(context.getResources().getString(R.string.place))
                .setMI(mi)
                .setName(place)
                .setLatLng(latLng)
                .setBitmap(bitmap);

        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.upload_event_place),
                this.responseListener);

        //SendNotification(place, latLng.latitude, latLng.longitude);
    }

    public void SendWaypoint(LatLng lastLocation){
        volleyHttpHelper = new VolleyHttpHelper()
                .setResponseListener(this.responseListener)
                .setUsername(SharedPreferencesHelper.getAccount()
                        .getUsername(MyApplicationContext.getContext()))
                .setPE(context.getResources().getString(R.string.place))
                .setMI(context.getResources().getString(R.string.INCLUDE))
                .setName(context.getResources().getString(R.string.Waypoint))
                .setLatLng(lastLocation);

        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.upload_event_place), this.responseListener);
    }

    public void SendNotification(MyPlace myPlace){
        FcmMessagingService.token = SharedPreferencesHelper.getAccount().getToken(MyApplicationContext.getContext());
        volleyHttpHelper = new VolleyHttpHelper()
                .setToken()
                .setTopic(myPlace.getName())
                .setResponseListener(this.responseListener)
                .setId(myPlace.getID());
        volleyHttpHelper.send(ip +
                context.getResources().getString(R.string.send_notification), this.responseListener);
    }

    public void DeleteRoute(int routeID){
        volleyHttpHelper = new VolleyHttpHelper()
                .setId(routeID)
                .setUsername(SharedPreferencesHelper.getAccount()
                        .getUsername(MyApplicationContext.getContext()));
        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.delete_route), this.responseListener);
    }

    public void DeleteMyPlace(int myPlaceID){
        volleyHttpHelper = new VolleyHttpHelper()
                .setId(myPlaceID)
                .setUsername(SharedPreferencesHelper.getAccount()
                        .getUsername(MyApplicationContext.getContext()));
        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.delete_my_place), this.responseListener);
    }

    public void DeleteAccount(){
        volleyHttpHelper = new VolleyHttpHelper()
                .setUsername(SharedPreferencesHelper.getAccount()
                        .getUsername(MyApplicationContext.getContext()));
        volleyHttpHelper.send(ip + this.context.getResources().getString(R.string.delete_account), this.responseListener);
    }

    public void RegisterAccount(String username, String password, Bitmap profilePic){
        VolleyHttpHelper helper = new VolleyHttpHelper()
                .setUsername(username)
                .setPassword(password)
                .setBitmap(profilePic);
        helper.send(MyApplicationContext.getContext().getResources().getString(R.string.ip) +
                MyApplicationContext.getContext().getResources().getString(R.string.send_user_data), this.responseListener);
    }

    public void SignInAccount(String username, String password){
        VolleyHttpHelper helper = new VolleyHttpHelper()
                .setUsername(username)
                .setPassword(password);
        helper.send(MyApplicationContext.getContext().getResources().getString(R.string.ip) +
                MyApplicationContext.getContext().getResources().getString(R.string.receive_user_data), this.responseListener);
    }

}
