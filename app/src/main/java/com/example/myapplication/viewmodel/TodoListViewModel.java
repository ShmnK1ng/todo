package com.example.myapplication.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Todo;
import com.example.myapplication.network.FirebaseInitRunnable;
import com.example.myapplication.network.GetAppIDRunnable;
import com.example.myapplication.network.GetTodoListRunnable;
import com.example.myapplication.sharedpreferences.AppIdentifier;
import com.example.myapplication.utilities.Callback;

import java.util.ArrayList;
import java.util.List;

public class TodoListViewModel extends ViewModel {

    private final MutableLiveData<Boolean> goToAddTodo = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Todo>> todoList = new MutableLiveData<>();
    private final MutableLiveData<Todo> goToEditTodo = new MutableLiveData<>();
    private final AppIdentifier appIdentifier;
    private final Callback<String> appInitEvent = new Callback<String>() {
        @Override
        public void onFail() {
            Callback<String> getIDEvent = new Callback<String>() {
                @Override
                public void onFail() {
                    //do nothing
                }

                @Override
                public void onSuccess(String result) {
                    appIdentifier.setID(result);
                }
            };
            Thread onServerInitThread = new Thread(new FirebaseInitRunnable(getIDEvent));
            onServerInitThread.start();
        }

        @Override
        public void onSuccess(String id) {
            Callback<List<Todo>> getTodoListEvent = new Callback<List<Todo>>() {
                @Override
                public void onFail() {
                    //do nothing
                }

                @Override
                public void onSuccess(List<Todo> result) {
                    todoList.postValue((ArrayList<Todo>) result);
                }
            };
            Thread getTodoListThread = new Thread(new GetTodoListRunnable(id, getTodoListEvent));
            getTodoListThread.start();
        }
    };

    public TodoListViewModel(AppIdentifier appIdentifier) {
        this.appIdentifier = appIdentifier;
        appInit();
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
        Thread appInitThread = new Thread(new GetAppIDRunnable(appIdentifier, appInitEvent));
        appInitThread.start();
    }
}