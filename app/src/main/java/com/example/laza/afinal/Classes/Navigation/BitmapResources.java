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

    public Bitmap getDestLocB(){
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplicationContext.getContext().getResources(),
                R.drawable.circle_destination);
        return Bitmap.createScaledBitmap(bitmap, 60, 60, false);
    }
}
