package com.example.myapplication.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.utilities.AlertDialogUtils
import com.example.myapplication.utilities.Callback
import com.example.myapplication.utilities.Repository

class TodoFragmentRegistrationViewModel(private val repository: Repository) : ViewModel() {
    private val emptyLoginError: MutableLiveData<Boolean> = MutableLiveData()
    private val emptyPasswordError: MutableLiveData<Boolean> = MutableLiveData()
    private val createAccount: MutableLiveData<Boolean> = MutableLiveData()
    private val registrationError: MutableLiveData<AlertDialogUtils.Events?> = MutableLiveData()
    private val callback = object : Callback<String> {
        override fun onFail(message: AlertDialogUtils.Events) {
            registrationError.value = message
        }

        override fun onSuccess(result: String?) {
            createAccount.value = true
        }

    }

    fun showEmptyLoginError(): LiveData<Boolean> {
        return emptyLoginError
    }

    fun showEmptyPasswordError(): LiveData<Boolean> {
        return emptyPasswordError
    }

    fun createAccountEvent(): LiveData<Boolean> {
        return createAccount
    }

    fun showRegistrationError(): MutableLiveData<AlertDialogUtils.Events?> {
        return registrationError
    }

    fun createAccountClicked(login: Editable?, password: Editable?) {
        if (login.toString().isEmpty()) {
            emptyLoginError.value = true
        }
        if (password.toString().isEmpty()) {
            emptyPasswordError.value = true
        }
        if (login.toString().isNotEmpty() && password.toString().isNotEmpty()) {
            repository.createUser(login.toString(), password.toString(), callback)
        }
    }

    fun resetEvent() {
        registrationError.value = null
        emptyLoginError.value = false
        emptyPasswordError.value = false
    }
}