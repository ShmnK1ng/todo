package com.example.myapplication.activity;

import static com.example.myapplication.activity.TodoListActivity.APP_PREFERENCES;
import static com.example.myapplication.activity.TodoListActivity.EXTRA_TODO;
import static com.example.myapplication.utilities.AlertDialogSetter.INVALID_INPUT_ERROR;
import static com.example.myapplication.utilities.AlertDialogSetter.NETWORK_ERROR;
import static com.example.myapplication.utilities.AlertDialogSetter.SENDING_ERROR;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Todo;
import com.example.myapplication.sharedpreferences.SharedPreferencesWrapper;
import com.example.myapplication.utilities.AlertDialogSetter;
import com.example.myapplication.viewmodel.TodoTextNoteViewModel;
import com.example.myapplication.viewmodel.TodoTextNoteViewModelFactory;

public class TodoTextNoteActivity extends AppCompatActivity {

    private EditText editText;
    private TodoTextNoteViewModel viewModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_text_note);
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        this.toolbar = findViewById(R.id.activity_todo_text_note_toolbar);
        viewModelInit();
        setObservers();
        viewModel.setExtraTodo(getIntent().getParcelableExtra(EXTRA_TODO));
        editTextChangedListener();
        onButtonClickListener();
        navigationClickListener();
    }

    private void sendTodoItem(Todo todo) {
        Intent data = new Intent(this, TodoListActivity.class);
        data.putExtra(EXTRA_TODO, todo);
        setResult(RESULT_OK, data);
        finish();
    }

    private void editTextChangedListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.updateTodoText(editable.toString());
            }
        });
    }

    private void onButtonClickListener() {
        toolbar.setOnMenuItemClickListener(item -> {
            viewModel.onButtonClicked(editText.getText().toString());
            return true;
        });
    }

    private void navigationClickListener() {
        toolbar.setNavigationOnClickListener(item -> finish());
    }

    private void setTodoText(String todoText) {
        if (!todoText.equals(editText.getText().toString())) {
            editText.setText(todoText);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void setAlertDialog(String id) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setOnDismissListener(dialogInterface -> viewModel.resetEvent());
        new AlertDialogSetter().setAlertDialog(this, id, alertDialog);
    }

    private void viewModelInit() {
        SharedPreferences serverID = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferencesWrapper sharedPreferencesWrapper = new SharedPreferencesWrapper(serverID);
        this.viewModel = new ViewModelProvider(this, new TodoTextNoteViewModelFactory(sharedPreferencesWrapper)).get(TodoTextNoteViewModel.class);
    }

    private void setObservers() {
        viewModel.getTodoText().observe(this, this::setTodoText);
        viewModel.getSavedTodo().observe(this, this::sendTodoItem);
        viewModel.checkConnectionState().observe(this, checkStarted -> {
            if (checkStarted) {
                if (!isNetworkConnected()) {
                    setAlertDialog(NETWORK_ERROR);
                    viewModel.resetEvent();
                }
            }
        });
        ProgressBar progressBar = findViewById(R.id.activity_todo_text_note_progressBar);
        viewModel.sendTodoEvent().observe(this, isTodoSent -> {
            if (isTodoSent) {
                progressBar.setVisibility(View.VISIBLE);
                toolbar.setEnabled(false);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                toolbar.setEnabled(true);
            }
        });
        viewModel.invalidInputEvent().observe(this, isInvalidTextInput -> {
            if (isInvalidTextInput) {
                setAlertDialog(INVALID_INPUT_ERROR);
            }
        });
        viewModel.sendingErrorEvent().observe(this, isSendingError -> {
            if (isSendingError) {
                setAlertDialog(SENDING_ERROR);
            }
        });
    }
}