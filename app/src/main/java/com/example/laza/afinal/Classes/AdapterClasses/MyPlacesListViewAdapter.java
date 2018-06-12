package com.example.laza.afinal.Classes.AdapterClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.HttpHelper;
import com.example.laza.afinal.Classes.Navigation.Geocoder;
import com.example.laza.afinal.Activities.MainActivity.MyPlacesFragment;
import com.example.laza.afinal.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by LAZA on 11/14/2017.
 */

public class MyPlacesListViewAdapter extends BaseAdapter implements IVolleyResponse {

    ArrayList<MyPlace> myPlaceData = new ArrayList<MyPlace>();
    MyPlacesFragment myPlacesFragment;
    Context context;
    int index;

    public MyPlacesListViewAdapter(MyPlacesFragment myPlacesFragment,
                                   ArrayList<MyPlace> myPlaceData,
                                   Context context){
        this.myPlacesFragment = myPlacesFragment;
        this.myPlaceData = myPlaceData;
        this.context = context;
    }


    @Override
    public int getCount() {
        return myPlaceData.size();
    }

    @Override
    public Object getItem(int i) {
        return myPlaceData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    this.myPlacesFragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.places_item, null);
        }

        ImageView routeImageView = view.findViewById(R.id.placeImageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Resources r = MyApplicationContext.getContext().getResources();
        int height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics()));

        int width = (int)(displayMetrics.widthPixels * 0.3);

        Bitmap img = Bitmap.createScaledBitmap(myPlaceData.get(i).getMyPlaceBitmap(), width, height, false);

        routeImageView.setImageBitmap(img);

        Geocoder geocoder = new Geocoder(new LatLng(myPlaceData.get(i).getLat(),
                                                    myPlaceData.get(i).getLon()));

        if (geocoder.getAddress() != null) {
            String[] address = geocoder.getAddress().split(",");


            TextView textView = view.findViewById(R.id.placeNameTextView);
            textView.setText(context.getResources().getString(R.string.name_my_places) + " " + myPlaceData.get(i).getName());
            textView = view.findViewById(R.id.countryTextView);
            textView.setText(context.getResources().getString(R.string.country_my_places) + " " + address[2]);
            textView = view.findViewById(R.id.cityTextView);
            textView.setText(context.getResources().getString(R.string.city_my_places) + " " + address[1]);
            textView = view.findViewById(R.id.addressTextView);
            textView.setText(context.getResources().getString(R.string.address_my_places) + " " + address[0]);
        }
        else {
            TextView textView = view.findViewById(R.id.placeNameTextView);
            textView.setText(context.getResources().getString(R.string.name_my_places) + " " + myPlaceData.get(i).getName());
            textView = view.findViewById(R.id.countryTextView);
            textView.setText("Lat" + " " + ((Double) myPlaceData.get(i).getLat()).toString());
            textView = view.findViewById(R.id.cityTextView);
            textView.setText("Lon" + " " + ((Double) myPlaceData.get(i).getLon()).toString());
        }

        final int index = i;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //routesFragment.setRoute(routeHolders.get(indeks));
                myPlacesFragment.setPlace(myPlaceData.get(index));
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteMyPlaceDialog(index);
                return false;
            }
        });

        return view;
    }




    public void deleteMyPlaceDialog(final int index){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog.setTitle(context.getResources().getString(R.string.delete_my_places_title));
        dialog.setMessage(context.getResources().getString(R.string.delete_my_places_message));
        dialog.setPositiveButton(context.getResources().getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        deleteMyPlace(index);
                    }
                });
        dialog.setNegativeButton(context.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void deleteMyPlace(int index){
        this.index = index;
        HttpHelper httpHelper = new HttpHelper(MyPlacesListViewAdapter.this);
        httpHelper.DeleteMyPlace(myPlaceData.get(index).getID());
    }

    @Override
    public void OnResponse(Object response) {
        myPlacesFragment.deletePlace(myPlaceData.get(index));
        myPlaceData.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public void OnIDResponse(Object response) {
    }

    @Override
    public void NotifyResponse(Object response){

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void OnError(String error) {

    }

    @Override
    public Context GetContext() {
        return null;
    }
}
