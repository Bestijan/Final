package com.example.laza.afinal.Activities.UserAccountActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.laza.afinal.Activities.AuthActivity.AuthActivity;
import com.example.laza.afinal.Classes.Interfaces.EnableDisableInterface;
import com.example.laza.afinal.Classes.Interfaces.IVolleyResponse;
import com.example.laza.afinal.Classes.MyApplicationContext;
import com.example.laza.afinal.Classes.HttpHelper;
import com.example.laza.afinal.Classes.SharedPreferencesHelper;
import com.example.laza.afinal.Classes.Volley.VolleyHttpHelper;
import com.example.laza.afinal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import static com.example.laza.afinal.Classes.MyApplicationContext.getContext;

public class UserAccountActivity extends AppCompatActivity implements IVolleyResponse, EnableDisableInterface {

    ImageView imageView;
    Bitmap bitmap;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        imageView = findViewById(R.id.userImageViewAccount);
        imageView.setImageBitmap(SharedPreferencesHelper.getAccount()
                .loadImageFromStorage(SharedPreferencesHelper.getAccount().getUserPic(this)));

        username = findViewById(R.id.editTextUsernameAccount);

        password = findViewById(R.id.editTextPasswordAccount);

        Button buttonUpdateAccount = findViewById(R.id.buttonUpdateAccount);

        buttonUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyHttpHelper volleyHttpHelper = new VolleyHttpHelper()
                        .setUsername(SharedPreferencesHelper.getAccount()
                                .getUsername(MyApplicationContext.getContext()));

                if(username.getText().toString().length() >= getResources().getInteger(R.integer.username_size))
                    volleyHttpHelper.setNewUsername(username.getText().toString());

                if(password.getText().toString().length() >= getResources().getInteger(R.integer.password_size))
                    volleyHttpHelper.setPassword(password.getText().toString());

                if(bitmap != null) {
                    volleyHttpHelper.setBitmap(bitmap);
                }
                volleyHttpHelper.send(getResources().getString(R.string.ip) +
                        getResources().getString(R.string.update_account), UserAccountActivity.this);

                setControllsDisable();
            }
        });

        Button buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);

        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountDialog();
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.getText().toString().length() == 0)
                    buttonUpdateAccount.setEnabled(true);
                else if (username.getText().toString().length() >= getResources().getInteger(R.integer.username_size))
                    buttonUpdateAccount.setEnabled(true);
                else buttonUpdateAccount.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().length() == 0)
                    buttonUpdateAccount.setEnabled(true);
                else if (password.getText().toString().length() >= getResources().getInteger(R.integer.password_size))
                    buttonUpdateAccount.setEnabled(true);
                else buttonUpdateAccount.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        findViewById(R.id.userImageViewAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountActivity.this);
                builder.setTitle(R.string.photo_action_title);
                builder.setMessage(R.string.photo_action_messagge);

                builder.setPositiveButton(R.string.gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        takeFromGallery();
                    }
                });

                builder.setNegativeButton(R.string.camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dispatchTakePictureIntent();
                    }
                });

                builder.show();
            }
        });

    }

    private void deleteAccountDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.delete_title));
        builder.setMessage(getResources().getString(R.string.delete_message));

        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HttpHelper httpHelper = new HttpHelper(UserAccountActivity.this);
                        httpHelper.DeleteAccount();
                        setControllsDisable();
                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @Override
    public void setControllsEnable() {
        findViewById(R.id.progressBarAccount).setVisibility(View.INVISIBLE);
        findViewById(R.id.buttonUpdateAccount).setEnabled(true);
        findViewById(R.id.buttonDeleteAccount).setEnabled(true);
        findViewById(R.id.userImageViewAccount).setEnabled(true);
        findViewById(R.id.editTextPasswordAccount).setEnabled(true);
        findViewById(R.id.editTextUsernameAccount).setEnabled(true);
    }

    @Override
    public void setControllsDisable() {
        findViewById(R.id.progressBarAccount).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonUpdateAccount).setEnabled(false);
        findViewById(R.id.buttonDeleteAccount).setEnabled(false);
        findViewById(R.id.userImageViewAccount).setEnabled(false);
        findViewById(R.id.editTextPasswordAccount).setEnabled(false);
        findViewById(R.id.editTextUsernameAccount).setEnabled(false);
    }

    public void removeAccount(){
        removeBitmap();
        removeUser();
    }

    private void removeBitmap(){
        Intent intent = new Intent(UserAccountActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ContextWrapper cw = new ContextWrapper(MyApplicationContext.getContext());
        // path to /data/data/yourapp/app_data/imageDir

        File directory = cw.getDir(getResources().getString(R.string.img_dir), Context.MODE_PRIVATE);
        // Create imageDir

        File mypath=new File(directory,SharedPreferencesHelper
                .getAccount().getUsername(MyApplicationContext.getContext()) +
                getResources().getString(R.string.jpg));
        mypath.delete();
    }

    private void removeUser(){
        SharedPreferencesHelper.getAccount().removeUser(MyApplicationContext.getContext());
        finish();
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

        intent.setDataAndType(data, getResources().getString(R.string.img_folder));
        startActivityForResult(intent, getResources().getInteger(R.integer.image_gallery));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap imageBitmap = null;
        int width = 0, height = 0;

        if (requestCode == getResources().getInteger(R.integer.image_camera) && resultCode == RESULT_OK) {

            removeBitmap();

            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get(getResources().getString(R.string.data));

            SharedPreferencesHelper.getAccount().putUserPic(MyApplicationContext.getContext(), imageBitmap);

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
    public void OnResponse(Object response) {
        setControllsEnable();
        if (bitmap != null)
            SharedPreferencesHelper.getAccount().putUserPic(MyApplicationContext.getContext(), bitmap);
        if (((JSONObject) response).has(getResources().getString(R.string.username)))
            try {
                String username = ((JSONObject)response).get(getResources().getString(R.string.username)).toString();
                SharedPreferencesHelper.getAccount()
                        .putUsername(MyApplicationContext.getContext(), username);
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void OnIDResponse(Object response) {
        removeAccount();
        setControllsEnable();
    }

    @Override
    public void NotifyResponse(Object response) {

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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
