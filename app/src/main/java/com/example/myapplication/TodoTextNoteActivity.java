package com.example.myapplication;
import static com.example.myapplication.TodoListActivity.EXTRA_TODO;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class TodoTextNoteActivity extends AppCompatActivity {

    private EditText editText;
    private TodoTextNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_text_note);
        this.viewModel = new ViewModelProvider(this).get(TodoTextNoteViewModel.class);
        viewModel.setExtraTodo(getIntent().getParcelableExtra(EXTRA_TODO));
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        viewModel.getTodoText().observe(this, this::setTodoText);
        viewModel.getSavedTodo().observe(this, todo -> {
            if (todo == null) {
                todoNoteAlertdialog();
            } else
                sendTodoItem(todo);
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

    private void todoNoteAlertdialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.activity_todo_text_note_dialog_title);
        alertDialog.setMessage(getString(R.string.activity_todo_text_note_dialog_message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.activity_todo_text_note_dialog_button_tittle),
                (dialogInterface, i) -> dialogInterface.dismiss()
        );
        alertDialog.show();
    }

    private void setTodoText(String todoText) {
        if (!todoText.equals(editText.getText().toString())) {
            editText.setText(todoText);
        }
    }
}