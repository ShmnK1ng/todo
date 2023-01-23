package com.example.myapplication.utilities;

public interface Callback<T> {
    void onFail(String message);

    void onSuccess(T result);

}
