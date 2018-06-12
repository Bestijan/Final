package com.example.laza.afinal.Classes.Navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.widget.Toast;

import com.directions.route.Route;
import com.example.laza.afinal.Classes.Factory;
import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.HttpHelper;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.Classes.Interfaces.IRoadsListener;
import com.example.laza.afinal.Classes.Navigation.Routing.MapQuestRouting;
import com.example.laza.afinal.Classes.Navigation.Routing.RoutingJdAlexander;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.Activities.MainActivity.GoogleMapFragment;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by LAZA on 11/3/2017.
 */

public class MediatorMap implements IVolleyResponse, IRoadsListener{

    private GApiClient GApiClient;
    private HttpHelper httpHelper;
    private MyPlace waypoint;
    private LatLng waypointLatLng;
    private int waypointCount;

    private LocListener listenerClass;
    private RoutingJdAlexander routingJdAlexander;
    private RouteHolder routeHolder;

    private MapReady mapReady;
    private Dialogs dialogs;
    private BitmapResources bitmapResources;

    private boolean routeLoaded;
    private boolean routeStarted;

    private ArrayList<MyPlace> waypoints;

    private Context context;
    private GoogleMapFragment googleMapFragment;
    private LatLng lastLocation;
    private LatLng currentLocation;

    public MediatorMap(Context context, GoogleMapFragment googleMapFragment) {

        this.httpHelper = new HttpHelper(this);
        this.routeHolder = new RouteHolder();
        this.routingJdAlexander = new RoutingJdAlexander(MediatorMap.this);

        this.GApiClient = new GApiClient(this);

        this.listenerClass = new LocListener(this);
        this.bitmapResources = new BitmapResources();
        this.routeLoaded = false;
        this.routeStarted = false;
        this.context = context;
        this.googleMapFragment = googleMapFragment;
        this.lastLocation = new LatLng(0.0, 0.0);
        this.waypointLatLng = new LatLng(0.0, 0.0);
        this.waypointCount = 0;
        this.waypoints = new ArrayList<>();
    }

    public void goToCurrentLocation(){
        this.mapReady.setMapAnimation(currentLocation);
    }

    public boolean getRouteLoaded(){
        return this.routeLoaded;
    }

    public boolean getRouteStarted() { return this.routeStarted; }

    public void setRouteStarted() {
        this.routeStarted = true;
    }

    public void setWaypoint(MyPlace myPlace){
        this.waypoint = myPlace;
    }

    public void setMapReady(MapReady view){
        this.mapReady = view;
    }
    public void setDialogs(Dialogs view){
        this.dialogs = view;
    }

    public void onMapClickListener(LatLng latLng){
        this.lastLocation = latLng;
        this.dialogs.onMapClick();
    }

    public void setMarkerDestination(Marker destination){
       this.routeHolder.setDestination(destination.getPosition());
    }

