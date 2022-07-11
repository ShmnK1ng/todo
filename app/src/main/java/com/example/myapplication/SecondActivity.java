package com.example.myapplication;

import static com.example.myapplication.MainActivity.ITEM;
import static com.example.myapplication.MainActivity.Send_Text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;


public class SecondActivity extends AppCompatActivity {

    private Todo todo;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.editText = findViewById(R.id.et_text);
        this.todo = getIntent().getParcelableExtra(ITEM);
        editText.setText(todo.getTodoText());
        addOnButtonClickListener();
    }

    private void sendTextItem() {
        Intent data = new Intent(this, MainActivity.class);
        data.putExtra(Send_Text, todo);
        setResult(RESULT_OK, data);
        finish();
    }

    private void addOnButtonClickListener() {

        ((Toolbar) findViewById(R.id.toolbar)).setOnMenuItemClickListener(item -> {
            String textEntered = editText.getText().toString();
            todo.setTodoText(textEntered);
            sendTextItem();
            return true;

        });

    }

}