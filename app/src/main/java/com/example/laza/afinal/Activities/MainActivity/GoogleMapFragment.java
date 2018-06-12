package com.example.laza.afinal.Activities.MainActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.Interfaces.EnableDisableInterface;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.Classes.Navigation.MediatorMap;
import com.example.laza.afinal.Classes.Navigation.Dialogs;
import com.example.laza.afinal.Classes.Navigation.MapReady;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.SupportMapFragment;

public class GoogleMapFragment extends Fragment implements EnableDisableInterface{

    private static final String ARG_PARAM1 = "param1";
    int i;
    MediatorMap mMediatorMap;

    MyPlace myPlace;

    private OnFragmentInteractionMainListener mListener;

    public GoogleMapFragment() {
    }

    public static GoogleMapFragment newInstance(int i) {
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, i);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMyPlace(MyPlace myPlace){
        this.myPlace = myPlace;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            i = getArguments().getInt(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_google_map, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionMainListener) {
            mListener = (OnFragmentInteractionMainListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        ///////////////////////////////////////////////////////////

        mMediatorMap = new MediatorMap(this.getContext(), this);

        if (this.myPlace != null)
            mMediatorMap.setWaypoint(this.myPlace);


        FloatingActionButton floatingActionButton = getView().findViewById(R.id.myLocationButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediatorMap.goToCurrentLocation();
            }
        });


        Dialogs dialogs = new Dialogs(mMediatorMap, this.getContext());
        MapReady mapReady = new MapReady(mMediatorMap, this.getContext(), GoogleMapFragment.this);
        mapFragment.getMapAsync(mapReady); ////////<---- ovde ide mapHelper  success
        mMediatorMap.setMapReady(mapReady);
        mMediatorMap.setDialogs(dialogs);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediatorMap.connectGAPIClient();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediatorMap.disconnectGAPIClient();
    }

    @Override
    public void setControllsEnable() {
        this.getActivity().findViewById(R.id.progressBarGoogleMap).setVisibility(View.INVISIBLE);
        this.getActivity().findViewById(R.id.map).setEnabled(true);
    }

    @Override
    public void setControllsDisable() {
        this.getActivity().findViewById(R.id.progressBarGoogleMap).setVisibility(View.VISIBLE);
        this.getActivity().findViewById(R.id.map).setEnabled(false);
    }

    public MediatorMap getmMediatorMap(){
        return mMediatorMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getmMediatorMap().onGAPIClientConnected();
                }
                break;
            }
        }
    }

    public void setNewRoute(RouteHolder routeHolder){
        mMediatorMap.setSavedRoute(routeHolder);
    }

    public void setNewPlace(MyPlace myPlace) {

        mMediatorMap.goToPlaceEvent(myPlace);
    }

    public void deleteEventPlace(MyPlace myPlace){
        mMediatorMap.deleteEventPlace(myPlace);
    }

    public interface OnFragmentInteractionMainListener {
        void onFragmentInteractionMain(int i);
    }
}
