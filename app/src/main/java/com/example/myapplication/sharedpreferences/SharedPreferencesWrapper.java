package com.example.myapplication.sharedpreferences;

import android.content.SharedPreferences;

public class SharedPreferencesWrapper implements TodoSharedPreferences {
    private static final String APP_PREFERENCES_ID = "App_id";
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesWrapper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getID() {
        if (sharedPreferences.contains(APP_PREFERENCES_ID)) {
            return sharedPreferences.getString(APP_PREFERENCES_ID, "");
        } else return null;
    }
}
