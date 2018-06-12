package com.example.laza.afinal.Classes.Volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LAZA on 10/19/2017.
 */

public class VolleyHttpHelper {

    HashMap<String, String> hashMap;
    Context context;
    IVolleyResponse responseListener;

    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public VolleyHttpHelper(){
        hashMap = new HashMap<>();
        this.context = MyApplicationContext.getContext();
    }

    public VolleyHttpHelper setResponseListener(IVolleyResponse responseListener){
        this.responseListener = responseListener;
        return this;
    }

    public VolleyHttpHelper setUsername(String username){
        hashMap.put(this.context.getResources().getString(R.string.username), username);
        return this;
    }

    public VolleyHttpHelper setPassword(String password){
        hashMap.put(this.context.getResources().getString(R.string.password), password);
        return this;
    }

    public VolleyHttpHelper setNullBitmap(){
        hashMap.put(this.context.getResources().getString(R.string.pic), null);
        return this;
    }

    public VolleyHttpHelper setBitmap(Bitmap pic) {
        hashMap.put(this.context.getResources().getString(R.string.pic), bitmapToString(pic));
        return this;
    }

    public VolleyHttpHelper setPE(String pe) {
        hashMap.put(context.getResources().getString(R.string.pe), pe);
        return this;
    }

    public VolleyHttpHelper setMI(String mi) {
        hashMap.put(context.getResources().getString(R.string.mi), mi);
        return this;
    }

    public VolleyHttpHelper setName(String name) {
        hashMap.put(context.getResources().getString(R.string.name), name);
        return this;
    }

    public VolleyHttpHelper setLatLng(LatLng latLng) {
        hashMap.put(context.getResources().getString(R.string.lat), String.valueOf(latLng.latitude));
        hashMap.put(context.getResources().getString(R.string.lon), String.valueOf(latLng.longitude));
        return this;
    }

    public VolleyHttpHelper setDistance(double distance) {
        hashMap.put(context.getResources().getString(R.string.distance), String.valueOf(distance));
        return this;
    }

    public VolleyHttpHelper setNames(String names) {
        hashMap.put(context.getResources().getString(R.string.names), names);
        return this;
    }

    public VolleyHttpHelper setStart(LatLng start){
        hashMap.put(context.getResources().getString(R.string.lat_s), String.valueOf(start.latitude));
        hashMap.put(context.getResources().getString(R.string.lon_s), String.valueOf(start.longitude));
        return this;
    }

    public VolleyHttpHelper setDestination(LatLng dest) {
        hashMap.put(context.getResources().getString(R.string.lat_d), String.valueOf(dest.latitude));
        hashMap.put(context.getResources().getString(R.string.lon_d), String.valueOf(dest.longitude));
        return this;
    }

    public VolleyHttpHelper setMarkers(String markers) {
        hashMap.put(context.getResources().getString(R.string.markers), markers);
        return this;
    }

    public VolleyHttpHelper setPlaceEvents(String placeEvents) {
        hashMap.put(context.getResources().getString(R.string.place_events), placeEvents);
        return this;
    }

    public VolleyHttpHelper setTopic(String topic){
        hashMap.put(context.getResources().getString(R.string.topic), topic);
        return this;
    }

    public VolleyHttpHelper setTopicLatLng(LatLng topicLatLng) {
        hashMap.put(context.getResources().getString(R.string.lat), String.valueOf(topicLatLng.latitude));
        hashMap.put(context.getResources().getString(R.string.lon), String.valueOf(topicLatLng.longitude));
        return this;
    }

    public VolleyHttpHelper setToken(){
        hashMap.put(context.getResources().getString(R.string.token), SharedPreferencesHelper.getAccount().getToken(MyApplicationContext.getContext()));
        return this;
    }

    public VolleyHttpHelper setId(int id){
        hashMap.put(context.getResources().getString(R.string.id), ((Integer)id).toString());
        return this;
    }

    public VolleyHttpHelper setNewUsername(String username){
        hashMap.put(context.getResources().getString(R.string.new_username), username);
        return this;
    }

    public VolleyHttpHelper(HashMap<String, String> hashMap, IVolleyResponse responseListener){
        this.hashMap = hashMap;
        this.responseListener = responseListener;
    }

    public void send(final String app_server_url, final IVolleyResponse listener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains(VolleyHttpHelper.this.context.getResources().getString(R.string.connection_error)) ||
                        response.contains(VolleyHttpHelper.this.context.getResources().getString(R.string.connection_error))) {
                    listener.OnError(VolleyHttpHelper.this.context.getResources().getString(R.string.connection_failed));
                    return;
                }

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String result = jsonObject.getString(VolleyHttpHelper.this.context.getResources().getString(R.string.result));
                    String id = jsonObject.getString(VolleyHttpHelper.this.context.getResources().getString(R.string.id));

                    if (result.equals(context.getResources().getString(R.string.ok_result))) {

                        switch (id) {
                            case "register": {
                                listener.OnResponse(hashMap);
                                break;
                            }
                            case "sign_in": {
                                hashMap.put(context.getResources().getString(R.string.pic),
                                        jsonObject.getString(VolleyHttpHelper.this.context.getResources().getString(R.string.pic)));
                                listener.OnResponse(hashMap);
                                break;
                            }
                            case "upload_event_place": {
                                listener.NotifyResponse(jsonObject.get("ID"));
                                break;
                            }
                            case "upload_waypoint": {
                                listener.OnIDResponse(jsonObject.get("ID"));
                                break;
                            }
                            case "get_event_place": {
                                listener.OnResponse(jsonObject);
                                break;
                            }
                            case "save_route": {
                                listener.onSuccess();
                                break;
                            }
                            case "get_route":{
                                listener.OnResponse(jsonObject);
                                break;
                            }
                            case "get_my_places":{
                                listener.OnResponse(jsonObject);
                                break;
                            }
                            case "delete_my_place":{
                                listener.OnResponse(jsonObject);
                                break;
                            }
                            case "delete_route":{
                                listener.OnResponse(jsonObject);
                                break;
                            }
                            case "delete_waypoint":{
                                break;
                            }
                            case "send_notification":{
                                break;
                            }
                            case "update_account":{
                                listener.OnResponse(jsonObject);
                                break;
                            }
                            case "delete_account": {
                                listener.OnIDResponse(jsonObject);
                                break;
                            }
                        }
                    } else if (result.equals(context.getResources().getString(R.string.already_exists)))
                        listener.OnError(VolleyHttpHelper.this.context.getResources().getString(R.string.user_already_exists));
                    else if (result.equals(VolleyHttpHelper.this.context.getResources().getString(R.string.parameters_error_result)))
                        listener.OnError(VolleyHttpHelper.this.context.getResources().getString(R.string.parameters_error));
                    else if (result.equals(VolleyHttpHelper.this.context.getResources().getString(R.string.does_not_exists)))
                        listener.OnError(VolleyHttpHelper.this.context.getResources().getString(R.string.user_does_not_exists));
                    else
                        listener.OnError(VolleyHttpHelper.this.context.getResources().getString(R.string.server_error));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.OnError(error.getMessage());
            }
        })
        {
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
}
