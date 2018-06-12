package com.example.laza.afinal.Classes.ModelClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.Navigation.Locations;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by LAZA on 11/12/2017.
 */

public class RouteHolder {

    int ID;
    String Username;
    double Lat_s;
    double Lon_s;
    double Lat_d;
    double Lon_d;

    String route_img;

    ArrayList<LatLng> pointsLatLngs;
    ArrayList<LatLng> partialRoutePoints;
    ArrayList<MyPlace> waypoints;
    ArrayList<MyPlace> markers;

    Bitmap routeBitmap;

    public RouteHolder(int id, String username, LatLng start, LatLng destination,
                       ArrayList<LatLng> points, ArrayList<MyPlace> waypoints, ArrayList<MyPlace> markers, String bitmap){
        this.ID = id;
        this.Username = username;
        this.Lat_s = start.latitude;
        this.Lon_s = start.longitude;
        this.Lat_d = destination.latitude;
        this.Lon_d = destination.longitude;
        this.pointsLatLngs = orderPoints(points);
        this.waypoints = waypoints;
        this.markers = markers;
        this.routeBitmap = putStringToBitmap(bitmap);
    }

    public RouteHolder(){
        pointsLatLngs = new ArrayList<>();
        waypoints = new ArrayList<>();
        markers = new ArrayList<>();
    }

    public int getId(){
        return this.ID;
    }

    public String getUsername(){
        return this.Username;
    }

    public LatLng getStart(){
        return new LatLng(Lat_s, Lon_s);
    }

    public LatLng getDestination(){
        return new LatLng(Lat_d, Lon_d);
    }

    public ArrayList<LatLng> getPoints(){
        return this.pointsLatLngs;
    }

    public ArrayList<LatLng> getPartialRoutePoints(){
        return this.partialRoutePoints;
    }

    public ArrayList<MyPlace> getWaypoints(){
        return this.waypoints;
    }

    public ArrayList<MyPlace> getMarkers() {return this.markers; }

    public Bitmap getRouteFromString(){
        return putStringToBitmap(this.route_img);
    }

    public Bitmap getRouteBitmap(){
        return this.routeBitmap;
    }

    /////////////////////

    public void setStart(LatLng latLng){
        this.Lat_s = latLng.latitude;
        this.Lon_s = latLng.longitude;
    }

    public void setPoints(ArrayList points){
        this.pointsLatLngs = new ArrayList<>();
        if (points.get(0) instanceof LatLng)
            this.pointsLatLngs = points;
    }

    public void setStartPoint(LatLng start) {
        Collections.reverse(pointsLatLngs);
        pointsLatLngs.add(start);
        Collections.reverse(pointsLatLngs);
    }

    public void setMarkers(ArrayList<MyPlace> markers){
        this.markers = markers;
    }

    public void setPartialRoutePoints(ArrayList<LatLng> partialRoutePoints) { this.partialRoutePoints = partialRoutePoints; }

    public void setWaypoints(ArrayList<MyPlace> waypoints) { this.waypoints = waypoints; }

    public void addWaypoint(MyPlace placeEventHolder){
        int index = -1;
        for(int i = 0; i < this.waypoints.size(); i++)
            if (this.waypoints.get(i).getLat() == placeEventHolder.getLat() &&
                    this.waypoints.get(i).getLon() == placeEventHolder.getLon())
                index = i;
        if (index > -1)
            this.waypoints.remove(index);
        else this.waypoints.add(placeEventHolder);
    }

    public void addMarker(MyPlace marker){
        int index = -1;
        for(int i = 0; i < this.markers.size(); i++)
            if (this.markers.get(i).getLat() == marker.getLat() &&
                    this.markers.get(i).getLon() == marker.getLon())
                index = i;
        if (index > -1)
            this.markers.remove(index);
        else this.markers.add(marker);
    }

    public void addPoints(ArrayList<LatLng> newRoutePoints){
        ArrayList<LatLng> points = new ArrayList<>();
        points.addAll(orderPoints(newRoutePoints));
        points.addAll(this.pointsLatLngs);
        this.pointsLatLngs = new ArrayList<>();
        this.pointsLatLngs = points;
    }

