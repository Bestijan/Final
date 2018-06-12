package com.example.laza.afinal.Activities.AuthActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laza.afinal.Activities.MainActivity.MainActivity;
import com.example.laza.afinal.Classes.Interfaces.EnableDisableInterface;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.HttpHelper;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.R;

import java.util.HashMap;

public class SignInFragment extends Fragment implements EnableDisableInterface, IVolleyResponse{

    private OnFragmentInteractionListener mListener;

    public SignInFragment() {
    }

    public static SignInFragment newInstance(int i) {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.sign_in_fragment, container, false);
        v.findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(getResources().getInteger(R.integer.register));
            }
        });

        v.findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = ((EditText)v.findViewById(R.id.editTextUsername)).getText().toString();
                String password = ((EditText)v.findViewById(R.id.editTextPassword)).getText().toString();

                if (!username.equals("") && !password.equals("")) {
                    if (username.length() >= getContext().getResources().getInteger(R.integer.username_size)
                            && password.length() >= getContext().getResources().getInteger(R.integer.password_size)) {
                        setControllsDisable();

                        HttpHelper signIn = new HttpHelper(SignInFragment.this);
                        signIn.SignInAccount(username, password);
                    }
                    else Toast.makeText(getContext(), getResources().getString(R.string.check_parameters_length),
                            Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getContext(), getResources().getString(R.string.not_enough_parameters), Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public void setControllsEnable() {
        getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        getActivity().findViewById(R.id.buttonSignIn).setEnabled(true);
        getActivity().findViewById(R.id.buttonRegister).setEnabled(true);
        getActivity().findViewById(R.id.editTextUsername).setEnabled(true);
        getActivity().findViewById(R.id.editTextPassword).setEnabled(true);
    }

    @Override
    public void setControllsDisable() {
        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        getActivity().findViewById(R.id.buttonSignIn).setEnabled(false);
        getActivity().findViewById(R.id.buttonRegister).setEnabled(false);
        getActivity().findViewById(R.id.editTextUsername).setEnabled(false);
        getActivity().findViewById(R.id.editTextPassword).setEnabled(false);
    }

    @Override
    public void OnResponse(Object response) {
        setControllsEnable();
        HashMap<String, String> hashMap = (HashMap<String, String>)response;
        Intent googleMap = new Intent(this.getContext(), MainActivity.class);
        SharedPreferencesHelper.getAccount().putUser(this.getContext(),
                hashMap.get(getResources().getString(R.string.username)), hashMap.get(getResources().getString(R.string.pic)));

        this.getContext().startActivity(googleMap);
        mListener.Finish();
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
                new android.support.v7.app.AlertDialog.Builder(SignInFragment.this.getContext());
        builder.setTitle(getResources().getString(R.string.volley_error));
        builder.setMessage(error);
        builder.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
        setControllsEnable();
    }

    @Override
    public Context GetContext() {
        return this.getContext();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int i);
        void Finish();
    }
}
