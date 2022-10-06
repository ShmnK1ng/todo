package com.example.myapplication.network;

import com.example.myapplication.Todo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class GetTodoListRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/.json";
    private static final String REQUEST_GET = "GET";
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private InputStream inputStream = null;

    @Override
    public void run() {

        URL url = null;

        try {
            url = new URL(FIREBASE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection httpURLConnection = null;

        try {
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (httpURLConnection != null) {
            try {
                httpURLConnection.setRequestMethod(REQUEST_GET);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            httpURLConnection.setDoInput(true);
            try {
                httpURLConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    inputStream = httpURLConnection.getInputStream();
                    List<Todo> todoList = todoJsonReader.readJsonStream(inputStream); // пока сохраняет список тудушек сюда
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
