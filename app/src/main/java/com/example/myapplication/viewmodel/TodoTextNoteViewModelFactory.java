package com.example.myapplication.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.ConnectionNetworkInfo;
import com.example.myapplication.utilities.TodoDao;

public class TodoTextNoteViewModelFactory implements ViewModelProvider.Factory {
    private final AppIdentifier appIdentifier;
    private final ConnectionNetworkInfo connectionNetworkInfo;
    private final TodoDao todoDAO;

    public TodoTextNoteViewModelFactory(AppIdentifier appIdentifier, ConnectionNetworkInfo connectionNetworkInfo, TodoDao todoDAO) {
        this.appIdentifier = appIdentifier;
        this.connectionNetworkInfo = connectionNetworkInfo;
        this.todoDAO = todoDAO;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoTextNoteViewModel.class)) {
            return (T) new TodoTextNoteViewModel(appIdentifier, connectionNetworkInfo, todoDAO);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
