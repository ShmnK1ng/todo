package com.example.myapplication.activity;

import static com.example.myapplication.activity.TodoListActivity.APP_PREFERENCES;
import static com.example.myapplication.activity.TodoListActivity.EXTRA_TODO;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Todo;
import com.example.myapplication.sharedpreferences.SharedPreferencesWrapper;
import com.example.myapplication.viewmodel.TodoTextNoteViewModel;
import com.example.myapplication.viewmodel.TodoTextNoteViewModelFactory;

public class TodoTextNoteActivity extends AppCompatActivity {

    private EditText editText;
    private TodoTextNoteViewModel viewModel;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_text_note);
        SharedPreferences serverID = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferencesWrapper sharedPreferencesWrapper = new SharedPreferencesWrapper(serverID);
        this.viewModel = new ViewModelProvider(this, new TodoTextNoteViewModelFactory(sharedPreferencesWrapper)).get(TodoTextNoteViewModel.class);
        viewModel.setExtraTodo(getIntent().getParcelableExtra(EXTRA_TODO));
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        viewModel.getTodoText().observe(this, this::setTodoText);
        viewModel.getSavedTodo().observe(this, this::sendTodoItem);
        viewModel.checkConnectionState().observe(this, checkStarted -> {
            if (checkStarted) {
                if (!isNetworkConnected()) {
                    networkError();
                    viewModel.resetCheckConnectionEvent();
                }
            }
        });
        viewModel.sendTodoEvent().observe(this, isTodoSent -> {
            if (isTodoSent) {
                findViewById(R.id.activity_todo_text_note_progressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.activity_todo_text_note_toolbar).setEnabled(false);
            } else {
                findViewById(R.id.activity_todo_text_note_progressBar).setVisibility(View.INVISIBLE);
                findViewById(R.id.activity_todo_text_note_toolbar).setEnabled(true);
            }
        });
        viewModel.invalidInputEvent().observe(this, isInvalidTextInput -> {
            if (isInvalidTextInput) {
                InvalidInputError();
            }
        });
        viewModel.sendingErrorEvent().observe(this, isSendingError -> {
            if (isSendingError) {
                sendingError();
            }
        });
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
        ((Toolbar) findViewById(R.id.activity_todo_text_note_toolbar)).setOnMenuItemClickListener(item -> {
            viewModel.onButtonClicked(editText.getText().toString());
            return true;
        });
    }

    private void navigationClickListener() {
        ((Toolbar) findViewById(R.id.activity_todo_text_note_toolbar)).setNavigationOnClickListener(item -> finish());
    }

    private void InvalidInputError() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.activity_todo_text_note_dialog_title);
        alertDialog.setMessage(getString(R.string.activity_todo_text_note_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.activity_todo_text_note_dialog_button_tittle),
                (dialogInterface, i) -> dialogInterface.dismiss()
        );
        alertDialog.setOnDismissListener(dialogInterface -> viewModel.resetInvalidInputEvent());
        alertDialog.show();
    }

    private void setTodoText(String todoText) {
        if (!todoText.equals(editText.getText().toString())) {
            editText.setText(todoText);
        }
    }

    private void networkError() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.activity_todo_text_note_network_error_dialog_title);
        alertDialog.setMessage(getString(R.string.activity_todo_text_note_network_error_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.activity_todo_text_note_dialog_button_tittle),
                (dialogInterface, i) -> dialogInterface.dismiss()
        );
        alertDialog.setOnDismissListener(dialogInterface -> viewModel.resetCheckConnectionEvent());
        alertDialog.show();
    }

    private void sendingError() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.activity_todo_text_sending_message_error_dialog_title);
        alertDialog.setMessage(getString(R.string.activity_todo_text_sending_message_error_dialog_title));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.activity_todo_text_note_dialog_button_tittle),
                (dialogInterface, i) -> dialogInterface.dismiss()
        );
        alertDialog.setOnDismissListener(dialogInterface -> viewModel.resetSendingErrorEvent());
        alertDialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}