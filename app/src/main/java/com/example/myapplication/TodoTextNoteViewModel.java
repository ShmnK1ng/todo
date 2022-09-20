package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

public class TodoTextNoteViewModel extends ViewModel {

    private final MutableLiveData<Todo> savedTodo = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private Todo todo;

    public LiveData<Todo> getSavedTodo() {
        return savedTodo;
    }

    public LiveData<String> getTodoText() {
        return editTodoText;
    }

    public void setExtraTodo(Todo todo) {
        this.todo = todo;
        if (todo != null) {
            String textTodo = todo.getTodoText();
            editTodoText.setValue(textTodo);
        }
    }

    public void updateTodoText(String editedText) {
        editTodoText.setValue(editedText);
    }

    public void onButtonClicked(String textTodo) {
        if (todo == null) {
            String uid = UUID.randomUUID().toString();
            todo = new Todo(uid, textTodo);
        } else {
            todo.setTodoText(textTodo);
        }
        if (textTodo.length() == 0) {
            savedTodo.setValue(null);
        } else {
            savedTodo.setValue(todo);
        }
    }
}