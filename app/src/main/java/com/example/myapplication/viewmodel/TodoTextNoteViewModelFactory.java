package com.example.myapplication.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.ConnectionNetworkInfo;

public class TodoTextNoteViewModelFactory implements ViewModelProvider.Factory {
    private final AppIdentifier appIdentifier;
    private final ConnectionNetworkInfo connectionNetworkInfo;

    public TodoTextNoteViewModelFactory(AppIdentifier appIdentifier, ConnectionNetworkInfo connectionNetworkInfo) {
        this.appIdentifier = appIdentifier;
        this.connectionNetworkInfo = connectionNetworkInfo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoTextNoteViewModel.class)) {
            return (T) new TodoTextNoteViewModel(appIdentifier, connectionNetworkInfo);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
