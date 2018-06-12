package com.example.laza.afinal.Classes.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by LAZA on 10/17/2017.
 */

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private static Context mCtx;
    private RequestQueue requestQueue;

    private VolleySingleton(Context mCtx)
    {
        this.mCtx = mCtx;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized VolleySingleton getmInstance(Context c)
    {
        if (mInstance == null)
            mInstance = new VolleySingleton(c);
        return mInstance;
    }

    public<T> void addToRequestQueue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
