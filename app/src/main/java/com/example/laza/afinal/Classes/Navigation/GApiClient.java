package com.example.laza.afinal.Classes.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by LAZA on 11/10/2017.
 */

public class GApiClient implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;
    MediatorMap mediatorMap;

    public GApiClient(MediatorMap mediatorMap){
        this.mediatorMap = mediatorMap;
    }

    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

    public void connectGoogleApiClient(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if(googleApiClient != null)
            googleApiClient.connect();
    }

    public void disconnectGAPIClient(){
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mediatorMap.onGAPIClientConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
