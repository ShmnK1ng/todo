package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class TodoAuthenticationViewModelFactory(private val firebaseAuth: FirebaseAuth) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoAuthenticationViewModel(firebaseAuth) as T
    }
}