package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class TodoListViewModel extends ViewModel {

    private final MutableLiveData<Boolean> goToTodoTextNoteOnButtonClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> goToTodoTextNoteOnItemClicked = new MutableLiveData<>();
    public LiveData<ArrayList<Todo>> _todoList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Todo>> todoList = (MutableLiveData<ArrayList<Todo>>) _todoList;
    private final ArrayList<Todo> todoListRepository = new ArrayList<>();
    private Todo todo;
    private Boolean onButtonClick = false;

    public MutableLiveData<ArrayList<Todo>> getTodoList() {
        return todoList;
    }

    public MutableLiveData<Boolean> OnButtonClickEvent() {
        return goToTodoTextNoteOnButtonClicked;
    }

    public MutableLiveData<Boolean> OnItemClickEvent() {
        return goToTodoTextNoteOnItemClicked;
    }

    public void onButtonClicked() {
        this.onButtonClick = true;
        goToTodoTextNoteOnButtonClicked.setValue(Boolean.TRUE);
    }

    public void onTodoItemClicked(Todo todo) {
        onButtonClick = true;
        this.todo = todo;
        goToTodoTextNoteOnItemClicked.setValue(Boolean.TRUE);
    }

    public ArrayList<Todo> getTodoListRepository() {
        return todoListRepository;
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

    public Todo getTodo() {
        return todo;
    }

    public Boolean onClicked() {
        return onButtonClick;
    }

    public void resetOnClickedState() {
        onButtonClick = false;
    }
}