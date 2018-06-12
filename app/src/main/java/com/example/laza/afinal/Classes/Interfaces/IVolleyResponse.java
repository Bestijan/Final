package com.example.laza.afinal.Classes.Interfaces;

import android.content.Context;

/**
 * Created by LAZA on 11/23/2017.
 */

public interface IVolleyResponse {
    void OnResponse(Object response);
    void OnIDResponse(Object response);
    void NotifyResponse(Object response);
    void onSuccess();
    void OnError(String error);
    Context GetContext();
}
