package com.example.myapplication.vievmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;

import java.util.UUID;

public class TodoTextNoteViewModel extends ViewModel {

    private final MutableLiveData<Todo> savedTodo = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> invalidInputError = new MutableLiveData<>();
    private Todo todo;

    public LiveData<Todo> getSavedTodo() {
        return savedTodo;
    }

    public LiveData<String> getTodoText() {
        return editTodoText;
    }

    public LiveData<Boolean> invalidInputEvent() {
        return invalidInputError;
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
            invalidInputError.setValue(true);
        } else {
            savedTodo.setValue(todo);
        }
    }

    public void resetInvalidInputEvent() {
        invalidInputError.setValue(false);
    }
}