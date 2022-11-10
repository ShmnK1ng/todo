package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.SendTodoRunnable;
import com.example.myapplication.sharedpreferences.AppIdentifier;
import com.example.myapplication.utilities.Callback;


public class TodoTextNoteViewModel extends ViewModel {

    private final MutableLiveData<Todo> savedTodo = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> invalidInputError = new MutableLiveData<>();
    private Todo todo;
    private final AppIdentifier appIdentifier;
    private final Callback<Todo> sendTodoCallback = new Callback<Todo>() {
        @Override
        public void onFail() {
            //do nothing
        }

        @Override
        public void onSuccess(Todo result) {
            savedTodo.postValue(result);
        }
    };

    public TodoTextNoteViewModel(AppIdentifier appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

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
            todo = new Todo(null, textTodo);
        } else {
            todo.setTodoText(textTodo);
        }
        if (textTodo.length() == 0) {
            invalidInputError.setValue(true);
        } else {
            Thread sentTodoThread = new Thread(new SendTodoRunnable(todo, sendTodoCallback, appIdentifier));
            sentTodoThread.start();
        }
    }

    public void resetInvalidInputEvent() {
        invalidInputError.setValue(false);
    }
}