package com.example.laza.afinal.Activities.MainActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.laza.afinal.Classes.AdapterClasses.MyPlacesListViewAdapter;
import com.example.laza.afinal.Classes.ModelClasses.MyPlace;
import com.example.laza.afinal.Classes.Interfaces.EnableDisableInterface;
import com.example.laza.afinal.Classes.Factory;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.Classes.Volley.VolleyHttpHelper;
import com.example.laza.afinal.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyPlacesFragment extends Fragment implements IVolleyResponse, EnableDisableInterface {

    private OnFragmentInteractionMainListener mListener;

    private boolean viewCreated;

    public static Fragment newInstance(int position) {
        return new MyPlacesFragment();
    }

    public MyPlacesFragment() {
        this.viewCreated = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_places, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControllsDisable();
        this.viewCreated = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionMainListener) {
            mListener = (OnFragmentInteractionMainListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.viewCreated)
        setControllsDisable();

        if (isVisibleToUser){
            getData();
        }
    }

    public void getData(){
        VolleyHttpHelper volleyHttpHelper = new VolleyHttpHelper().setUsername(SharedPreferencesHelper
                .getAccount()
                .getUsername(getContext()));
        volleyHttpHelper.send( getResources().getString(R.string.ip) +
                getResources().getString(R.string.get_my_places), MyPlacesFragment.this);
    }

    @Override
    public void OnResponse(Object response) {
        Factory factory = new Factory();
        getMyPlaces(factory.getObjects((JSONObject) response,
                getContext().getResources().getInteger(R.integer.CLASS_MY_PLACE)));
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

        setControllsEnable();

        final android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(MyPlacesFragment.this.getContext());
        builder.setTitle(getResources().getString(R.string.volley_error));
        builder.setMessage(error);
        builder.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public Context GetContext() {
        return this.getContext();
    }

    @Override
    public void setControllsEnable() {
        this.getView().findViewById(R.id.progressBarGoogleMap).setVisibility(View.INVISIBLE);
        this.getView().findViewById(R.id.listViewMyPlaces).setEnabled(true);
    }

    @Override
    public void setControllsDisable() {
        this.getView().findViewById(R.id.progressBarGoogleMap).setVisibility(View.VISIBLE);
        this.getView().findViewById(R.id.listViewMyPlaces).setEnabled(false);
    }

    public void setPlace(MyPlace myPlace){
        mListener.onFragmentInteractionMain(myPlace);
    }

    public void deletePlace(MyPlace myPlace){
        mListener.onFragmentInteractionDelete(myPlace);
    }

    public void getMyPlaces(ArrayList<MyPlace> myPlaceData){

        setControllsEnable();

        if (myPlaceData.size() > 0) {
            MyPlacesListViewAdapter listViewAdapter =
                    new MyPlacesListViewAdapter(MyPlacesFragment.this, myPlaceData, this.getContext());
            ListView listView = MyPlacesFragment.this.getActivity().findViewById(R.id.listViewMyPlaces);
            listView.setAdapter(listViewAdapter);
        }
        else {
            Toast.makeText(this.getContext(),
                    this.getContext().getResources().getString(R.string.no_results), Toast.LENGTH_LONG).show();
        }
    }


    public interface OnFragmentInteractionMainListener {
        void onFragmentInteractionMain(MyPlace myPlace);
        void onFragmentInteractionDelete(MyPlace myPlace);
    }

}
