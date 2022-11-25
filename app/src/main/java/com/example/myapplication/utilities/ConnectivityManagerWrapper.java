package com.example.myapplication.utilities;

import android.net.ConnectivityManager;

public class ConnectivityManagerWrapper implements ConnectionNetworkInfo {

    private final ConnectivityManager connectivityManager;

    public ConnectivityManagerWrapper(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    @Override
    public Boolean isConnected() {
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
