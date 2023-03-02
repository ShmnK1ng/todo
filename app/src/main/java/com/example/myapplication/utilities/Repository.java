package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

import java.util.List;

public interface Repository {
    void getTodoList(Callback<List<Todo>> callback);

    void saveTodo(Callback<Todo> callback, Todo todo);
}