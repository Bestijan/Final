package com.example.laza.afinal.Classes.Interfaces;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LAZA on 4/21/2018.
 */

public interface IRoadsListener {
    void OnResponse(ArrayList<LatLng> points);
    void OnError(String error);
}
