package com.example.myapplication.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.utilities.AppIdentifier;

public class TodoListViewModelFactory implements ViewModelProvider.Factory {
    private final AppIdentifier appIdentifier;

    public TodoListViewModelFactory(AppIdentifier appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoListViewModel.class)) {
            return (T) new TodoListViewModel(appIdentifier);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
