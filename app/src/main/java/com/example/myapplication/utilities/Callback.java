package com.example.myapplication.utilities;

public interface Callback<T> {
    void onFail(AlertDialogUtils.Events message);

    void onSuccess(T result);

}
