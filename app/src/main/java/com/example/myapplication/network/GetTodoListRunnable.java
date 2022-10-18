package com.example.myapplication.network;

import com.example.myapplication.model.Todo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetTodoListRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/.json";
    private static final String REQUEST_GET = "GET";
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private HttpURLConnection httpURLConnection;

    @Override
    public void run() {
        try {
            URL url = new URL(FIREBASE_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(REQUEST_GET);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                List<Todo> todoList = todoJsonReader.readJsonStream(inputStream); // пока сохраняет список тудушек сюда
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}