    private Bitmap putStringToBitmap(String bitmapString){
        byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setRouteBitmap(){
        this.routeBitmap = putStringToBitmap(this.route_img);
    }

    public void setRouteBitmap(Bitmap routeBitmap){
        this.routeBitmap = routeBitmap;
    }



    public void setDestination(LatLng destination){
        this.Lat_d = destination.latitude;
        this.Lon_d = destination.longitude;
    }

    public ArrayList<LatLng> getRouteWaypoints(){
        ArrayList<LatLng> waypoints_ = new ArrayList<LatLng>();
        waypoints_.add(getStart());
        for (MyPlace placeEventHolder : waypoints
             ) {
            waypoints_.add(new LatLng(placeEventHolder.getLat(), placeEventHolder.getLon()));
        }
        waypoints_.add(getDestination());

        return waypoints_;
    }

    public ArrayList<LatLng> orderPoints(ArrayList<LatLng> points){
        ArrayList<LatLng> points_ = points;
        if (CalculationByDistance(points.get(0), Locations.getLocations().getCurrentLocation())
                > CalculationByDistance(points.get(points.size() - 1), Locations.getLocations().getCurrentLocation())){
            Collections.reverse(points_);
        }
        return points_;
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
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        return Radius * c * 1000.0;
    }

    public String getPointsToString(){

        JSONArray jsonArray = new JSONArray();

        for (LatLng latlng: pointsLatLngs) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(MyApplicationContext.getContext().getResources().getString(R.string.lat), latlng.latitude);
                jsonObject.put(MyApplicationContext.getContext().getResources().getString(R.string.lon), latlng.longitude);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public String getPlaceEventIds(){
        JSONArray jsonArray = new JSONArray();
        try {
            for (MyPlace placeEventHolder : waypoints) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(MyApplicationContext.getContext().getResources().getString(R.string.id), placeEventHolder.getID());
                jsonArray.put(jsonObject);
            }
        }
        catch (JSONException jsonException){

        }
        return jsonArray.toString();
    }

    public ArrayList<MyPlace> getMarkersWaypoints(){
        ArrayList<MyPlace> markersWaypoints = new ArrayList<>();

        for (MyPlace holder: this.markers) {
            if (!HasMarker(holder))
                markersWaypoints.add(holder);
        }

        markersWaypoints.addAll(this.waypoints);
        return markersWaypoints;
    }

    private boolean HasMarker(MyPlace holder){
        boolean has = false;
        for (MyPlace waypoint: waypoints) {
            if (waypoint.getID() == holder.getID()) {
                has = true;
                break;
            }
        }
        return has;
    }

    public boolean deleteEventPlace(MyPlace myPlace) {
        if (removeWaypoint(myPlace)){
            removeMarker(myPlace);
            return true;
        }
        else return false;
    }

    private boolean removeWaypoint(MyPlace myPlace){
        int index = -1;
        for (int i = 0; i < this.waypoints.size(); i++)
            if (this.waypoints.get(i).getLat() == myPlace.getLat() &&
                    this.waypoints.get(i).getLon() == myPlace.getLon())
                index = i;
        if (index > -1) {
            this.waypoints.remove(index);
            return true;
        }
        else return false;
    }

    private void removeMarker(MyPlace myPlace){
        int index = -1;
        for (int i = 0; i < this.markers.size(); i++)
            if (this.markers.get(i).getLat() == myPlace.getLat() &&
                    this.markers.get(i).getLon() == myPlace.getLon())
                index = i;
        if (index > -1)
            this.markers.remove(index);
    }

    public ArrayList<LatLng> getUnwanted(){
        ArrayList<LatLng> unwanted = new ArrayList<>();
        for (MyPlace myPlace : this.markers) {
            if (myPlace.getMI().equals(MyApplicationContext.getContext().getResources().getString(R.string.MARK))){
                unwanted.add(new LatLng(myPlace.getLat(), myPlace.getLon()));
            }
        }
        return unwanted;
    }
}
