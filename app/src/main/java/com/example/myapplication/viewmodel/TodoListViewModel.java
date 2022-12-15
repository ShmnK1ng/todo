package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.FirebaseInitRunnable;
import com.example.myapplication.network.GetAppIDRunnable;
import com.example.myapplication.network.GetTodoListRunnable;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.Callback;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.TodoDAO;

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
    private final TodoDAO todoDAO;
    private final Callback<String> getIDCallback = new Callback<String>() {
        @Override
        public void onFail() {
            getTodoListError.postValue(true);
            getTodoList.postValue(false);
        }

        @Override
        public void onSuccess(String result) {
            appIdentifier.setID(result);
            getTodoList.postValue(false);
        }
    };

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
            todoDAO.saveTodoList(result);
        }
    };

    private final Callback<AppIdentifier> appInitCallback = new Callback<AppIdentifier>() {
        @Override
        public void onFail() {
            Thread onServerInitThread = new Thread(new FirebaseInitRunnable(getIDCallback));
            onServerInitThread.start();
        }

        @Override
        public void onSuccess(AppIdentifier appIdentifier) {
            Thread getTodoListThread = new Thread(new GetTodoListRunnable(appIdentifier, getTodoListCallback));
            getTodoListThread.start();
        }
    };

    public TodoListViewModel(AppIdentifier appIdentifier, ConnectionNetworkInfo connectionNetworkInfo, TodoDAO todoDAO) {
        this.appIdentifier = appIdentifier;
        this.todoDAO = todoDAO;
        if (connectionNetworkInfo.isConnected()) {
            getTodoList.setValue(true);
            appInit();
        } else {
            todoList.postValue(todoDAO.getTodoList());
        }
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
        Thread appInitThread = new Thread(new GetAppIDRunnable(appIdentifier, appInitCallback));
        appInitThread.start();
    }

    public void resetGetTodoListErrorEvent() {
        getTodoListError.setValue(false);
    }

    public void refreshRequest() {
        Thread refreshTodoListThread = new Thread(new GetTodoListRunnable(appIdentifier, getTodoListCallback));
        refreshTodoListThread.start();
        refreshTodoList.setValue(true);
    }
}