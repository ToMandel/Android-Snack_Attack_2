package com.example.snackattack_2.Game_2.init;

import android.content.Context;
import android.content.SharedPreferences;

public class MySP {

    private static final String SP_FILE_NAME = "SP_FILE_NAME";
    private static MySP instance;

    private SharedPreferences preferences = null;

    private MySP(Context context) {
        preferences = context.getSharedPreferences("DB_FILE", Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySP(context);
        }
    }

    public static MySP getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MySP is not initialized, call init() method first.");
        }
        return instance;
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int readInt(String key, int def) {
        return preferences.getInt(key, def);
    }
}
