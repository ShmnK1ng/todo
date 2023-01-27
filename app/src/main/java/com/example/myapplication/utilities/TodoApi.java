package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

import java.util.List;

public interface TodoApi {
    List<Todo> getTodoList(String appID);

    String saveTodo(String callback, Todo todo);

    String serverInit();
}