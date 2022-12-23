package com.example.myapplication.utilities;

import com.example.myapplication.model.Todo;

public interface TodoDao {
    void saveTodo(Todo todo);

    void editTodo(Todo todo);
}
