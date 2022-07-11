package com.example.myapplication;

import static com.example.myapplication.MainActivity.ITEM;
import static com.example.myapplication.MainActivity.Send_Text;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
=======

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

>>>>>>> efdd45e15a418c50f25fa13d3d68b660a5a9bf7d
=======
>>>>>>> Stashed changes

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

<<<<<<< Updated upstream
=======
>>>>>>> efdd45e15a418c50f25fa13d3d68b660a5a9bf7d
>>>>>>> Stashed changes
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;


public class SecondActivity extends AppCompatActivity {

    private Todo todo;
    private EditText editText;
<<<<<<< Updated upstream
    private ActionMenuItemView toolbar_button;
=======
<<<<<<< HEAD
<<<<<<< HEAD
=======
    private ActionMenuItemView toolbar_button;
>>>>>>> efdd45e15a418c50f25fa13d3d68b660a5a9bf7d
=======
    private ActionMenuItemView toolbar_button;
>>>>>>> efdd45e15a418c50f25fa13d3d68b660a5a9bf7d
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.editText = findViewById(R.id.et_text);
        this.toolbar_button = findViewById(R.id.toolbar_button);
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
<<<<<<< Updated upstream
        toolbar_button.setOnClickListener(view -> {
            String textEntered = editText.getText().toString();
            todo.setTodoText(textEntered);
            sendTextItem();
=======
<<<<<<< HEAD
<<<<<<< HEAD
        ((Toolbar) findViewById(R.id.toolbar)).setOnMenuItemClickListener(item -> {
            String textEntered = editText.getText().toString();
            todo.setTodoText(textEntered);
            sendTextItem();
            return true;
=======
        toolbar_button.setOnClickListener(view -> {
            String textEntered = editText.getText().toString();
            todo.setTodoText(textEntered);
            sendTextItem();
>>>>>>> efdd45e15a418c50f25fa13d3d68b660a5a9bf7d
=======
        toolbar_button.setOnClickListener(view -> {
            String textEntered = editText.getText().toString();
            todo.setTodoText(textEntered);
            sendTextItem();
>>>>>>> efdd45e15a418c50f25fa13d3d68b660a5a9bf7d
>>>>>>> Stashed changes
        });

    }

}