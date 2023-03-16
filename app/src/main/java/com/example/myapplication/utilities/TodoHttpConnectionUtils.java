package com.example.myapplication.utilities;


import com.example.myapplication.model.Todo;
import com.example.myapplication.network.TodoJsonReader;
import com.example.myapplication.network.TodoJsonWriter;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TodoHttpConnectionUtils implements TodoApi {

    public static final String PUT_TODO = "put_todo";
    public static final String POST_TODO = "post_todo";
    public static final String GET_TODO_LIST = "get_todo_list";
    private static final String FIREBASE_URL = "https://todoapp-f8a0e-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET = "GET";
    private static final String REQUEST_PUT = "PUT";
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private HttpURLConnection httpURLConnection;
    private URL url;
    private String requestMethod;
    private boolean inputRequest;
    private boolean outputRequest;

    private Boolean connectToServer(String requestConnectionMethod, String appID, Todo todo) {
        boolean isConnected = true;
        try {
            switch (requestConnectionMethod) {
                case GET_TODO_LIST:
                    url = new URL(FIREBASE_URL + appID + "/.json");
                    requestMethod = REQUEST_GET;
                    inputRequest = true;
                    outputRequest = false;
                    break;
                case POST_TODO:
                    url = new URL(FIREBASE_URL + appID + "/.json");
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
    public String getUserUID() {
        return FirebaseAuth.getInstance().getUid();
    }

    @Override
    public void userLogin(String login, String password, Callback<String> callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(getUserUID());
                    } else {
                        callback.onFail(AlertDialogUtils.Events.SERVER_LOGIN_ERROR);
                    }
                });
    }

    @Override
    public void userLogout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void createUser(String login, String password, Callback<String> callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFail(AlertDialogUtils.Events.SERVER_REGISTRATION_ERROR);
                    }
                });
    }

    @Override
    public void getTodoList(String appID, Callback<List<Todo>> callback) {
        if (connectToServer(GET_TODO_LIST, appID, null)) {
            try {
                if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    List<Todo> todoList = todoJsonReader.readJsonStream(inputStream);
                    inputStream.close();
                    callback.onSuccess(todoList);
                }
            } catch (IOException e) {
                callback.onFail(AlertDialogUtils.Events.GET_TODO_LIST_ERROR);
            } finally {
                httpURLConnection.disconnect();
            }
        }
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
                InputStream inputStream = httpURLConnection.getInputStream();
                if (requestMethod.equals(POST_TODO)) {
                    if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
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