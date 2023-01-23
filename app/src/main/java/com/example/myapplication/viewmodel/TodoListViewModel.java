package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.TodoApi;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.TodoDao;

import java.util.ArrayList;
import java.util.List;

public class TodoListViewModel extends ViewModel {

    private final MutableLiveData<Boolean> goToAddTodo = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Todo>> todoList = new MutableLiveData<>();
    private final MutableLiveData<Todo> goToEditTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> getTodoList = new MutableLiveData<>();
    private final MutableLiveData<String> getTodoListError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshTodoList = new MutableLiveData<>();
    private final AppIdentifier appIdentifier;
    private final TodoDao todoDao;
    private final ConnectionNetworkInfo connectionNetworkInfo;
    private final Callback<List<Todo>> getTodoListCallback = new Callback<List<Todo>>() {
        @Override
        public void onFail(String message) {
            getTodoListError.postValue(message);
            getTodoList.postValue(false);
            refreshTodoList.postValue(false);
        }

        @Override
        public void onSuccess(List<Todo> result) {
            todoList.postValue(new ArrayList<>(result));
            getTodoList.postValue(false);
            refreshTodoList.postValue(false);
        }
    };
    public TodoListViewModel(AppIdentifier appIdentifier, ConnectionNetworkInfo connectionNetworkInfo, TodoDao todoDao) {
        this.todoDao = todoDao;
        this.appIdentifier = appIdentifier;
        this.connectionNetworkInfo = connectionNetworkInfo;
        getTodoListFromServer();
    }

    public LiveData<Boolean> refreshTodoListEvent() {
        return refreshTodoList;
    }

    public LiveData<Boolean> getTodoListProgressEvent() {
        return getTodoList;
    }

    public LiveData<? extends List<Todo>> getTodoList() {
        return todoList;
    }

    public LiveData<Boolean> addTodoEvent() {
        return goToAddTodo;
    }

    public LiveData<Todo> itemClickEvent() {
        return goToEditTodo;
    }

    public LiveData<String> getTodoListErrorEvent() {
        return getTodoListError;
    }

    public void addTodoClicked() {
        goToAddTodo.setValue(true);
    }

    public void todoItemClicked(Todo todo) {
        goToEditTodo.setValue(todo);
    }

    public void updateTodo(Todo todo) {
        ArrayList<Todo> todoListRepository = todoList.getValue();
        if (todoListRepository == null) {
            todoListRepository = new ArrayList<>();
        }
        if (!todoListRepository.contains(todo)) {
            todoListRepository.add(todo);
        } else {
            int position = todoListRepository.indexOf(todo);
            todoListRepository.set(position, todo);
        }
        todoList.setValue(todoListRepository);
    }

    public void resetClickState() {
        goToAddTodo.setValue(false);
        goToEditTodo.setValue(null);
    }

    public void getTodoListFromServer() {
        getTodoList.setValue(true);
        Thread etTodoListThread = new Thread(() -> new TodoApi(appIdentifier, todoDao).getTodoList(getTodoListCallback, connectionNetworkInfo));
        etTodoListThread.start();
    }

    public void resetGetTodoListErrorEvent() {
        getTodoListError.setValue(null);
    }

    public void refreshRequest() {
        Thread refreshTodoListThread = new Thread(() -> new TodoApi(appIdentifier, todoDao).getTodoListFromServer(getTodoListCallback, appIdentifier.getID()));
        refreshTodoListThread.start();
        refreshTodoList.setValue(true);
    }
}