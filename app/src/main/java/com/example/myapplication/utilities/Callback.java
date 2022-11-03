package com.example.myapplication.utilities;

public interface Callback<T> {
    void onFail();

    void onSuccess(T result);

}
