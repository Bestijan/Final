package com.example.laza.afinal.Classes.Firebase;

import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by LAZA on 10/17/2017.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {

    // Obavlja smeštanje tokena koji dobija od Firebase-a u SharedPreferences
    // i šalje http serveru

    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferencesHelper.getAccount().putToken(MyApplicationContext.getContext(), recent_token);
        FcmMessagingService.token = recent_token;

        super.onTokenRefresh();
    }
}
