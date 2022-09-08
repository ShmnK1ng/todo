package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

public class TodoTextNoteViewModel extends ViewModel {

    public final LiveData<Todo> _putExtraTodo = new MutableLiveData<>();
    private final MutableLiveData<Todo> putExtraTodo = (MutableLiveData<Todo>) _putExtraTodo;
    public LiveData<Todo> _inputTodo = new MutableLiveData<>();
    private final MutableLiveData<Todo> inputTodo = (MutableLiveData<Todo>) _inputTodo;
    private Todo todo;
    private String textTodo;
    private Boolean enteredTextNoteStatus;

    public MutableLiveData<Todo> getExtraTodo() {
        return putExtraTodo;
    }

    public MutableLiveData<Todo> getInputTodo() {
        return inputTodo;
    }

    public void setInputTodo(Todo todo) {
        if (todo != null) {
            this.todo = todo;
            inputTodo.setValue(todo);
        }
    }

    public void updateTodo() {
        if (todo == null) {
            String uid = UUID.randomUUID().toString();
            todo = new Todo(uid, textTodo);
        } else {
            todo.setTodoText(textTodo);
        }
        putExtraTodo.setValue(todo);
    }

    public Todo getTodoTextNote() {
        return todo;
    }

    public void onButtonClicked(String textTodo) {
        this.textTodo = textTodo;
        enteredTextNoteStatus = textTodo.length() != 0;
    }

    public Boolean enteredTextNote() {
        return enteredTextNoteStatus;
    }
}