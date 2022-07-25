package com.example.myapplication;

import static com.example.myapplication.TodoListActivity.EXTRA_TODO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.UUID;


public class TodoTextNoteActivity extends AppCompatActivity {

    private Todo todo;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_text_note);
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        this.todo = getIntent().getParcelableExtra(EXTRA_TODO);
        if (todo != null) {
            editText.setText(todo.getTodoText());
        }
        addOnButtonClickListener();
        addNavigationClickListener();
    }

    private void sendTextItem() {
        Intent data = new Intent(this, TodoListActivity.class);
        data.putExtra(EXTRA_TODO, todo);
        setResult(RESULT_OK, data);
        finish();
    }

    private void addOnButtonClickListener() {
        ((Toolbar) findViewById(R.id.activity_todo_text_note_toolbar)).setOnMenuItemClickListener(item -> {
            String textEntered = editText.getText().toString();
            if (textEntered.length() == 0) {
                todoNoteAlertdialog();
            } else {
                if (todo == null) {
                    String uid = UUID.randomUUID().toString();
                    todo = new Todo(uid, textEntered);
                } else todo.setTodoText(textEntered);
                sendTextItem();
            }
            return true;
        });
    }

    private void addNavigationClickListener() {
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