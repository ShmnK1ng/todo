package com.example.myapplication.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.sharedpreferences.AppIdentifier;

public class TodoTextNoteViewModelFactory implements ViewModelProvider.Factory {
    private final AppIdentifier appIdentifier;

    public TodoTextNoteViewModelFactory(AppIdentifier appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoTextNoteViewModel.class)) {
            return (T) new TodoTextNoteViewModel(appIdentifier);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
