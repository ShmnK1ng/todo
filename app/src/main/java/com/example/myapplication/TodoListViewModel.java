package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class TodoListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Todo>> todoList;
    private MutableLiveData<Boolean> onClickState;
    private final ArrayList<Todo> todoListRepository = new ArrayList<>();
    private Boolean currentOnButtonClickState;
    private Boolean currentOnTodoItemClickState;

    public MutableLiveData<ArrayList<Todo>> getTodoList() {
        if (todoList == null) {
            todoList = new MutableLiveData<>();
        }
        return todoList;

    }

    public MutableLiveData<Boolean> getOnClickState() {
        if (onClickState == null) {
            onClickState = new MutableLiveData<>();
        }
        return onClickState;
    }

    public void onButtonClicked() {
        currentOnButtonClickState = Boolean.TRUE;
        onClickState.setValue(true);
    }

    public void onTodoItemClickState() {
        currentOnTodoItemClickState = Boolean.TRUE;
        onClickState.setValue(true);
    }

    public Boolean getCurrentOnButtonClickState() {
        return currentOnButtonClickState;
    }

    public Boolean getCurrentOnTodoItemClickState() {
        return currentOnTodoItemClickState;
    }

    public void updateOnItemTodoClickState() {
        currentOnTodoItemClickState = Boolean.FALSE;
    }

    public void updateOnButtonClickState() {
        currentOnButtonClickState = Boolean.FALSE;
    }

    public ArrayList<Todo> getTodoListRepository() {
        return todoListRepository;
    }

    public void updateTodo(Todo todo) {
        if (!todoListRepository.contains(todo)) {
            todoListRepository.add(todo);
        }
        else {
            int position = todoListRepository.indexOf(todo);
            todoListRepository.set(position, todo);
        }
        todoList.setValue(todoListRepository);
    }
}