package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.SendTodoRunnable;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.TodoDAO;


public class TodoTextNoteViewModel extends ViewModel {

    private final MutableLiveData<Todo> savedTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> connectionState = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> invalidInputError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendingError = new MutableLiveData<>();
    private Todo todo;
    private final AppIdentifier appIdentifier;
    private final ConnectionNetworkInfo connectionNetworkInfo;
    private final TodoDAO todoDAO;
    private final Callback<Todo> sendTodoCallback = new Callback<Todo>() {
        @Override
        public void onFail() {
            sendingError.postValue(true);
            sendTodo.postValue(false);
        }

        @Override
        public void onSuccess(Todo result) {
            savedTodo.postValue(result);
            sendTodo.postValue(false);
        }
    };

    public TodoTextNoteViewModel(AppIdentifier appIdentifier, ConnectionNetworkInfo connectionNetworkInfo, TodoDAO todoDAO) {
        this.appIdentifier = appIdentifier;
        this.connectionNetworkInfo = connectionNetworkInfo;
        this.todoDAO = todoDAO;
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
        if (todo == null) {
            todo = new Todo(null, textTodo);
        } else {
            todo.setTodoText(textTodo);
        }
        if (textTodo.length() == 0) {
            invalidInputError.setValue(true);
        } else {
            if (connectionNetworkInfo.isConnected()) {
                Thread sentTodoThread = new Thread(new SendTodoRunnable(todo, sendTodoCallback, appIdentifier, todoDAO));
                sentTodoThread.start();
                sendTodo.setValue(true);
            } else {
                connectionState.setValue(true);
            }
        }
    }

    public void resetEvent() {
        invalidInputError.setValue(false);
        connectionState.setValue(false);
        sendingError.setValue(false);
    }
}