    private boolean checkIfAvailable(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            dialogs.showLocationSourceDialog();
            return false;
        }
        return true;
    }

    ///////////////////////////// Routes \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void createNewRoute(){
        this.googleMapFragment.setControllsDisable();
        routeHolder = new RouteHolder();
        routeHolder.setStart(currentLocation);
        routeHolder.setDestination(lastLocation);
        httpHelper.GetEventPlace(currentLocation, lastLocation);
    }

    public void onGetEventPlace(ArrayList<MyPlace> markers){
        routeHolder.setMarkers(markers);
        findRoute(routeHolder.getRouteWaypoints());
    }

    public void findRoute(ArrayList<LatLng> waypoints){
        routingJdAlexander.findRoute(MyApplicationContext.getContext().getResources().getString(R.string.directions), waypoints);
        //httpHelper = new HttpHelper(this);
        //httpHelper.GetRoads(this.routeHolder.getUnwanted(), this);
        //MapQuestRouting mapQuestRouting = new MapQuestRouting();
        //mapQuestRouting.GetRoute(MediatorMap.this);
        //mapQuestRouting.GetLinkID(waypoints.get(0), this);
        //mapQuestRouting.setFromTo(waypoints.get(0), waypoints.get(1));
        //mapQuestRouting.GetRoute(this, "");
        //Geocoder geocoder = new Geocoder(waypoints.get(0));
        //String address = geocoder.getAddress();
        //ReverseGeocoding geocoding = new ReverseGeocoding(waypoints.get(0));
        //geocoding.GetReverseGeocoding(this);
    }

    public void onRouteSuccess(ArrayList<Route> routes, int routeIndeks){
        routeHolder.setPoints(new ArrayList<>(routes.get(routeIndeks).getPoints()));
        mapReady.setNewRoute(routeHolder);
        this.routeLoaded = true;
        googleMapFragment.setControllsEnable();
    }

    public void onNewRouteFailure(){
        Toast.makeText(context, "Failed to find new route", Toast.LENGTH_SHORT).show();
        googleMapFragment.setControllsEnable();
    }

    public void setSavedRoute(RouteHolder routeHolder_){
        this.routeLoaded = true;
        this.routeHolder = routeHolder_;
        this.dialogs.setOpeningDialog(1);
        ArrayList<LatLng> savedRouteWaypoints = this.routeHolder.getRouteWaypoints();
        savedRouteWaypoints.add(0, this.currentLocation);
        findRoute(savedRouteWaypoints);
    }


    public void addWaypoint(){
        setWaypoint(context.getResources().getString(R.string.place),
                context.getResources().getString(R.string.INCLUDE),
                context.getResources().getString(R.string.Waypoint));
    }

    public void removeWaypoint(MyPlace MyPlace){
        routeHolder.addMarker(MyPlace);
        routeHolder.addWaypoint(MyPlace);
        findRoute(routeHolder.getRouteWaypoints());
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

        return Radius * c;
    }

    public void setCurrentLocation(LatLng latLng){
        this.routeHolder.setStart(latLng);
        this.currentLocation = latLng;
        this.mapReady.setCurrentLocation(latLng);
        if (this.routeHolder != null)
        if (this.routeStarted)
            if (this.routeHolder.getPoints().size() > 0) {
                if (CalculationByDistance(this.currentLocation, this.routeHolder.getPoints()
                        .get(this.routeHolder.getPoints().size() - 1)) < 0.005){
                    this.dialogs.stopRoute();
                    this.stopRoute();
            }
        }
    }

    public void onSaveRouteDialog(){
        this.dialogs.saveNewRouteDialog();
    }

    public void onSaveRoute(){
        this.googleMapFragment.setControllsDisable();
        this.mapReady.captureRoute();
    }

    public void stopRoute(){
        this.routeLoaded = false;
        this.routeStarted = false;
        mapReady.clearAll();
    }

    public void onEventChoice(String event_place){
        this.googleMapFragment.setControllsDisable();
        if (this.routeStarted)
            mapReady.captureMapEvent(event_place, currentLocation);
        else mapReady.captureMapEvent(event_place, lastLocation);
    }

    public void onPlaceChoice(String mi, String place){
        this.googleMapFragment.setControllsDisable();
        if(this.routeStarted)
            mapReady.captureMapPlace(mi, place, currentLocation);
        else mapReady.captureMapPlace(mi, place, lastLocation);
    }

    public void onMarkerClick(MyPlace MyPlace){
        routeHolder.addWaypoint(MyPlace);
        routeHolder.addMarker(MyPlace);
        findRoute(routeHolder.getRouteWaypoints());
    }

    public void onWaypointAdd(MyPlace myPlace){
        routeHolder.addWaypoint(myPlace);
        routeHolder.addMarker(myPlace);
        findRoute(routeHolder.getRouteWaypoints());
    }

    //////////////////////////////////////////////////////////////////////////////////
    public void connectGAPIClient(){
        if (checkIfAvailable(context))
            GApiClient.connectGoogleApiClient(context);
    }

    public void onGAPIClientConnected(){
        listenerClass.setRequestLocation(GApiClient.getGoogleApiClient());
    }

    public void onMapLoaded(){
        if (currentLocation != null) {
            setCurrentLocation(currentLocation);
            mapReady.setCurrentLocation(currentLocation);
            mapReady.setMapAnimation(currentLocation);
        }
        if (this.waypoint != null)
            mapReady.addWaypointMarker(this.waypoint);
    }

    public void disconnectGAPIClient(){
        GApiClient.disconnectGAPIClient();
    }

    public void goToPlaceEvent(MyPlace myPlace){
        this.mapReady.goToPlace(myPlace);
    }

    public void OnGoToPlaceEvent(MyPlace myPlace, boolean locationOnPath){

        this.routeHolder.addMarker(myPlace);
        //this.lastLocation = new LatLng(myPlace.getLat(), myPlace.getLon());
        this.waypoint = myPlace;

        if (myPlace.getMI().equals(context.getResources().getString(R.string.MARK))){
            if (locationOnPath){
                this.dialogs.UnwantedRerouteDialog(myPlace.getName());
            }
        }
        else if(myPlace.getMI().equals(context.getResources().getString(R.string.INCLUDE))) {
            this.dialogs.WantedRerouteDialog(myPlace.getName());
        }
    }

    public void WantedRerroute(){
        //Toast.makeText(context, "Wanted", Toast.LENGTH_SHORT).show();
        routeHolder.addWaypoint(this.waypoint);
        routeHolder.addMarker(this.waypoint);
        findRoute(routeHolder.getRouteWaypoints());
    }

    public void UnwantedReroute(){
        this.googleMapFragment.setControllsDisable();
        MapQuestRouting mapQuestRouting = new MapQuestRouting(this);
        mapQuestRouting.setFromTo(this.currentLocation, this.routeHolder.getDestination());
        ArrayList<LatLng> avoid = new ArrayList<>();
        avoid.addAll(this.routeHolder.getUnwanted());
        avoid.add(new LatLng(this.waypoint.getLat(), this.waypoint.getLon()));
        mapQuestRouting.setAvoid(avoid);
        mapQuestRouting.GetLinkIDs();
    }

    /// Send \\\

    public void setRouteHolderBitmap(Bitmap googleMapBitmap){
        this.routeHolder.setRouteBitmap(googleMapBitmap);
        ArrayList<MyPlace> waypoints = new ArrayList<>();
        for (MyPlace waypoint: this.routeHolder.getWaypoints()) {
            if (waypoint.getName().equals(context.getResources().getString(R.string.Waypoint)))
                waypoints.add(waypoint);
        }
        this.waypoints = waypoints;
        sendRoute();
    }

    public void sendRoute(){

        if (waypointCount < this.waypoints.size()){
            this.waypointLatLng = new LatLng(this.waypoints.get(waypointCount).getLat(),
                                             this.waypoints.get(waypointCount).getLon());
            this.httpHelper.SendWaypoint(this.waypointLatLng);
            this.waypointCount++;
        }
        else {
            httpHelper.SendRoute(this.routeHolder);
            this.waypointCount = 0;
        }
    }

    public void sendEvent(String event, Bitmap bitmap){
        setWaypoint(context.getResources().getString(R.string.event),
                context.getResources().getString(R.string.mark), event);
        httpHelper.SendEvent(event, bitmap, lastLocation);
    }

    public void sendPlace(String mi, String place, Bitmap bitmap){
        //if (place.equals("Waypoint"))
            setWaypoint(context.getResources().getString(R.string.place), mi, place);
        httpHelper.SendPlace(mi, place, bitmap, lastLocation);
    }

    public void sendWaypoint(){
        httpHelper.SendWaypoint(lastLocation);
    }

    private void setWaypoint(String PE, String MI, String Name){
        LatLng latLng = lastLocation;
        LatLng startLatLng = currentLocation;
        waypoint = new MyPlace(0, SharedPreferencesHelper.getAccount().getUsername(MyApplicationContext.getContext()),
                PE, MI, Name, null,
                latLng.latitude, latLng.longitude, null, CalculationByDistance(startLatLng, latLng));
        if (Name.equals(context.getResources().getString(R.string.Waypoint)))
        onWaypointAdd(waypoint);
    }

    public void deleteEventPlace(MyPlace myPlace){
        if (this.routeHolder.deleteEventPlace(myPlace)) {
            this.mapReady.deleteMarker(myPlace);
            if (routeHolder != null && routeHolder.getDestination() != null)
                findRoute(this.routeHolder.getRouteWaypoints());
        }
    }

    @Override
    public void onSuccess(){
        this.googleMapFragment.setControllsEnable();
    }

    @Override
    public void OnResponse(Object response) {
        this.googleMapFragment.setControllsEnable();
        Factory factory = new Factory();
        ArrayList<MyPlace> myPlaceData = factory.getObjects((JSONObject)response,
                context.getResources().getInteger(R.integer.CLASS_WAYPOINT));
        onGetEventPlace(myPlaceData);
    }

    @Override
    public void OnIDResponse(Object response) {
        this.googleMapFragment.setControllsEnable();
        for (int i = 0; i < this.routeHolder.getWaypoints().size(); i++) {
            if (this.routeHolder.getWaypoints().get(i).getName().equals(context.getResources().getString(R.string.Waypoint))
                    && this.waypointLatLng.latitude == this.routeHolder.getWaypoints().get(i).getLat()
                    && this.waypointLatLng.longitude == this.routeHolder.getWaypoints().get(i).getLon()){
                this.routeHolder.getWaypoints().get(i).setID(Integer.valueOf(response.toString()));
            }
        }
        sendRoute();
    }

    /// Notify response zahteva id kako bi mogao da posalje waypoint upload_event_place
    /// zahteva da vrati i ID

    @Override
    public void NotifyResponse(Object response) {
        this.googleMapFragment.setControllsEnable();
        String id_s = response.toString();
        int id = Integer.valueOf(id_s);
        waypoint.setID(id);
        this.mapReady.setLastMarkerTag(waypoint);
        httpHelper.SendNotification(waypoint);

    }

    @Override
    public void OnResponse(ArrayList<LatLng> points) {
        this.googleMapFragment.setControllsEnable();
        this.mapReady.setReroute(points);
    }

    @Override
    public void OnError(String error) {
        dialogs.onError(error);
        this.googleMapFragment.setControllsEnable();
    }

    @Override
    public Context GetContext() {
        return null;
    }

}
