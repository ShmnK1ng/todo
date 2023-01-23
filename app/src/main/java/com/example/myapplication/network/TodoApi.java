package com.example.myapplication.network;

import static com.example.myapplication.utilities.AlertDialogUtils.GET_TODO_LIST_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.NETWORK_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.SENDING_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.SERVER_INIT_ERROR;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.TodoDao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TodoApi {

    public static final String TODO_SENDING = "todo_sending";
    public static final String SERVER_INIT = "server_init";
    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/";
    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET = "GET";
    private static final String REQUEST_PUT = "PUT";
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private final AppIdentifier appIdentifier;
    private final TodoDao todoDao;
    private HttpURLConnection httpURLConnection;
    private String appID;

    public TodoApi(AppIdentifier appIdentifier, TodoDao todoDao) {
        this.appIdentifier = appIdentifier;
        this.todoDao = todoDao;
    }

    public void getTodoList(Callback<List<Todo>> callback, ConnectionNetworkInfo connectionNetworkInfo) {
        if (connectionNetworkInfo.isConnected()) {
            appID = appIdentifier.getID();
            if (appID == null) {
                serverInit(callback, SERVER_INIT, null);
            } else {
                getTodoListFromServer(callback, appID);
            }
        } else {
            getTodoListFromBD(callback);
        }
    }

    public void getTodoListFromServer(Callback<List<Todo>> callback, String APP_ID) {
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
                todoDao.saveTodoList(todoList);
                callback.onSuccess(todoList);
            } else {
                callback.onFail(GET_TODO_LIST_ERROR);
            }
        } catch (IOException e) {
            callback.onFail(GET_TODO_LIST_ERROR);
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public <T> void serverInit(Callback<T> callback, String initParam, Todo todo) {
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
                if (initParam.equals(TODO_SENDING)) {
                    startSendingTodo((Callback<Todo>) callback, todo);
                }
            } else {
                callback.onFail(SERVER_INIT_ERROR);
            }
        } catch (IOException e) {
            callback.onFail(SERVER_INIT_ERROR);
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public void getTodoListFromBD(Callback<List<Todo>> callback) {
        try {
            List<Todo> todoList = todoDao.getTodoList();
            callback.onSuccess(todoList);
        } catch (Exception e) {
            callback.onFail(GET_TODO_LIST_ERROR);
        }
    }

    public void sendTodo(Callback<Todo> callback, Todo todo, ConnectionNetworkInfo connectionNetworkInfo) {
        if (connectionNetworkInfo.isConnected()) {
            appID = appIdentifier.getID();
            if (appID == null) {
                serverInit(callback, TODO_SENDING, todo);
            } else {
                startSendingTodo(callback, todo);
            }
        } else {
            callback.onFail(NETWORK_ERROR);
        }
    }

    public void startSendingTodo(Callback<Todo> callback, Todo todo) {
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
                    todoDao.saveTodo(todo);
                } else {
                    todoDao.editTodo(todo);
                }
                callback.onSuccess(todo);
            } else {
                callback.onFail(SENDING_ERROR);
            }
        } catch (IOException e) {
            callback.onFail(SENDING_ERROR);
        } finally {
            httpURLConnection.disconnect();
        }
    }
}