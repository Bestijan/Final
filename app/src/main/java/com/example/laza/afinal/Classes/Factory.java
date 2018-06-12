package com.example.laza.afinal.Classes;

import android.content.Context;

import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LAZA on 11/8/2017.
 */

public class Factory {

    public ArrayList getObjects(JSONObject result, int class_){
        ArrayList objects = null;
        Gson gson = new GsonBuilder().create();

        Context context = MyApplicationContext.getContext();

        String MARK = context.getResources().getString(R.string.MARK);
        String INCLUDE = context.getResources().getString(R.string.INCLUDE);
        String ROUTES = context.getResources().getString(R.string.ROUTES);
        String MY_PLACES = context.getResources().getString(R.string.MY_PLACES);


        switch (class_){
            case 0: {

                objects = new ArrayList<MyPlace>();
                try {
                    Type placeEventType = new TypeToken<ArrayList<MyPlace>>(){}.getType();
                    JSONArray array;
                    ArrayList<MyPlace> placeEvents;
                    if (result.has(MARK)) {
                        array = (JSONArray) result.get(MARK);
                        placeEvents = gson.fromJson(array.toString(), placeEventType);
                        objects.addAll(placeEvents);
                    }
                    if (result.has(INCLUDE)) {
                        array  = (JSONArray) result.get(INCLUDE);

                        placeEvents = gson.fromJson(array.toString(), placeEventType);
                        objects.addAll(placeEvents);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }
            case 1:{

                Type routeHolderType = new TypeToken<RouteHolder>(){}.getType();
                Type placeEventType = new TypeToken<ArrayList<MyPlace>>(){}.getType();

                objects = new ArrayList<RouteHolder>();
                try {
                    JSONArray array = (JSONArray) result.get(ROUTES);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject p = array.getJSONObject(i);
                        RouteHolder route = gson.fromJson(p.toString(), routeHolderType);
                        if (p.has(context.getResources().getString(R.string.markers)))
                            route.setMarkers
                                    (gson.fromJson
                                            (p.getJSONArray(context.getResources()
                                                    .getString(R.string.markers)).toString(), placeEventType));
                        if (p.has(context.getResources().getString(R.string.waypoints)))
                            route.setWaypoints(gson.fromJson
                                    (p.getJSONArray(context
                                            .getResources()
                                            .getString(R.string.waypoints)).toString(), placeEventType));
                        objects.add(route);
                        }
                    }
                    catch (JSONException e) {
                            e.printStackTrace();
                    }

                    break;
            }
            case 2:{
                JSONArray array = null;
                objects = new ArrayList<MyPlace>();
                Type myPlaceHolderType = new TypeToken<ArrayList<MyPlace>>(){}.getType();

                try {
                    array = (JSONArray) result.get(MY_PLACES);
                    objects = gson.fromJson(array.toString(), myPlaceHolderType);

                    for (MyPlace myPlace : ((ArrayList<MyPlace>) objects)) {
                        myPlace.setMyPlaceBitmap();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return objects;
    }
}
