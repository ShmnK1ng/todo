package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

import java.util.List;

public interface Repository {
    void getTodoList(Callback<List<Todo>> callback);

    void saveTodo(Callback<Todo> callback, Todo todo);

    void createUser(String login, String password, Callback<String> callback);

    void serverLogin(String login, String password, Callback<String> callback);

    void serverLogout();
}