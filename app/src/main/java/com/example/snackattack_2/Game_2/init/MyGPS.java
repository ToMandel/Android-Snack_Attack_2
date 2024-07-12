package com.example.snackattack_2.Game_2.init;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import im.delight.android.location.SimpleLocation;

public class MyGPS {
    @SuppressLint("StaticFieldLeak")
    private static MyGPS instance;
    private final Context context;
    private final SimpleLocation location;

    public static void init(Context context) {
        if (instance == null) {
            instance = new MyGPS(context);
        }
    }

    private MyGPS(Context context) {
        this.context = context;
        location = new SimpleLocation(context);
    }

    public static MyGPS getInstance() {
        return instance;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public void start() {
        location.beginUpdates();
    }

    public void stop() {
        location.endUpdates();
    }

    public void checkPermissions(AppCompatActivity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }
}

