package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.activity.TodoListActivity
import com.example.myapplication.data.TodoRepository
import com.example.myapplication.databinding.FragmentTodoLoginBinding
import com.example.myapplication.utilities.AlertDialogUtils
import com.example.myapplication.utilities.Repository
import com.example.myapplication.viewmodel.TodoFragmentLoginViewModel
import com.example.myapplication.viewmodel.TodoFragmentLoginViewModelFactory

class TodoLoginFragment : Fragment() {
    private lateinit var binding: FragmentTodoLoginBinding
    private val emptyFieldError: String by lazy { getString(R.string.todo_fragment_login_empty_field_error) }
    private val repository: Repository by lazy { TodoRepository.getInstance(context?.applicationContext) }
    private val viewModel: TodoFragmentLoginViewModel by lazy {
        ViewModelProvider(
            this,
            TodoFragmentLoginViewModelFactory(repository)
        )[TodoFragmentLoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.fragmentTodoLoginCard.setOnClickListener {
            viewModel.serverLoginStarted(
                binding.fragmentTodoLoginEditTextEmail.text,
                binding.fragmentTodoLoginEditTextPassword.text
            )
        }
        binding.fragmentTodoLoginTextViewCreateAccount.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_todo_authentication, TodoRegistrationFragment.newInstance())
                .commit()
        }
    }

    private fun setObservers() {
        viewModel.serverLoginEvent().observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                val startTodoListActivityIntent = Intent(activity, TodoListActivity::class.java)
                startActivity(startTodoListActivityIntent)
            }
        }
        viewModel.showMessageError().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.fragmentTodoLoginProgressBar.visibility = View.VISIBLE
                binding.fragmentTodoLoginCard.visibility = View.GONE
                binding.fragmentTodoLoginTextViewCreateAccount.visibility = View.GONE
                AlertDialogUtils.showAlertDialog(
                    activity, errorMessage
                ) {
                    viewModel.resetEvent()
                    binding.fragmentTodoLoginProgressBar.visibility = View.GONE
                    binding.fragmentTodoLoginCard.visibility = View.VISIBLE
                    binding.fragmentTodoLoginTextViewCreateAccount.visibility = View.VISIBLE
                }
            }
        }

        viewModel.showEmptyLoginError().observe(viewLifecycleOwner) { showError ->
            if (showError) {
                binding.fragmentTodoLoginEditTextEmail.error = emptyFieldError
            }
        }
        viewModel.showEmptyPasswordError().observe(viewLifecycleOwner) { showError ->
            if (showError) {
                binding.fragmentTodoLoginEditTextPassword.error = emptyFieldError
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TodoLoginFragment()
    }
}