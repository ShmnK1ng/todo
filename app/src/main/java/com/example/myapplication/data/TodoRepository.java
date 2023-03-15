package com.example.myapplication.data;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AlertDialogUtils;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.ConnectivityManagerWrapper;
import com.example.myapplication.utilities.Repository;
import com.example.myapplication.utilities.TodoApi;
import com.example.myapplication.utilities.TodoDao;
import com.example.myapplication.utilities.TodoHttpConnectionUtils;

import java.util.List;

public class TodoRepository implements Repository {

    private final TodoDao todoDao;
    private final TodoApi todoApi;
    private final ConnectionNetworkInfo connectionNetworkInfo;
    private static volatile TodoRepository instance;

    public TodoRepository(Context context) {
        this.todoDao = new TodoDbHelperWrapper(TodoDbHelper.getInstance(context));
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
                getTodoListFromServer(callback, todoApi.getUserUID());
            } else {
                getTodoListFromBD(callback);
            }
        }).start();
    }

    private void getTodoListFromServer(Callback<List<Todo>> callbackFromVM, String appID) {
        Callback<List<Todo>> callback = new Callback<List<Todo>>() {
            @Override
            public void onFail(AlertDialogUtils.Events message) {
                callbackFromVM.onFail(message);
            }

            @Override
            public void onSuccess(List<Todo> result) {
                todoDao.saveTodoList(result, todoApi.getUserUID());
                callbackFromVM.onSuccess(result);
            }
        };
        todoApi.getTodoList(appID, callback);
    }

    @Override
    public void createUser(String login, String password, Callback<String> callback) {
        todoApi.createUser(login, password, callback);
    }

    @Override
    public void serverLogin(String login, String password, Callback<String> callbackFromVM) {
        Callback<String> callback = new Callback<String>() {
            @Override
            public void onFail(AlertDialogUtils.Events message) {
                callbackFromVM.onFail(message);
            }

            @Override
            public void onSuccess(String result) {
                todoDao.setUserUID(result);
                callbackFromVM.onSuccess(null);
            }
        };
        todoApi.userLogin(login, password, callback);
    }

    @Override
    public void serverLogout() {
        todoApi.userLogout();
    }

    private void getTodoListFromBD(Callback<List<Todo>> callback) {
        try {
            List<Todo> todoList = todoDao.getTodoList(todoApi.getUserUID());
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
                startSavingTodo(callback, todo, todoApi.getUserUID());
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
                todoDao.saveTodo(todo, todoApi.getUserUID());
                callback.onSuccess(todo);
            } else {
                callback.onFail(AlertDialogUtils.Events.SENDING_ERROR);
            }
        }
    }
}