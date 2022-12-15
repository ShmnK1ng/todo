package com.example.myapplication.network;

import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;

public class GetAppIDRunnable implements Runnable {
    private final AppIdentifier appIdentifier;
    private final Callback<AppIdentifier> callBack;

    public GetAppIDRunnable(AppIdentifier appIdentifier, Callback<AppIdentifier> callBack) {
        this.appIdentifier = appIdentifier;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        String appID = appIdentifier.getID();
        if (appID == null) {
            callBack.onFail();
        } else {
            callBack.onSuccess(appIdentifier);
        }
    }
}