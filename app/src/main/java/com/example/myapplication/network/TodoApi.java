package com.example.myapplication.network;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TodoApi {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/";
    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET = "GET";
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private final AppIdentifier appIdentifier;
    private final Callback<List<Todo>> callback;
    private HttpURLConnection httpURLConnection;

    public TodoApi(Callback<List<Todo>> callback, AppIdentifier appIdentifier) {
        this.callback = callback;
        this.appIdentifier = appIdentifier;
    }

    public void getAppID() {
        String appID = appIdentifier.getID();
        if (appID == null) {
            firebaseInit();
        } else {
            getTodoList(appID);
        }
    }

    public void getTodoList(String APP_ID) {
        try {
            URL url = new URL(FIREBASE_URL + APP_ID + ".json");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(REQUEST_GET);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                List<Todo> todoList = todoJsonReader.readJsonStream(inputStream);
                inputStream.close();
                callback.onSuccess(todoList);
            } else {
                callback.onFail();
            }
        } catch (IOException e) {
            callback.onFail();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public void firebaseInit() {
        try {
            URL url = new URL(FIREBASE_URL + ".json");
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
                appIdentifier.setID(APP_ID);
            } else {
                callback.onFail();
            }
        } catch (IOException e) {
            callback.onFail();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
