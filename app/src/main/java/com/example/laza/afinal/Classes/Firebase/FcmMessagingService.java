package com.example.laza.afinal.Classes.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;

import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Activities.MainActivity.MainActivity;
import com.example.laza.afinal.R;
import com.example.laza.afinal.Activities.SplashScreenActivity.SplashScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by LAZA on 11/15/2017.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    public static String token;

    @Override
    public void onCreate() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {

            ArrayMap<String, String> t = (ArrayMap<String, String>) remoteMessage.getData();

            String token = t.get(MyApplicationContext.getContext().getResources().getString(R.string.token));
            int id = Integer.parseInt(t.get(MyApplicationContext.getContext().getResources().getString(R.string.id)));
            String username = t.get(MyApplicationContext.getContext().getResources().getString(R.string.username_upper_case));
            String pe = t.get(MyApplicationContext.getContext().getResources().getString(R.string.pe_upper_case));
            String mi = t.get(MyApplicationContext.getContext().getResources().getString(R.string.mi_upper_case));
            String name = t.get(MyApplicationContext.getContext().getResources().getString(R.string.name_upper_case));
            String date_time = t.get(MyApplicationContext.getContext().getResources().getString(R.string.date_time));
            double lat = Double.parseDouble(t.get(MyApplicationContext.getContext().getResources().getString(R.string.lat_upper_case)));
            double lon = Double.parseDouble(t.get(MyApplicationContext.getContext().getResources().getString(R.string.lon_upper_case)));

            MyPlace myPlace =
                    new MyPlace(id, username, pe, mi, name, date_time, lat, lon, null, 0);

            String my_token = getSharedPreferences("NavigationPrefs",
                    Context.MODE_PRIVATE).getString(MyApplicationContext.getContext()
                    .getResources().getString(R.string.token), "");

            if (!my_token.equals(token))
            {
                SetNotification(myPlace);
            }

        }
        catch (Exception e){
            String s = e.getMessage();
        }
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    private void SetNotification(MyPlace myPlace){

        PendingIntent contentIntent;

        boolean onDestroyed = getSharedPreferences("NavigationPrefs", Context.MODE_PRIVATE).getBoolean("onDestroyed", false);

        if (onDestroyed) {
            Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
            notificationIntent.putExtra(MyApplicationContext.getContext()
                    .getResources().getString(R.string.my_place_holder), (Parcelable) myPlace);
            contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        } else {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.putExtra(MyApplicationContext.getContext()
                    .getResources().getString(R.string.my_place_holder), (Parcelable) myPlace);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setAutoCancel(true)
                        .setContentTitle(MyApplicationContext.getContext().getString(R.string.new_place_event))
                        .setContentText(myPlace.getName())
                        .setContentIntent(contentIntent);

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
