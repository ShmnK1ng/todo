package com.example.myapplication.network;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.TodoDao;

import java.util.List;

public class GetTodoListDbRunnable implements Runnable {

    private final TodoDao todoDAO;
    private final Callback<List<Todo>> getTodoListDbCallback;

    public GetTodoListDbRunnable(TodoDao todoDAO, Callback<List<Todo>> getTodoListDbCallback) {
        this.todoDAO = todoDAO;
        this.getTodoListDbCallback = getTodoListDbCallback;
    }


    @Override
    public void run() {
        try {
            List<Todo> todoList = todoDAO.getTodoList();
            getTodoListDbCallback.onSuccess(todoList);
        } catch (Exception e) {
            getTodoListDbCallback.onFail();
        }
    }
}
