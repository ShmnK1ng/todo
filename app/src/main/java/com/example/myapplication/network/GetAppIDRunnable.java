package com.example.myapplication.network;

import com.example.myapplication.sharedpreferences.AppIdentifier;
import com.example.myapplication.utilities.Callback;

public class GetAppIDRunnable implements Runnable {
    private final AppIdentifier appIdentifier;
    private final Callback<String> callBack;

    public GetAppIDRunnable(AppIdentifier appIdentifier, Callback<String> callBack) {
        this.appIdentifier = appIdentifier;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        String appID = appIdentifier.getID();
        if (appID == null) {
            callBack.onFail();
        } else {
            callBack.onSuccess(appID);
        }
    }
}
