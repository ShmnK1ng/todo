package com.example.myapplication.data;

import static com.example.myapplication.utilities.AlertDialogUtils.GET_TODO_LIST_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.NETWORK_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.SENDING_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.SERVER_INIT_ERROR;
import static com.example.myapplication.utilities.TodoHttpConnectionUtils.TODO_ADDED;
import static com.example.myapplication.utilities.TodoHttpConnectionUtils.TODO_EDITED;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
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

    public TodoRepository(AppIdentifier appIdentifier, TodoDao todoDao, TodoApi todoApi, ConnectionNetworkInfo connectionNetworkInfo) {
        this.appIdentifier = appIdentifier;
        this.todoDao = todoDao;
        this.todoApi = todoApi;
        this.connectionNetworkInfo = connectionNetworkInfo;
    }

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

    public void getTodoListFromServer(Callback<List<Todo>> callback, String appID) {
        List<Todo> todoList = todoApi.getTodoList(appID);
        if (todoList != null) {
            todoDao.saveTodoList(todoList);
            callback.onSuccess(todoList);
        } else {
            callback.onFail(GET_TODO_LIST_ERROR);
        }
    }

    public <T> void serverInit(Callback<T> callback) {
        appID = todoApi.serverInit();
        if (appID != null) {
            appIdentifier.setID(appID);
            callback.onSuccess(null);
        } else {
            callback.onFail(SERVER_INIT_ERROR);
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
                callback.onFail(NETWORK_ERROR);
            }
        }).start();
    }

    public void startSavingTodo(Callback<Todo> callback, Todo todo, String appID) {
        String savingStatus = new TodoHttpConnectionUtils().saveTodo(appID, todo);
        if (savingStatus.equals(TODO_EDITED)) {
            todoDao.editTodo(todo);
            callback.onSuccess(todo);
        } else {
            if (savingStatus.equals(TODO_ADDED)) {
                todoDao.saveTodo(todo);
                callback.onSuccess(todo);
            } else {
                callback.onFail(SENDING_ERROR);
            }
        }
    }
}