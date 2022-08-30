package com.example.myapplication;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class TodoViewModel extends ViewModel {

    private final ArrayList<Todo> todoList = new ArrayList<>();
    private int position;

    public ArrayList<Todo> getTodolist() {
        return todoList;
    }

    public void addTodo(Todo todo) {
        todoList.add(todo);
    }

    public void setTodo(int position, Todo todo) {
        this.position = position;
        todoList.set(position, todo);
    }

    public boolean containsTodo(Todo todo) {
        return todoList.contains(todo);
    }

    public int getPosition(Todo todo) {
        this.position = todoList.indexOf(todo);
        return position;
    }

    public int getTodoListSize () {
        return todoList.size();
    }
}