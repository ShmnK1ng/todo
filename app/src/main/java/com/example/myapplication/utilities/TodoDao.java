package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

import java.util.ArrayList;
import java.util.List;

public interface TodoDao {
    void saveTodo(Todo todo, String appUID);

    void editTodo(Todo todo);

    void saveTodoList(List<Todo> todoList, String accountUID);

    ArrayList<Todo> getTodoList(String appUID);

    void setUserUID(String id);
}
