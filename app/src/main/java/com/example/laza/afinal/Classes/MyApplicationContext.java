package com.example.laza.afinal.Classes;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by LAZA on 11/26/2017.
 */

public class MyApplicationContext {

    private static Context context;

    private MyApplicationContext(){
    }

    public static void setContext(Context appContext){
        context = appContext;
    }

    public static Context getContext(){
        return context;
    }
}
