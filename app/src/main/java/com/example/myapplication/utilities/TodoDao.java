package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

import java.util.ArrayList;
import java.util.List;

public interface TodoDao {
    void saveTodo(Todo todo);

    void editTodo(Todo todo);

    void saveTodoList(List<Todo> todoList);

    ArrayList<Todo> getTodoList();
}
