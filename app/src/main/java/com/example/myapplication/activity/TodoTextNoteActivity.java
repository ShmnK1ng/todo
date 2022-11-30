package com.example.myapplication.activity;

import static com.example.myapplication.activity.TodoListActivity.EXTRA_TODO;
import static com.example.myapplication.utilities.AlertDialogUtils.INVALID_INPUT_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.NETWORK_ERROR;
import static com.example.myapplication.utilities.AlertDialogUtils.SENDING_ERROR;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.data.TodoDbHelper;
import com.example.myapplication.data.TodoDbHelperWrapper;
import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AlertDialogUtils;
import com.example.myapplication.utilities.ConnectivityManagerWrapper;
import com.example.myapplication.viewmodel.TodoTextNoteViewModel;
import com.example.myapplication.viewmodel.TodoTextNoteViewModelFactory;

public class TodoTextNoteActivity extends AppCompatActivity {

    private EditText editText;
    private TodoTextNoteViewModel viewModel;
    private Toolbar toolbar;
    private ActionMenuItemView sendTodoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_text_note);
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        this.toolbar = findViewById(R.id.activity_todo_text_note_toolbar);
        this.sendTodoButton = toolbar.findViewById(R.id.menu_button);
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

    private void showAlertDialog(String id) {
        AlertDialogUtils.showAlertDialog(this, id, dialog -> viewModel.resetEvent());
    }

    private void viewModelInit() {
        TodoDbHelper dbHelper = TodoDbHelper.getInstance(getApplicationContext());
        TodoDbHelperWrapper dbHelperWrapper = new TodoDbHelperWrapper(dbHelper);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManagerWrapper connectivityManagerWrapper = new ConnectivityManagerWrapper(cm);
        this.viewModel = new ViewModelProvider(this, new TodoTextNoteViewModelFactory(dbHelperWrapper, connectivityManagerWrapper)).get(TodoTextNoteViewModel.class);
    }

    private void setObservers() {
        viewModel.getTodoText().observe(this, this::setTodoText);
        viewModel.getSavedTodo().observe(this, this::sendTodoItem);
        viewModel.checkConnectionState().observe(this, isNotConnected -> {
            if (isNotConnected) {
                showAlertDialog(NETWORK_ERROR);
            }
        });
        ProgressBar progressBar = findViewById(R.id.activity_todo_text_note_progressBar);
        viewModel.sendTodoEvent().observe(this, isTodoSent -> {
            if (isTodoSent) {
                progressBar.setVisibility(View.VISIBLE);
                sendTodoButton.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                sendTodoButton.setVisibility(View.VISIBLE);
            }
        });
        viewModel.invalidInputEvent().observe(this, isInvalidTextInput -> {
            if (isInvalidTextInput) {
                showAlertDialog(INVALID_INPUT_ERROR);
            }
        });
        viewModel.sendingErrorEvent().observe(this, isSendingError -> {
            if (isSendingError) {
                showAlertDialog(SENDING_ERROR);
            }
        });
    }
}