package com.example.myapplication.data;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AlertDialogUtils;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.ConnectivityManagerWrapper;
import com.example.myapplication.utilities.Repository;
import com.example.myapplication.utilities.TodoApi;
import com.example.myapplication.utilities.TodoDao;
import com.example.myapplication.utilities.TodoHttpConnectionUtils;

import java.util.List;

public class TodoRepository implements Repository {

    private final AppIdentifier appIdentifier;
    private final TodoDao todoDao;
    private final TodoApi todoApi;
    private final ConnectionNetworkInfo connectionNetworkInfo;
    private String appID;
    private static volatile TodoRepository instance;

    public TodoRepository(Context context) {
        TodoDbHelperWrapper dbHelperWrapper = new TodoDbHelperWrapper(TodoDbHelper.getInstance(context));
        this.appIdentifier = dbHelperWrapper;
        this.todoDao = dbHelperWrapper;
        this.todoApi = new TodoHttpConnectionUtils();
        this.connectionNetworkInfo = new ConnectivityManagerWrapper((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    public static TodoRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (TodoRepository.class) {
                if (instance == null) {
                    instance = new TodoRepository(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void getTodoList(Callback<List<Todo>> callback) {
        new Thread(() -> {
            if (connectionNetworkInfo.isConnected()) {
                appID = appIdentifier.getID();
                if (appID == null) {
                    serverInit(callback);
                } else {
                    getTodoListFromServer(callback, appID);
                }
            } else {
                getTodoListFromBD(callback);
            }
        }).start();
    }

    private void getTodoListFromServer(Callback<List<Todo>> callback, String appID) {
        List<Todo> todoList = todoApi.getTodoList(appID);
        if (todoList != null) {
            todoDao.saveTodoList(todoList);
            callback.onSuccess(todoList);
        } else {
            callback.onFail(AlertDialogUtils.Events.GET_TODO_LIST_ERROR);
        }
    }

    private <T> void serverInit(Callback<T> callback) {
        appID = todoApi.serverInit();
        if (appID != null) {
            appIdentifier.setID(appID);
            callback.onSuccess(null);
        } else {
            callback.onFail(AlertDialogUtils.Events.SERVER_INIT_ERROR);
        }
    }

    private void getTodoListFromBD(Callback<List<Todo>> callback) {
        try {
            List<Todo> todoList = todoDao.getTodoList();
            callback.onSuccess(todoList);
            callback.onFail(AlertDialogUtils.Events.NETWORK_ERROR);
        } catch (Exception e) {
            callback.onFail(AlertDialogUtils.Events.GET_TODO_LIST_ERROR);
        }
    }

    @Override
    public void saveTodo(Callback<Todo> callback, Todo todo) {
        new Thread(() -> {
            if (connectionNetworkInfo.isConnected()) {
                appID = appIdentifier.getID();
                if (appID == null) {
                    serverInit(callback);
                } else {
                    startSavingTodo(callback, todo, appID);
                }
            } else {
                callback.onFail(AlertDialogUtils.Events.NETWORK_ERROR);
            }
        }).start();
    }

    private void startSavingTodo(Callback<Todo> callback, Todo todo, String appID) {

        AlertDialogUtils.Events savingStatus = new TodoHttpConnectionUtils().saveTodo(appID, todo);
        if (savingStatus == AlertDialogUtils.Events.TODO_EDITED) {
            todoDao.editTodo(todo);
            callback.onSuccess(todo);
        } else {
            if (savingStatus == AlertDialogUtils.Events.TODO_ADDED) {
                todoDao.saveTodo(todo);
                callback.onSuccess(todo);
            } else {
                callback.onFail(AlertDialogUtils.Events.SENDING_ERROR);
            }
        }
    }
}