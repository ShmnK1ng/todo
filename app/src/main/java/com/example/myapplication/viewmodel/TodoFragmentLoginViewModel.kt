package com.example.myapplication.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.utilities.AlertDialogUtils
import com.example.myapplication.utilities.Callback
import com.example.myapplication.utilities.Repository

class TodoFragmentLoginViewModel(private val repository: Repository) : ViewModel() {
    private val serverLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val serverLoginError: MutableLiveData<AlertDialogUtils.Events> = MutableLiveData()
    private val emptyLoginError: MutableLiveData<Boolean> = MutableLiveData()
    private val emptyPasswordError: MutableLiveData<Boolean> = MutableLiveData()
    private val callback = object : Callback<String> {
        override fun onFail(message: AlertDialogUtils.Events) {
            serverLoginError.value = message
        }

        override fun onSuccess(result: String?) {
            serverLogin.value = true
        }
    }

    fun serverLoginEvent(): LiveData<Boolean> {
        return serverLogin
    }

    fun showMessageError(): LiveData<AlertDialogUtils.Events> {
        return serverLoginError
    }

    fun showEmptyLoginError(): LiveData<Boolean> {
        return emptyLoginError
    }

    fun showEmptyPasswordError(): LiveData<Boolean> {
        return emptyPasswordError
    }

    fun serverLoginStarted(login: Editable?, password: Editable?) {
        if (login.toString().isEmpty()) {
            emptyLoginError.value = true
        }
        if (password.toString().isEmpty()) {
            emptyPasswordError.value = true
        }
        if (login.toString().isNotEmpty() && password.toString().isNotEmpty()) {
            repository.serverLogin(login.toString(), password.toString(), callback)
        }
    }

    fun resetEvent() {
        serverLoginError.value = null
        emptyLoginError.value = false
        emptyPasswordError.value = false
    }
}