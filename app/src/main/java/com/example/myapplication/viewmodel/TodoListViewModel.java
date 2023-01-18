package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.TodoApi;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;

import java.util.ArrayList;
import java.util.List;

public class TodoListViewModel extends ViewModel {

    private final MutableLiveData<Boolean> goToAddTodo = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Todo>> todoList = new MutableLiveData<>();
    private final MutableLiveData<Todo> goToEditTodo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> getTodoList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> getTodoListError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshTodoList = new MutableLiveData<>();
    private final AppIdentifier appIdentifier;
    private final Callback<List<Todo>> getTodoListCallback = new Callback<List<Todo>>() {
        @Override
        public void onFail() {
            getTodoListError.postValue(true);
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

    public TodoListViewModel(AppIdentifier appIdentifier) {
        this.appIdentifier = appIdentifier;
        getTodoList.setValue(true);
        appInit();
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

    public LiveData<Boolean> getTodoListErrorEvent() {
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

    public void appInit() {
        Thread appInitThread = new Thread(() -> new TodoApi(getTodoListCallback, appIdentifier).getAppID());
        appInitThread.start();
    }

    public void resetGetTodoListErrorEvent() {
        getTodoListError.setValue(false);
    }

    public void refreshRequest() {
        Thread refreshTodoListThread = new Thread(() -> new TodoApi(getTodoListCallback, appIdentifier).getTodoList(appIdentifier.getID()));
        refreshTodoListThread.start();
        refreshTodoList.setValue(true);
    }
}