package com.example.laza.afinal.Classes;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.laza.afinal.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by LAZA on 10/23/2017.
 */

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper accountHelper;

    private SharedPreferencesHelper() {
    }

    public static SharedPreferencesHelper getAccount() {
        if (accountHelper == null)
            return accountHelper = new SharedPreferencesHelper();
        return accountHelper;
    }

    private SharedPreferences getSharedPreferences(Context context) {

        return context.getSharedPreferences("NavigationPrefs", Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        return editor;
    }

    public String getUsername(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String returnStr = sharedPreferences
                .getString(MyApplicationContext.getContext().getResources().getString(R.string.username), "");
        return returnStr;
    }

    public String getUserPic(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String returnStr =
                sharedPreferences.getString(MyApplicationContext.getContext().getResources().getString(R.string.usernamePic), "");
        return returnStr;
    }

    public void putUser(Context context, String name, String pic) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(MyApplicationContext.getContext().getResources().getString(R.string.username), name);
        editor.putString(MyApplicationContext.getContext().getResources().getString(R.string.usernamePic),
                saveToInternalStorage(context, name, pic));
        editor.commit();
    }

    public void putUsername(Context context, String name) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(MyApplicationContext.getContext().getResources().getString(R.string.username), name);
        editor.commit();
    }

    public void removeUser(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(MyApplicationContext.getContext().getResources().getString(R.string.username));
        editor.remove(MyApplicationContext.getContext().getResources().getString(R.string.pic));
        editor.commit();
    }

    public void putUserPic(Context context, Bitmap bitmap) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(MyApplicationContext.getContext().getResources().getString(R.string.usernamePic),
                saveBitmapToInternalStorage(context, getUsername(context), bitmap));
        editor.commit();
    }

    private String saveBitmapToInternalStorage(Context context, String name, Bitmap bitmap){

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File mypath=new File(directory,name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private String saveToInternalStorage(Context context, String name, String pic){

        byte[] bytes = Base64.decode(pic, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File mypath=new File(directory,name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path)
    {
        Bitmap b = null;
        try {
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return b;
    }

    public boolean hasUsername(Context context, String username) {
        return getSharedPreferences(context).contains(username);
    }

    public void putRouteSettings(Context context, String setting){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(setting, !getRouteSettings(context, setting));
        editor.commit();
    }

    public boolean getRouteSettings(Context context, String setting){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(setting, false);
    }

    public void putSubscribe(Context context, String setting){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("subscribe_" + setting, !getSubscribe(context, setting));
        editor.commit();
    }

    public boolean getSubscribe(Context context, String setting){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean("subscribe_" + setting, false);
    }

    public void putToken(Context context, String token) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String returnStr = sharedPreferences.getString("token", "");
        return returnStr;
    }

    public boolean hasToken(Context context){
        return getSharedPreferences(context).contains(context.getResources().getString(R.string.token));
    }

    public void putOnDestroyed(Context context, boolean onDestroyed){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean("onDestoyed", onDestroyed);
        editor.commit();
    }

    public boolean getOnDestroyed(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean("onDestroyed", false);
    }

    public String getRouteStrings(Context context){
        String[] marks_include = context.getResources().getStringArray(R.array.marks_include);

        ArrayList<String> settings = new ArrayList<String>();
        String names = "";
        for (int i = 0; i < marks_include.length; i++){
            if (getRouteSettings(context, marks_include[i])){
                settings.add("Name = " + "'" + marks_include[i] + "'");
            }
        }

        for(int i = 0; i < settings.size(); i++){
            if (i != settings.size() - 1)
                names += settings.get(i) + " OR ";
            else names += settings.get(i);
        }

        if (names.length() != 0)
            names = "WHERE " + names + " ";

        return names;
    }
}
