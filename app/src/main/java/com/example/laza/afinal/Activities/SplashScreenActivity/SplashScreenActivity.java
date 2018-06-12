package com.example.laza.afinal.Activities.SplashScreenActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.laza.afinal.Activities.AuthActivity.AuthActivity;
import com.example.laza.afinal.Activities.MainActivity.MainActivity;
import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        MyApplicationContext.setContext(getBaseContext());

        if (!SharedPreferencesHelper.getAccount().hasToken(this))
            SharedPreferencesHelper.getAccount().putToken(this, FirebaseInstanceId.getInstance().getToken());

        PremmisionReguest();

    }

    private void startSplashScreen(){
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                String my_place_holder = getResources().getString(R.string.my_place_holder);
                String string = SharedPreferencesHelper.getAccount().getUsername(SplashScreenActivity.this);
                boolean hasMyPlaceHolder = SplashScreenActivity.this.getIntent().hasExtra(my_place_holder);

                if (string.equals("")){
                    intent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                }
                else {
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra(getResources().getString(R.string.pic),
                            SharedPreferencesHelper.getAccount().getUserPic(SplashScreenActivity.this));
                    intent.putExtra(getResources().getString(R.string.username),
                            SharedPreferencesHelper.getAccount().getUsername(SplashScreenActivity.this));
                }

                if (hasMyPlaceHolder)
                    intent.putExtra(my_place_holder,
                            (MyPlace)SplashScreenActivity.this.getIntent().getParcelableExtra(my_place_holder));

                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startSplashScreen();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void PremmisionReguest(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MyApplicationContext.getContext().getResources().getInteger(R.integer.request_location));

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else {
            startSplashScreen();
        }
    }

}
