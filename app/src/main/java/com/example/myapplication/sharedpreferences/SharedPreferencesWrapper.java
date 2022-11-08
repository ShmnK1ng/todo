package com.example.myapplication.sharedpreferences;

import android.content.SharedPreferences;

public class SharedPreferencesWrapper implements AppIdentifier {
    private static final String APP_PREFERENCES_ID = "App_id";
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesWrapper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getID() {
            return sharedPreferences.getString(APP_PREFERENCES_ID, null);
    }

    @Override
    public void setID(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_PREFERENCES_ID, id);
        editor.apply();
    }
}
