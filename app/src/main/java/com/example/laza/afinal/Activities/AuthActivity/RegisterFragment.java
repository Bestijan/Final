package com.example.laza.afinal.Activities.AuthActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laza.afinal.Activities.MainActivity.MainActivity;
import com.example.laza.afinal.Classes.Interfaces.EnableDisableInterface;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.HttpHelper;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment implements EnableDisableInterface, IVolleyResponse{

    private OnFragmentInteractionListener mListener;
    private ImageView imageView;
    private Bitmap bitmap;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance(int i) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.register_fragment, container, false);
            v.findViewById(R.id.buttonSignInReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(getResources().getInteger(R.integer.sing_up));
            }
        });

        v.findViewById(R.id.buttonRegisterReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText)v.findViewById(R.id.editTextUsernameReg)).getText().toString();
                String password = ((EditText)v.findViewById(R.id.editTextPasswordReg)).getText().toString();
                Bitmap profilePic = ((BitmapDrawable)((ImageView)v.findViewById(R.id.userImageViewReg)).getDrawable()).getBitmap();

                if (!username.equals("") && !password.equals("") && profilePic != null) {
                    if (username.length() >= getContext().getResources().getInteger(R.integer.username_size)
                            && password.length() >= getContext().getResources().getInteger(R.integer.password_size)) {
                        setControllsDisable();

                        HttpHelper registerAccount = new HttpHelper(RegisterFragment.this);
                        registerAccount.RegisterAccount(username, password, profilePic);
                    }
                    else Toast.makeText(getContext(), getResources().getString(R.string.check_parameters_length), Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getContext(), getResources().getString(R.string.not_enough_parameters), Toast.LENGTH_LONG).show();
            }
        });

        imageView = v.findViewById(R.id.userImageViewReg);

                v.findViewById(R.id.userImageViewReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.photo_action_title));
                builder.setMessage(getResources().getString(R.string.photo_action_messagge));

                builder.setPositiveButton(getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        takeFromGallery();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                });

                builder.show();
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, getResources().getInteger(R.integer.image_camera));
        }
    }

    private void takeFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String path = pictureDir.getPath();
        Uri data = Uri.parse(path);

        intent.setDataAndType(data, "image/*");
        startActivityForResult(intent, getResources().getInteger(R.integer.image_gallery));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap imageBitmap = null;
        int width = 0, height = 0;

        if (requestCode == getResources().getInteger(R.integer.image_camera) && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            width = imageBitmap.getWidth();
            height = imageBitmap.getHeight();

            if (imageView.getWidth() < width)
                width = imageView.getWidth();
            if (imageView.getHeight() < height)
                height = imageView.getHeight();

            imageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, width, height, false));
        }
        else if (requestCode == getResources().getInteger(R.integer.image_gallery) && resultCode == RESULT_OK){
            try {
                imageBitmap = BitmapFactory.decodeStream(getContext().
                        getContentResolver().openInputStream(data.getData()));

                width = imageBitmap.getWidth();
                height = imageBitmap.getHeight();

                if (imageView.getWidth() < width)
                    width = imageView.getWidth();
                if (imageView.getHeight() < height)
                    height = imageView.getHeight();

                bitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);

                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch(Exception e){
                String s = e.getMessage();
            }
        }
    }

    @Override
    public void setControllsEnable() {
        getActivity().findViewById(R.id.progressBarReg).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.buttonSignInReg).setEnabled(true);
        getActivity().findViewById(R.id.buttonRegisterReg).setEnabled(true);
        getActivity().findViewById(R.id.userImageViewReg).setEnabled(true);
        getActivity().findViewById(R.id.editTextPasswordReg).setEnabled(true);
        getActivity().findViewById(R.id.editTextUsernameReg).setEnabled(true);
    }

    @Override
    public void setControllsDisable() {
        getActivity().findViewById(R.id.progressBarReg).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.buttonSignInReg).setEnabled(false);
        getActivity().findViewById(R.id.buttonRegisterReg).setEnabled(false);
        getActivity().findViewById(R.id.userImageViewReg).setEnabled(false);
        getActivity().findViewById(R.id.editTextPasswordReg).setEnabled(false);
        getActivity().findViewById(R.id.editTextUsernameReg).setEnabled(false);
    }

    @Override
    public void OnResponse(Object response) {
        setControllsEnable();
        Intent googleMap = new Intent(this.getContext(), MainActivity.class);
        //googleMap.putExtra(DataHolder.username, hashMap.get(DataHolder.username));
        //googleMap.putExtra(DataHolder.pic, hashMap.get(DataHolder.pic));

        HashMap<String, String> hashMap = (HashMap<String, String>) response;
        SharedPreferencesHelper.getAccount().putUser(RegisterFragment.this.getContext(),
                hashMap.get(getResources().getString(R.string.username)),
                hashMap.get(getResources().getString(R.string.pic)));
        mListener.Finish();
        RegisterFragment.this.startActivity(googleMap);
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
                new android.support.v7.app.AlertDialog.Builder(RegisterFragment.this.getContext());
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int i);
        void Finish();
    }
}
