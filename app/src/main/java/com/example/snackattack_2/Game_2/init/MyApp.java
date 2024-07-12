package com.example.snackattack_2.Game_2.init;

import android.app.Application;

import com.example.snackattack_2.Game_2.data.GameManager;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MySignal.init(this);
        MySP.init(this);
        MyGPS.init(this);
        GameManager.init();
    }
}
