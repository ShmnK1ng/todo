package com.example.myapplication.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AlertDialogUtils;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.Repository;


public class TodoTextNoteViewModel extends ViewModel {

    private final MutableLiveData<Todo> savedTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogUtils.Events> getTodoError = new MutableLiveData<>();
    private Todo todo;
    private final Repository repository;
    private final Callback<Todo> sendTodoCallback = new Callback<Todo>() {
        @Override
        public void onFail(AlertDialogUtils.Events message) {
            getTodoError.postValue(message);
            sendTodo.postValue(false);
        }

        @Override
        public void onSuccess(Todo result) {
            savedTodo.postValue(result);
            sendTodo.postValue(false);
        }
    };

    public TodoTextNoteViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> sendTodoEvent() {
        return sendTodo;
    }

    public LiveData<Todo> getSavedTodo() {
        return savedTodo;
    }

    public LiveData<String> getTodoText() {
        return editTodoText;
    }

    public LiveData<AlertDialogUtils.Events> sendingErrorEvent() {
        return getTodoError;
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
            getTodoError.setValue(AlertDialogUtils.Events.INVALID_INPUT_ERROR);
        } else {
            repository.saveTodo(sendTodoCallback, todo);
            sendTodo.setValue(true);
        }
    }

    public void resetEvent() {
        getTodoError.setValue(null);
    }
}