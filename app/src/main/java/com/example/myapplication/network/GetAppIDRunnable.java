package com.example.myapplication.network;

import static com.example.myapplication.data.TodoDbHelper.NULL_VALUE;

import com.example.myapplication.utilities.AppIdentifier;
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
        if (appID.equals(NULL_VALUE)) {
            callBack.onFail();
        } else {
            callBack.onSuccess(appID);
        }
    }
}