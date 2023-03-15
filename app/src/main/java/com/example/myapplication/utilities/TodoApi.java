package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

import java.util.List;

public interface TodoApi {
    void getTodoList(String appID, Callback<List<Todo>> callback);

    AlertDialogUtils.Events saveTodo(String callback, Todo todo);

    String getUserUID();

    void userLogin(String login, String password, Callback<String> callback);

    void userLogout();

    void createUser(String login, String password, Callback<String> callback);
}