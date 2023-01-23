package com.example.myapplication.viewmodel;

import static com.example.myapplication.utilities.AlertDialogUtils.INVALID_INPUT_ERROR;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.TodoApi;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.TodoDao;


public class TodoTextNoteViewModel extends ViewModel {

    private final MutableLiveData<Todo> savedTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<String> editTodoText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> invalidInputError = new MutableLiveData<>();
    private final MutableLiveData<String> getTodoError = new MutableLiveData<>();
    private Todo todo;
    private final AppIdentifier appIdentifier;
    private final ConnectionNetworkInfo connectionNetworkInfo;
    private final TodoDao todoDAO;
    private final Callback<Todo> sendTodoCallback = new Callback<Todo>() {
        @Override
        public void onFail(String message) {
            getTodoError.postValue(message);
            sendTodo.postValue(false);
        }

        @Override
        public void onSuccess(Todo result) {
            savedTodo.postValue(result);
            sendTodo.postValue(false);
        }
    };

    public TodoTextNoteViewModel(AppIdentifier appIdentifier, ConnectionNetworkInfo connectionNetworkInfo, TodoDao todoDAO) {
        this.appIdentifier = appIdentifier;
        this.connectionNetworkInfo = connectionNetworkInfo;
        this.todoDAO = todoDAO;
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

    public LiveData<Boolean> invalidInputEvent() {
        return invalidInputError;
    }

    public LiveData<String> sendingErrorEvent() {
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
            getTodoError.setValue(INVALID_INPUT_ERROR);
        } else {
            Thread sendTodoThread = new Thread(() -> new TodoApi(appIdentifier, todoDAO).sendTodo(sendTodoCallback, todo, connectionNetworkInfo));
            sendTodoThread.start();
            sendTodo.setValue(true);
        }
    }

    public void resetEvent() {
        getTodoError.setValue(null);
    }
}