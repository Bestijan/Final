package com.example.laza.afinal.Classes.Navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by LAZA on 11/25/2017.
 */

public class Dialogs {

    private int openningDialog;
    private MediatorMap mediatorMap;
    private Context context;

    private static int eventIndex = 0;
    private static int placeIndex = 0;

    public Dialogs(MediatorMap mediatorMap, Context context){
        this.mediatorMap = mediatorMap;
        this.context = context;
        this.openningDialog = 0;
    }

    public void setMediatorMap(MediatorMap mediatorMap){
        this.mediatorMap = mediatorMap;
    }

    public void setOpeningDialog(int setDialog){
        switch (setDialog){
            case 0:{
                this.openningDialog = 0;
                break;
            }
            case 1:{
                this.openningDialog = 1;
                break;
            }
        }
    }

    public void showLocationSourceDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.locationTitle));
        dialog.setMessage(context.getResources().getString(R.string.locationMessage));
        dialog.setPositiveButton(context.getResources().getString(R.string.positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplicationContext.getContext().startActivity(myIntent);
                    }
                });
        dialog.setNegativeButton(context.getResources().getString(R.string.negative),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void onMapClick(){
        switch (openningDialog) {
            case 0: {
                openAddingLocationDialog();
                break;
            }
            case 1:{
                addNewRoute();
                break;
            }
        }
    }

    public void openAddingLocationDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(MyApplicationContext.getContext().getString(R.string.title_choose_action));

        dialog.setItems(context.getResources().getStringArray(R.array.add_actions), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0: {
                        createNewRoute();
                        setOpeningDialog(1);
                        break;
                    }
                    case 1: {
                        addEventDialog();
                        break;
                    }
                    case 2: {
                        addPlaceDialog();
                        break;
                    }
                    case 3:{
                        break;
                    }
                }
            }
        });
        dialog.show();
    }

    public void addNewRoute(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.title_choose_action));

        String[] strings = new String[0];

        if (!mediatorMap.getRouteStarted())
            strings = context.getResources().getStringArray(R.array.start_new_route);
        else strings = context.getResources().getStringArray(R.array.add_new_route);


        dialog.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0: {
                        if (mediatorMap.getRouteStarted()) {
                            mediatorMap.stopRoute();
                            setOpeningDialog(0);
                        }
                        else {
                            mediatorMap.setRouteStarted();
                        }
                        break;
                    }
                    case 1:{
                        mediatorMap.addWaypoint();
                        break;
                    }
                    case 2: {
                        addEventDialog();
                        break;
                    }
                    case 3: {
                        addPlaceDialog();
                        break;
                    }
                    case 4:{
                        break;
                    }
                }
            }
        });
        //setOpeningDialog(openAddingDialog);
        dialog.show();
    }

    public void stopRoute(){
        this.openningDialog = 0;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.stop_route_title));
        dialog.setMessage(context.getResources().getString(R.string.stop_route_messagge));

        dialog.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        dialog.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    private void addWaypoint(){
        this.mediatorMap.addWaypoint();
    }

    private void createNewRoute(){
        this.mediatorMap.createNewRoute();
        this.setOpeningDialog(context.getResources().getInteger(R.integer.newRouteDialog));
    }

    private void addEventDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.title_choose_action));
        dialog.setSingleChoiceItems(context.getResources().getStringArray(R.array.events), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventIndex = i;
            }
        });
        dialog.setPositiveButton(context.getResources().getString(R.string.send_event), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mediatorMap.onEventChoice(context.getResources().getStringArray(R.array.events)[eventIndex]);
            }
        });

        dialog.show();
    }

    private void addPlaceDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.title_choose_action));

        String[] places = context.getResources().getStringArray(R.array.places);
        String[] place_mark = context.getResources().getStringArray(R.array.place_mark);
        String[] place_include = context.getResources().getStringArray(R.array.place_include);

        dialog.setSingleChoiceItems(places, placeIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                placeIndex = i;
            }
        });
        dialog.setPositiveButton(context.getResources().getString(R.string.send_place), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String mi = "";
                String place = "";
                if ((new ArrayList<String>(Arrays.asList(place_mark))).contains(places[placeIndex])) {
                    mi = context.getResources().getString(R.string.MARK);
                    place = places[placeIndex];
                }
                else if ((new ArrayList<String>(Arrays.asList(place_include))).contains(places[placeIndex])) {
                    mi = context.getResources().getString(R.string.INCLUDE);
                    place = places[placeIndex];
                }
                mediatorMap.onPlaceChoice(mi, place);
            }
        });

        dialog.show();
    }


    public void saveNewRouteDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.save_route_title));
        dialog.setMessage(context.getResources().getString(R.string.save_route_messagge));
        dialog.setPositiveButton(context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediatorMap.onSaveRoute();
                    }
                });
        dialog.setNegativeButton(context.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        dialog.show();
    }

    public void UnwantedRerouteDialog(String name){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.new_location_unwanted_reroute) + " " + name);
        dialog.setMessage(context.getResources().getString(R.string.rerouting));
        dialog.setPositiveButton(context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediatorMap.UnwantedReroute();
                    }
                });
        dialog.setNegativeButton(context.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        dialog.show();
    }

    public void WantedRerouteDialog(String name){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.new_location_wanted_reroute) + " " + name);
        dialog.setMessage(context.getResources().getString(R.string.rerouting));
        dialog.setPositiveButton(context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediatorMap.WantedRerroute();
                    }
                });
        dialog.setNegativeButton(context.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        dialog.show();
    }

    public void onError(String error){
        final android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.volley_error));
        builder.setMessage(error);
        builder.setNeutralButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

}
