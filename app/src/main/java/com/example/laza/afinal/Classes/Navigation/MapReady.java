package com.example.laza.afinal.Classes.Navigation;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.Activities.MainActivity.GoogleMapFragment;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by LAZA on 11/10/2017.
 */

public class MapReady implements OnMapReadyCallback  {

    MediatorMap mediatorMap;
    Context context;
    GoogleMapFragment googleMapFragment;
    Polyline routePolyline;
    Polyline partialPolyline;

    public GoogleMap googleMap;
    private Marker currentLocation;
    private Marker markerDestination;
    private BitmapResources bitmapResources;
    private ArrayList<Marker> markers;
    private Marker lastMarker;
    private Bitmap currentLoc;

    public MapReady(MediatorMap mediatorMap, Context context, GoogleMapFragment googleMapFragment){
        this.mediatorMap = mediatorMap;
        this.context = context;
        this.googleMapFragment = googleMapFragment;
        this.bitmapResources = new BitmapResources();
        this.markers = new ArrayList<>();
        this.currentLoc = bitmapResources.getCurrLocB();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        setMapAttributes();
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mediatorMap.onMapLoaded();
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mediatorMap.onMapClickListener(latLng);
            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (mediatorMap.getRouteLoaded() && !mediatorMap.getRouteStarted())
                    mediatorMap.onSaveRouteDialog();
            }
        });


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Object tag = marker.getTag();
                if (tag != null && MapReady.this.mediatorMap.getRouteLoaded()) {
                    onMarkerClickMyPlaceHolder((MyPlace) tag);
                }
                else marker.remove();
                return false;
            }
        });

    }

    private void onMarkerClickMyPlaceHolder(MyPlace MyPlace){
            if (MyPlace.getMI().equals(context.getResources().getString(R.string.INCLUDE)))
                if (MyPlace.getName().equals(context.getResources().getString(R.string.Waypoint)) ||
                    SharedPreferencesHelper.getAccount()
                            .getRouteSettings(MyApplicationContext.getContext(), MyPlace.getName())) {
                mediatorMap.onMarkerClick(MyPlace);
            }
    }

    private void setMapAttributes() {
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setBuildingsEnabled(false);
        googleMap.setPadding(0, 0, 20, 100);
    }

    public void setMapAnimation(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                (new CameraPosition.Builder().target(latLng)
                        .zoom(18.0f).build()));
    }

    public void setMarkerDestination(LatLng latLng, BitmapDescriptor markerIcon){
        this.markerDestination = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(context.getResources().getString(R.string.Destination)));
        markerDestination.setIcon(markerIcon);
        markerDestination.showInfoWindow();
        setDestination(markerDestination);
    }

    public void setCurrentLocation(LatLng currentLocation){
        if (this.currentLocation != null)
        this.currentLocation.remove();
        this.currentLocation = googleMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromBitmap(currentLoc))
                .title(context.getResources().getString(R.string.Current_location)));
        if (this.mediatorMap.getRouteStarted()) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                    (new CameraPosition.Builder().target(new LatLng(currentLocation.latitude,
                                                                    currentLocation.longitude))
                            .zoom(20.0f).build()));
        }
    }

    public void setDestination(Marker destination){
        this.mediatorMap.setMarkerDestination(destination);
    }

    public void setLastMarkerTag(MyPlace myPlace) {
        this.lastMarker.setTag(myPlace);
    }

    public void setNewRoute(RouteHolder routeHolder){

        clearAll();

        BitmapDescriptor markerBitmap = bitmapResources.getDestLocBD();

        animateCamera(routeHolder);
        showPlaceEvents(routeHolder.getMarkersWaypoints());

        Marker markerDestinationLocation = googleMap
                .addMarker(new MarkerOptions().position(routeHolder.getDestination()).title("Destination location"));
        markerDestinationLocation.setIcon(markerBitmap);

        LatLng[] points = new LatLng[routeHolder.getPoints().size()];

        points = routeHolder.getPoints().toArray(points);

        routePolyline = googleMap
                .addPolyline(new PolylineOptions().add(points).color(0xaa3333cc).width(25));
    }

    public void setReroute(ArrayList<LatLng> route){

        if (routePolyline != null)
            routePolyline.remove();

        LatLng[] points = new LatLng[route.size()];
        points = route.toArray(points);

        routePolyline = googleMap
                .addPolyline(new PolylineOptions().add(points).color(0xaa3333cc).width(25));
    }

    public void animateCamera(RouteHolder routeHolder){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : routeHolder.getRouteWaypoints()) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        googleMap.moveCamera(cu);
        googleMap.moveCamera(CameraUpdateFactory.zoomBy(-0.4f));
    }

    public void showPlaceEvents(ArrayList<MyPlace> myPlaceData){
        for (MyPlace MyPlace : myPlaceData) {
            addWaypointMarker(MyPlace);
        }
    }

    public void deleteMarker(MyPlace myPlace){
        int index = -1;
        for (int i = 0; i < this.markers.size(); i++) {
            MyPlace markerTag = ((MyPlace) this.markers.get(i).getTag());
            if (markerTag.getLat() == myPlace.getLat() && markerTag.getLon() == myPlace.getLon())
                index = i;
        }
        if (index > -1)
            this.markers.remove(index);
    }

    public void addWaypointMarker(MyPlace MyPlace){
        Marker marker = googleMap
                .addMarker(new MarkerOptions().position(new LatLng(MyPlace.getLat(), MyPlace.getLon()))
                        .title(MyPlace.getName()));

        marker.setTag(MyPlace);
        marker.setTitle(MyPlace.getName());
        marker.showInfoWindow();

        if (MyPlace.getName().equals(context.getResources().getString(R.string.Waypoint))){
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        else if (MyPlace.getMI().equals(context.getResources().getString(R.string.MARK))){
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        else if (MyPlace.getMI().equals(context.getResources().getString(R.string.INCLUDE))){
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        markers.add(marker);
    }

    public void captureMapEvent(final String event, LatLng location){
        GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                mediatorMap.sendEvent(event, bitmap);
            }
        };

        this.lastMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude))
                .title(event));

        this.lastMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        if (!this.mediatorMap.getRouteStarted())
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                (new CameraPosition.Builder().target(location)
                        .zoom(20.0f).build()), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                googleMap.snapshot(snapshotReadyCallback);
            }

            @Override
            public void onCancel() {

            }
        });
        else googleMap.snapshot(snapshotReadyCallback);
    }

    public void captureMapPlace(final String mi, final String place, LatLng location){
        GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                mediatorMap.sendPlace(mi, place, bitmap);
            }
        };

        this.lastMarker = googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(place));

        if (place.equals(context.getResources().getString(R.string.Waypoint)))
            this.lastMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        else if (mi.equals(context.getResources().getString(R.string.MARK)))
            this.lastMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        else this.lastMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));



        if (!this.mediatorMap.getRouteStarted())
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                (new CameraPosition.Builder().target(location)
                        .zoom(20.0f).build()), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                googleMap.snapshot(snapshotReadyCallback);
            }

            @Override
            public void onCancel() {

            }
        });
        else googleMap.snapshot(snapshotReadyCallback);
    }

    public void captureRoute(){
        GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                mediatorMap.setRouteHolderBitmap(bitmap);
            }
        };
        googleMap.snapshot(snapshotReadyCallback);
    }

    public void goToPlace(MyPlace myPlace){
        this.markerDestination = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(myPlace.getLat(), myPlace.getLon()))
                .title(myPlace.getName()));

        this.markerDestination.setTag(myPlace);

        if (myPlace.getName().equals(context.getResources().getString(R.string.Waypoint)))
            this.markerDestination.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        else {
            if (myPlace.getMI().equals(context.getResources().getString(R.string.MARK)))
                this.markerDestination.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            else this.markerDestination.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                (new CameraPosition.Builder().target(new LatLng(myPlace.getLat(), myPlace.getLon()))
                        .zoom(18.0f).build()));

        if (this.mediatorMap.getRouteLoaded()) {
            boolean locationOnPath = com.google.maps.android.PolyUtil
                    .isLocationOnPath(new LatLng(myPlace.getLat(), myPlace.getLon()),
                                      routePolyline.getPoints(), false, 5);

            this.mediatorMap.OnGoToPlaceEvent(myPlace, locationOnPath);

        }
    }

    public void clearAll(){
        this.googleMap.clear();
        if (routePolyline != null)
            routePolyline.remove();
    }
}

