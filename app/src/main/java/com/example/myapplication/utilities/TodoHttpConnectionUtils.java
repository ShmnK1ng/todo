package com.example.myapplication.utilities;


import com.example.myapplication.model.Todo;
import com.example.myapplication.network.TodoJsonReader;
import com.example.myapplication.network.TodoJsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TodoHttpConnectionUtils implements TodoApi {

    public static final String PUT_TODO = "put_todo";
    public static final String POST_TODO = "post_todo";
    public static final String SERVER_INIT = "server_init";
    public static final String GET_TODO_LIST = "get_todo_list";
    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/TodoList/";
    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET = "GET";
    private static final String REQUEST_PUT = "PUT";
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private HttpURLConnection httpURLConnection;
    private URL url;
    private String requestMethod;
    private String appID;
    private boolean inputRequest;
    private boolean outputRequest;

    private Boolean connectToServer(String requestConnectionMethod, String appID, Todo todo) {
        boolean isConnected = true;
        try {
            switch (requestConnectionMethod) {
                case SERVER_INIT:
                    url = new URL(FIREBASE_URL + ".json");
                    requestMethod = REQUEST_POST;
                    inputRequest = true;
                    outputRequest = true;
                    break;
                case GET_TODO_LIST:
                    url = new URL(FIREBASE_URL + appID + ".json");
                    requestMethod = REQUEST_GET;
                    inputRequest = true;
                    outputRequest = false;
                    break;
                case POST_TODO:
                    url = new URL(FIREBASE_URL + appID + ".json");
                    requestMethod = REQUEST_POST;
                    inputRequest = true;
                    outputRequest = true;
                    break;
                case PUT_TODO:
                    url = new URL(FIREBASE_URL + appID + "/" + todo.getUid() + ".json");
                    requestMethod = REQUEST_PUT;
                    inputRequest = true;
                    outputRequest = true;
            }
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.setDoOutput(outputRequest);
            httpURLConnection.setDoInput(inputRequest);
            httpURLConnection.connect();
        } catch (IOException e) {
            isConnected = false;
        }
        return isConnected;
    }

    @Override
    public String serverInit() {
        if (connectToServer(SERVER_INIT, null, null)) {
            try {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                todoJsonWriter.writeInitJson(outputStream);
                if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    appID = todoJsonReader.readJsonFromServer(inputStream);
                } else {
                    appID = null;
                }
            } catch (IOException e) {
                appID = null;
            } finally {
                httpURLConnection.disconnect();
            }
        }
        return appID;
    }

    @Override
    public List<Todo> getTodoList(String appID) {
        List<Todo> todoList = null;
        if (connectToServer(GET_TODO_LIST, appID, null)) {
            try {
                if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    todoList = todoJsonReader.readJsonStream(inputStream);
                    inputStream.close();
                }
            } catch (IOException e) {
                todoList = null;
            } finally {
                httpURLConnection.disconnect();
            }
        }
        return todoList;
    }

    @Override
    public AlertDialogUtils.Events saveTodo(String appID, Todo todo) {
        String requestMethod;
        AlertDialogUtils.Events savingStatus;
        if (todo.getUid() == null) {
            requestMethod = POST_TODO;
        } else {
            requestMethod = PUT_TODO;
        }
        if (connectToServer(requestMethod, appID, todo)) {
            try {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                todoJsonWriter.writeJson(outputStream, todo);
                if (requestMethod.equals(POST_TODO)) {
                    if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String todoUID = todoJsonReader.readJsonFromServer(inputStream);
                        todo.setUid(todoUID);
                        savingStatus = AlertDialogUtils.Events.TODO_ADDED;
                    } else {
                        savingStatus = AlertDialogUtils.Events.SENDING_ERROR;
                    }
                } else {
                    savingStatus = AlertDialogUtils.Events.TODO_EDITED;
                }
            } catch (IOException e) {
                savingStatus = AlertDialogUtils.Events.SENDING_ERROR;
            } finally {
                httpURLConnection.disconnect();
            }
        } else {
            savingStatus = AlertDialogUtils.Events.SENDING_ERROR;
        }
        return savingStatus;
    }
}