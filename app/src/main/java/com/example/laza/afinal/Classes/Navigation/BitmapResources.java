package com.example.laza.afinal.Classes.Navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by LAZA on 11/26/2017.
 */

public class BitmapResources {

    public BitmapDescriptor getCurrLocBD(){
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                R.drawable.circle_marker);
        bitmap = Bitmap.createScaledBitmap(bitmap, 20, 20, false);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public Bitmap getCurrLocB(){
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                R.drawable.circle_marker);
        return Bitmap.createScaledBitmap(bitmap, 20, 20, false);
    }

    public BitmapDescriptor getDestLocBD(){
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                R.drawable.circle_destination );
        bitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, false);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public Bitmap getMarkerBitmap(String markerName){
        switch (markerName)
        {
            case "Restaurant":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.restaurant_marker);
                return Bitmap.createScaledBitmap(bitmap, 70, 70, false);
            }
            case "Accident":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.accident_marker);
                return Bitmap.createScaledBitmap(bitmap, 70, 70, false);
            }
            case "Flood":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.flood_marker);
                return Bitmap.createScaledBitmap(bitmap, 80, 70, false);
            }
            case "Police":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.police_marker);
                return Bitmap.createScaledBitmap(bitmap, 70, 70, false);
            }
            case "Nature":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.nature_marker);
                return Bitmap.createScaledBitmap(bitmap, 80, 70, false);
            }
            case "Park":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.park_marker);
                return Bitmap.createScaledBitmap(bitmap, 80, 70, false);
            }
            case "Potholes":{
                Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                        R.drawable.potholes_marker);
                return Bitmap.createScaledBitmap(bitmap, 80, 70, false);
            }
        }
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                R.drawable.circle_destination);
        return Bitmap.createScaledBitmap(bitmap, 60, 60, false);
    }

    public Bitmap getDestLocB(){
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                R.drawable.circle_destination);
        return Bitmap.createScaledBitmap(bitmap, 60, 60, false);
    }
}
