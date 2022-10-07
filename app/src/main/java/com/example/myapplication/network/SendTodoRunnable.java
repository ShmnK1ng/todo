package com.example.myapplication.network;

import com.example.myapplication.todo.Todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendTodoRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/.json";
    private static final String REQUEST_POST = "POST";
    private final Todo todo;
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();

    public SendTodoRunnable(Todo todo) {
        this.todo = todo;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(FIREBASE_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(REQUEST_POST);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            todoJsonWriter.writeJson(outputStream, todo);
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String serverMassage = bufferedReader.readLine(); // пока просто сохраняю сообщение от сервера
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
