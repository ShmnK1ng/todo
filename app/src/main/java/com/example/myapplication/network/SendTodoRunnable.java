package com.example.myapplication.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SendTodoRunnable implements Runnable {

    private final StringWriter stringWriter;

    public SendTodoRunnable(StringWriter stringWriter) {
        this.stringWriter = stringWriter;
    }

    @Override
    public void run() {
        try {
            String jsonInputString = stringWriter.toString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            String firebaseURL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/.json";
            URL url = new URL(firebaseURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(200);
            httpURLConnection.setReadTimeout(200);
            httpURLConnection.connect();
            try {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String serverMassage = bufferedReader.readLine(); // пока просто сохраняю сообщение от сервера
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
