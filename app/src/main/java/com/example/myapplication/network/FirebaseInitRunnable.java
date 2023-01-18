package com.example.myapplication.network;

import com.example.myapplication.utilities.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseInitRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/.json";
    private static final String REQUEST_POST = "POST";
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private HttpURLConnection httpURLConnection;
    private final Callback<String> callBack;

    public FirebaseInitRunnable(Callback<String> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(FIREBASE_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(REQUEST_POST);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            todoJsonWriter.writeInitJson(outputStream);
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                String APP_ID = todoJsonReader.readJsonFromServer(inputStream);
                callBack.onSuccess(APP_ID);
            } else {
                callBack.onFail();
            }
        } catch (IOException e) {
            callBack.onFail();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
