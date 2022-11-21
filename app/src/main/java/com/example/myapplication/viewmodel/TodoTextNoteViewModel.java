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
    private final MutableLiveData<Boolean> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> connectionState = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> invalidInputError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendingError = new MutableLiveData<>();
    private Todo todo;
    private final AppIdentifier appIdentifier;
    private final Callback<Todo> sendTodoCallback = new Callback<Todo>() {
        @Override
        public void onFail() {
            sendingError.postValue(true);
        }

        @Override
        public void onSuccess(Todo result) {
            savedTodo.postValue(result);
            sendTodo.postValue(false);
        }
    };

    public TodoTextNoteViewModel(AppIdentifier appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    public LiveData<Boolean> sendTodoEvent() {
        return sendTodo;
    }

    public LiveData<Boolean> checkConnectionState() {
        return connectionState;
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

    public LiveData<Boolean> sendingErrorEvent() {
        return sendingError;
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
        connectionState.setValue(true);
        if (todo == null) {
            todo = new Todo(null, textTodo);
        } else {
            todo.setTodoText(textTodo);
        }
        if (textTodo.length() == 0) {
            invalidInputError.setValue(true);
        } else {
            if (Boolean.TRUE.equals(connectionState.getValue())) {
                Thread sentTodoThread = new Thread(new SendTodoRunnable(todo, sendTodoCallback, appIdentifier));
                sentTodoThread.start();
                sendTodo.setValue(true);
            }
        }
    }

    public void resetEvent() {
        invalidInputError.setValue(false);
        connectionState.setValue(false);
        sendingError.setValue(false);
    }
}