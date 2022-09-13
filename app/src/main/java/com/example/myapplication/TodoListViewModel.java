package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TodoListViewModel extends ViewModel {

    private final MutableLiveData<Boolean> goToAddTodo = new MutableLiveData<>();
    private final MutableLiveData<Todo> todoItem = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Todo>> todoList = new MutableLiveData<>();
    private final ArrayList<Todo> todoListRepository = new ArrayList<>();
    private final MutableLiveData<Boolean> goToEditTodo = new MutableLiveData<>();

    public LiveData<ArrayList<Todo>> getTodoList() {
        return todoList;
    }

    public LiveData<Boolean> addTodoEvent() {
        return goToAddTodo;
    }

    public LiveData<Todo> getTodoItem() {
        return todoItem;
    }

    public LiveData<Boolean> itemClickEvent() {
        return goToEditTodo;
    }

    public void addTodoClicked() {
        goToAddTodo.setValue(true);
    }

    public void todoItemClicked(Todo todo) {
        todoItem.setValue(todo);
        goToEditTodo.setValue(true);
    }

    public void updateTodo(Todo todo) {
        if (!todoListRepository.contains(todo)) {
            todoListRepository.add(todo);
        } else {
            int position = todoListRepository.indexOf(todo);
            todoListRepository.set(position, todo);
        }
        todoList.setValue(todoListRepository);
    }

    public void resetClickState() {
        goToAddTodo.setValue(false);
        goToEditTodo.setValue(false);
    }
}