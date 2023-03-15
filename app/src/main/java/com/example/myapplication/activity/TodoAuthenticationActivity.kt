package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTodoAuthenticationBinding
import com.example.myapplication.fragment.TodoLoginFragment
import com.example.myapplication.viewmodel.TodoAuthenticationViewModel
import com.example.myapplication.viewmodel.TodoAuthenticationViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TodoAuthenticationActivity : AppCompatActivity() {
    private lateinit var todoLoginActivityBinding: ActivityTodoAuthenticationBinding
    private var isAuth: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { isAuth }
        todoLoginActivityBinding = ActivityTodoAuthenticationBinding.inflate(layoutInflater)
        setContentView(todoLoginActivityBinding.root)
        val firebaseAuth: FirebaseAuth = Firebase.auth
        val viewModel = ViewModelProvider(
            this,
            TodoAuthenticationViewModelFactory(firebaseAuth)
        )[TodoAuthenticationViewModel::class.java]
        viewModel.checkAuthenticationEvent().observe(this) { isLogged ->
            if (isLogged) {
                isAuth = false
                val startTodoListActivityIntent = Intent(this, TodoListActivity::class.java)
                startActivity(startTodoListActivityIntent)
                finish()
            } else {
                isAuth = false
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_todo_authentication, TodoLoginFragment.newInstance())
                    .commit()
            }
        }
        viewModel.activityCreated()
    }
}