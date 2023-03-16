package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.TodoRepository
import com.example.myapplication.databinding.FragmentTodoRegistationBinding
import com.example.myapplication.utilities.AlertDialogUtils
import com.example.myapplication.utilities.Repository
import com.example.myapplication.viewmodel.TodoFragmentRegistrationViewModel
import com.example.myapplication.viewmodel.TodoFragmentRegistrationViewModelFactory


class TodoRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentTodoRegistationBinding
    private val emptyFieldError: String by lazy { getString(R.string.todo_fragment_login_empty_field_error) }
    private val repository: Repository by lazy { TodoRepository.getInstance(context?.applicationContext) }
    private val viewModel: TodoFragmentRegistrationViewModel by lazy {
        ViewModelProvider(
            this,
            TodoFragmentRegistrationViewModelFactory(repository)
        )[TodoFragmentRegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoRegistationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentTodoRegistrationTextViewCreateAccount.setOnClickListener {
            viewModel.createAccountClicked(
                binding.fragmentTodoRegistrationEditTextEmail.text,
                binding.fragmentTodoRegistrationEditTextPassword.text
            )
        }
        setObservers()
    }

    private fun setObservers() {
        viewModel.createAccountEvent().observe(viewLifecycleOwner) { isRegistered ->
            if (isRegistered) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_todo_authentication, TodoLoginFragment.newInstance())
                    .commit()
            }
        }
        viewModel.showRegistrationError().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                AlertDialogUtils.showAlertDialog(
                    activity, errorMessage
                ) {
                    viewModel.resetEvent()
                    binding.fragmentTodoRegistrationProgressBar.visibility = View.GONE
                    binding.fragmentTodoRegistrationTextViewCreateAccount.visibility = View.VISIBLE
                }
            }
        }
        viewModel.showEmptyLoginError().observe(viewLifecycleOwner) { showError ->
            if (showError) {
                binding.fragmentTodoRegistrationEditTextEmail.error = emptyFieldError
            }
        }
        viewModel.showEmptyPasswordError().observe(viewLifecycleOwner) { showError ->
            if (showError) {
                binding.fragmentTodoRegistrationEditTextPassword.error = emptyFieldError
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TodoRegistrationFragment()
    }
}