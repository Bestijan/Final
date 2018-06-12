package com.example.laza.afinal.Classes.AdapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

/**
 * Created by LAZA on 3/17/2018.
 */

public class SettingsListViewAdapter extends BaseAdapter {

    Context context;
    List<String> settings;

    public SettingsListViewAdapter(Context context, List<String> settings){
        this.context = context;
        this.settings = settings;
    }

    @Override
    public int getCount() {
        return this.settings.size();
    }

    @Override
    public Object getItem(int position) {
        return this.settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        String textSettings = this.settings.get(position);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    MyApplicationContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.options, null);
        }

        TextView txtListChild = view.findViewById(R.id.text);
        txtListChild.setText(textSettings);

        ((CheckBox) view.findViewById(R.id.checkbox)).setChecked(SharedPreferencesHelper
                .getAccount().getRouteSettings(context, textSettings));

        view.findViewById(R.id.checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesHelper.getAccount().getRouteSettings(context, textSettings))
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(textSettings);
                else FirebaseMessaging.getInstance().subscribeToTopic(textSettings);
                SharedPreferencesHelper.getAccount().putRouteSettings(context, textSettings);
            }
        });

        return view;
    }
}
