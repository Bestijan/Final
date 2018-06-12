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

import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.ModelClasses.RouteHolder;
import com.example.laza.afinal.Classes.HttpHelper;
import com.example.laza.afinal.Classes.Navigation.Geocoder;
import com.example.laza.afinal.R;
import com.example.laza.afinal.Activities.MainActivity.RoutesFragment;

import java.util.ArrayList;

/**
 * Created by LAZA on 11/12/2017.
 */

public class RoutesListViewAdapter extends BaseAdapter implements IVolleyResponse{

    ArrayList<RouteHolder> routeHolders = new ArrayList<RouteHolder>();
    RoutesFragment routesFragment;
    Context context;
    int index;

    public RoutesListViewAdapter(RoutesFragment routesFragment, ArrayList<RouteHolder> routeHolders, Context context){
        this.routesFragment = routesFragment;
        this.routeHolders = routeHolders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return routeHolders.size();
    }

    @Override
    public Object getItem(int i) {
        return routeHolders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.routesFragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.routes_item, null);
        }

        ImageView routeImageView = view.findViewById(R.id.routesImageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Resources r = MyApplicationContext.getContext().getResources();
        int height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics()));

        int width = (int)(displayMetrics.widthPixels * 0.3);

        Bitmap img = Bitmap.createScaledBitmap(routeHolders.get(i).getRouteFromString(), width, height, false);

        routeImageView.setImageBitmap(img);

        Geocoder geocoder = new Geocoder(routeHolders.get(i).getStart());

        if (geocoder.getAddress() != null) {
            String[] address = geocoder.getAddress().split(",");

            TextView textView = view.findViewById(R.id.countryStartTextView);
            textView.setText(context.getResources().getString(R.string.country_my_places) + " " + address[2]);
            textView = view.findViewById(R.id.cityStartTextView);
            textView.setText(context.getResources().getString(R.string.city_my_places) + " " + address[1]);
            textView = view.findViewById(R.id.addressStartTextView);
            textView.setText(context.getResources().getString(R.string.address_my_places) + " " + address[0]);
        }


        geocoder = new Geocoder(routeHolders.get(i).getDestination());

        if (geocoder.getAddress() != null) {
            String[] address = geocoder.getAddress().split(",");

            TextView textView = view.findViewById(R.id.countryDestTextView);
            textView.setText(context.getResources().getString(R.string.country_my_places) + " " +  address[2]);
            textView = view.findViewById(R.id.cityDestTextView);
            textView.setText(context.getResources().getString(R.string.city_my_places) + " " + address[1]);
            textView = view.findViewById(R.id.addressDestTextView);
            textView.setText(context.getResources().getString(R.string.address_my_places) + " " +  address[0]);
        }
        final int indeks = i;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routesFragment.setRoute(routeHolders.get(indeks));
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteRouteDialog(indeks);
                return false;
            }
        });

        return view;
    }


    public void deleteRouteDialog(final int index){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog.setTitle(context.getResources().getString(R.string.delete_my_routes_title));
        dialog.setMessage(context.getResources().getString(R.string.delete_my_places_message));
        dialog.setPositiveButton(context.getResources().getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        deleteRoute(index);
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

    public void deleteRoute(int index){
        this.index = index;
        HttpHelper httpHelper = new HttpHelper(RoutesListViewAdapter.this);
        httpHelper.DeleteRoute(routeHolders.get(index).getId());
    }

    @Override
    public void OnResponse(Object response) {
        routeHolders.remove(index);
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
