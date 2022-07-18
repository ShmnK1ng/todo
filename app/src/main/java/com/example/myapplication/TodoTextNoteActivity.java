package com.example.myapplication;

import static com.example.myapplication.TodoListActivity.EXTRA_TODO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        this.todo = getIntent().getParcelableExtra(EXTRA_TODO);
        editText.setText(todo.getTodoText());
        addOnButtonClickListener();
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
            todo.setTodoText(textEntered);
            sendTextItem();
            return true;

        });

    }

}