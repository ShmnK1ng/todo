package com.example.myapplication;

import static com.example.myapplication.TodoListActivity.EXTRA_TODO;
import static com.example.myapplication.TodoListActivity.REMOVE_TODO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;


public class TodoTextNoteActivity extends AppCompatActivity {

    private Todo todo;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_text_note);
        this.editText = findViewById(R.id.activity_todo_text_note_edit_text);
        getIntent().getParcelableExtra(EXTRA_TODO);
        this.todo = getIntent().getParcelableExtra(EXTRA_TODO);
        editText.setText(todo.getTodoText());
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
                AlertDialog alertDialog = new AlertDialog.Builder(TodoTextNoteActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Enter note text");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        (dialogInterface, i) -> dialogInterface.dismiss()
                );
                alertDialog.show();
            }else {
                todo.setTodoText(textEntered);
                sendTextItem();
            }
            return true;
        });
    }
    private void addNavigationClickListener() {
        ((Toolbar) findViewById(R.id.activity_todo_text_note_toolbar)).setNavigationOnClickListener(item -> {
            Intent backToTodoListActivity = new Intent(this, TodoListActivity.class);
            backToTodoListActivity.putExtra(REMOVE_TODO, todo);
            setResult(RESULT_CANCELED, backToTodoListActivity);
            finish();
        });
    }
}