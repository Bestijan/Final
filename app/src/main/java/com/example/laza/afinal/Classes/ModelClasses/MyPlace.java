package com.example.laza.afinal.Classes.ModelClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.example.laza.afinal.Classes.Interfaces.IHolder;

/**
 * Created by LAZA on 11/14/2017.
 */

public class MyPlace implements IHolder, Parcelable{

    int ID;
    String Username;
    String PE;
    String MI;
    String Name;
    String Date_time;
    double Lat;
    double Lon;
    Bitmap myPlaceBitmap;
    String my_place_img;

    public MyPlace(int ID, String Username, String PE, String MI, String Name, String Date_time,
                   double Lat, double Lon, String myPlaceBitmap, double v){

        this.ID = ID;
        this.Username = Username;
        this.PE = PE;
        this.MI = MI;
        this.Name = Name;
        this.Date_time = Date_time;
        this.Lat = Lat;
        this.Lon = Lon;
    }

    protected MyPlace(Parcel in) {
        ID = in.readInt();
        Username = in.readString();
        PE = in.readString();
        MI = in.readString();
        Name = in.readString();
        Date_time = in.readString();
        Lat = in.readDouble();
        Lon = in.readDouble();
        myPlaceBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        my_place_img = in.readString();
    }

    public static final Creator<MyPlace> CREATOR = new Creator<MyPlace>() {
        @Override
        public MyPlace createFromParcel(Parcel in) {
            return new MyPlace(in);
        }

        @Override
        public MyPlace[] newArray(int size) {
            return new MyPlace[size];
        }
    };

    private Bitmap putStringToBitmap(String bitmapString){
        byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setMyPlaceBitmap(){
        this.myPlaceBitmap = putStringToBitmap(this.my_place_img);
    }

    public void setID(int id) { this.ID = id; }

    public Bitmap getMyPlaceBitmap(){
        return this.myPlaceBitmap;
    }

    public double getLat(){
        return this.Lat;
    }

    public double getLon(){
        return this.Lon;
    }

    public String getName(){
        return this.Name;
    }

    public int getID() {return this.ID; }

    public String getMI (){ return this.MI; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Username);
        dest.writeString(PE);
        dest.writeString(MI);
        dest.writeString(Name);
        dest.writeString(Date_time);
        dest.writeDouble(Lat);
        dest.writeDouble(Lon);
        dest.writeParcelable(myPlaceBitmap, flags);
        dest.writeString(my_place_img);
    }
}
