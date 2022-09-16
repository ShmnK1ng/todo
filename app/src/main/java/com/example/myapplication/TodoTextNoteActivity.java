package com.example.myapplication;
import static com.example.myapplication.TodoListActivity.EXTRA_TODO;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
        viewModel.getExtraTodo(getIntent().getParcelableExtra(EXTRA_TODO));
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        viewModel.getTodoText().observe(this, todoText -> editText.setText(todoText));
        viewModel.getSavedTodo().observe(this, todo -> {
            if (todo == null) {
                todoNoteAlertdialog();
            } else
                sendTodoItem(todo);
        });
        onButtonClickListener();
        navigationClickListener();
    }

    private void sendTodoItem(Todo todo) {
        Intent data = new Intent(this, TodoListActivity.class);
        data.putExtra(EXTRA_TODO, todo);
        setResult(RESULT_OK, data);
        finish();
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
}