package com.example.myapplication.network;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.TodoDao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendTodoRunnable implements Runnable {

    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/";
    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_PUT = "PUT";
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private final AppIdentifier appIdentifier;
    private final Todo todo;
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private HttpURLConnection httpURLConnection;
    private final Callback<Todo> callback;
    private final TodoDao todoDAO;

    public SendTodoRunnable(Todo todo, Callback<Todo> callback, AppIdentifier appIdentifier, TodoDao todoDAO) {
        this.todo = todo;
        this.appIdentifier = appIdentifier;
        this.callback = callback;
        this.todoDAO = todoDAO;
    }

    @Override
    public void run() {
        try {
            String todoListURL = appIdentifier.getID();
            if (todo.getUid() == null) {
                URL url = new URL(FIREBASE_URL + todoListURL + ".json");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_POST);
            } else {
                String todoURL = todo.getUid();
                URL url = new URL(FIREBASE_URL + todoListURL + "/" + todoURL + ".json");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(REQUEST_PUT);
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            todoJsonWriter.writeJson(outputStream, todo);
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                if (todo.getUid() == null) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    String todoID = todoJsonReader.readJsonFromServer(inputStream);
                    todo.setUid(todoID);
                    todoDAO.saveTodo(todo);
                } else {
                    todoDAO.editTodo(todo);
                }
                callback.onSuccess(todo);
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
