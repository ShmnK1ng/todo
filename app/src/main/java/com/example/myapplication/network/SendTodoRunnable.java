package com.example.myapplication.network;

import com.example.myapplication.todo.Todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SendTodoRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/.json";
    private static final String REQUEST_POST = "POST";
    private final Todo todo;
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private OutputStream outputStream = null;

    public SendTodoRunnable(Todo todo) {
        this.todo = todo;
    }

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
                httpURLConnection.setRequestMethod(REQUEST_POST);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            try {
                httpURLConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                outputStream = httpURLConnection.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                todoJsonWriter.writeJson(outputStream, todo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String serverMassage = bufferedReader.readLine(); // пока просто сохраняю сообщение от сервера
                    inputStreamReader.close();
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
