package com.example.laza.afinal.Classes.Navigation.Routing;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.laza.afinal.BuildConfig;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.Navigation.Geocoder;
import com.example.laza.afinal.Classes.Volley.VolleySingleton;
import com.example.laza.afinal.Classes.Interfaces.IRoadsListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LAZA on 4/23/2018.
 */

public class MapQuestRouting {

    IRoadsListener listener;

    LatLng from, to;
    ArrayList<LatLng> avoid;
    ArrayList<String> linkIds;
    String getRoute = "http://www.mapquestapi.com/directions/v2/route";
    HashMap<String, String> hashMap;

    int avoidNums;

    public MapQuestRouting(IRoadsListener listener) {
        this.listener = listener;
        this.avoid = new ArrayList<>();
        this.linkIds = new ArrayList<>();
        this.hashMap = new HashMap<>();
        this.avoidNums = 0;
    }

    public void setAvoid(ArrayList<LatLng> unwanted){
        this.avoid = unwanted;
    }

    public void setFromTo(LatLng from, LatLng to){
        this.from = from;
        this.to = to;
    }

    public void GetLinkIDs() {
        if (this.avoidNums < this.avoid.size()) {
            String url = "http://www.mapquestapi.com/directions/v2/findlinkid?key=" + BuildConfig.API_KEY
                    .replace("[", "")
                    .replace("]", "") +
                    "&lat=" + ((Double) this.avoid.get(this.avoidNums).latitude).toString() +
                    "&lng=" + ((Double) this.avoid.get(this.avoidNums).longitude).toString();

            getLinkID(url);
        }
        else {
            GetRoute();
        }
    }

    private void getLinkID(String app_server_url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String linkId = jsonObject.getString("linkId");
                    MapQuestRouting.this.linkIds.add(linkId);
                    MapQuestRouting.this.avoidNums++;
                    GetLinkIDs();
                } catch (JSONException e) {
                    listener.OnError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.OnError(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return hashMap;
            }
        };
        VolleySingleton.getmInstance(MyApplicationContext.getContext()).addToRequestQueue(stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )));
    }

    public void GetRoute() {

        Geocoder from_geocoder = new Geocoder(from);

        Geocoder to_geocoder = new Geocoder(to);
        JSONObject request = new JSONObject();
        JSONArray locations = new JSONArray();
        locations.put(from_geocoder.getAddress());
        locations.put(to_geocoder.getAddress());
        try {
            JSONObject fromJson = new JSONObject();
            fromJson.put("lat", from.latitude);
            fromJson.put("lng", from.longitude);
            JSONObject fromObject = new JSONObject();
            fromObject.put("latLng", fromJson);

            JSONObject toJson = new JSONObject();
            toJson.put("lat", to.latitude);
            toJson.put("lng", to.longitude);
            JSONObject toObject = new JSONObject();
            toObject.put("latLng", toJson);

            //locations.put(fromObject);
            //locations.put(toObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject options = new JSONObject();

        JSONArray linkIds = new JSONArray();
        for (String link_id: this.linkIds) {
            linkIds.put(link_id);
        }

        try {
            options.put("avoids", new JSONArray());
            options.put("avoidTimedConditions", false);
            options.put("doReverseGeocode", true);
            options.put("shapeFormat", "raw");
            options.put("generalize", 0);
            options.put("routeType", "fastest");
            options.put("timeType", 1);
            //options.put("locale", "sr");
            options.put("unit", "m");
            options.put("enhancedNarrative", false);
            options.put("drivingStyle", 2);
            options.put("highwayEfficiency", 21.0);
            options.put("mustAvoidLinkIds", linkIds);
            request.put("locations", locations);
            request.put("options", options);

        } catch (JSONException e) {
            listener.OnError(e.getMessage());
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, getRoute + "?key=" + BuildConfig.API_KEY
                        .replace("[", "")
                        .replace("]", ""),
                        request, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<LatLng> points = new ArrayList<>();
                            JSONArray shapePoints = response.getJSONObject("route").getJSONObject("shape").getJSONArray("shapePoints");
                            for(int i = 0; i < shapePoints.length(); i+=2)
                                points.add(new LatLng(((Double) shapePoints.get(i)),
                                                      ((Double) shapePoints.get(i + 1))));

                            MapQuestRouting.this.listener.OnResponse(points);
                        } catch (JSONException e) {
                            listener.OnError(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                            listener.OnError(error.getMessage());
                    }
                });

// Access the RequestQueue through your singleton class.
        VolleySingleton.getmInstance(MyApplicationContext.getContext()).addToRequestQueue(jsonObjectRequest);
    }
}
