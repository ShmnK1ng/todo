package com.example.myapplication.network;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.TodoDAO;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetTodoListRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/";
    private static final String REQUEST_GET = "GET";
    private static String APP_ID = "";
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private HttpURLConnection httpURLConnection;
    private final Callback<List<Todo>> callBack;
    private TodoDAO todoDAO;

    public GetTodoListRunnable(String id, Callback<List<Todo>> callBack, TodoDAO todoDAO) {
        APP_ID = id;
        this.callBack = callBack;
        this.todoDAO = todoDAO;
    }

    @Override
    public void run() {
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
                callBack.onSuccess(todoList);
                todoDAO.saveTodoList(todoList);
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


