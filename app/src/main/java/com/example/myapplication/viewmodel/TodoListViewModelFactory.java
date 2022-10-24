package com.example.myapplication.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.sharedpreferences.TodoSharedPreferences;

public class TodoListViewModelFactory implements ViewModelProvider.Factory {
    private final TodoSharedPreferences todoSharedPreferences;

    public TodoListViewModelFactory(TodoSharedPreferences todoSharedPreferences) {
        this.todoSharedPreferences = todoSharedPreferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoListViewModel.class)) {
            return (T) new TodoListViewModel(todoSharedPreferences);
           /* try {
                return modelClass.getConstructor(String.class).newInstance(todoSharedPreferences);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } */ // еще видел такой вариант
        } else throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
