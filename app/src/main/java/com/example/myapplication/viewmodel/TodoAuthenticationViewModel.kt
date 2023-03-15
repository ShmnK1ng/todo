package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class TodoAuthenticationViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val checkAuthentication: MutableLiveData<Boolean> = MutableLiveData()

    fun checkAuthenticationEvent(): LiveData<Boolean> {
        return checkAuthentication
    }

    fun activityCreated() {
        checkAuthentication.postValue(firebaseAuth.currentUser != null)
    }
}