package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    static final String ITEM = "ITEM";
    static final String Send_Text = "Send_Text";
    ArrayList<Todo> todoList = new ArrayList<>();
    TodoAdapter todoAdapter;


    ActivityResultLauncher<Intent> getNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Todo todoEnteredText = result.getData().getParcelableExtra(Send_Text);
                    int itemPosition = todoList.indexOf(todoEnteredText);
                    todoList.set(itemPosition, todoEnteredText);
                    todoAdapter.notifyItemChanged(itemPosition);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoList.add(new Todo(UUID.randomUUID().toString(), "first"));
        todoList.add(new Todo(UUID.randomUUID().toString(), "second"));
        todoList.add(new Todo(UUID.randomUUID().toString(), "third"));
        initRecyclerView();
    }

    TodoAdapter.OnTodoItemClickListener todoClickListener = todo -> {
        Intent SecondActivityIntent = new Intent(this, SecondActivity.class);
        SecondActivityIntent.putExtra(ITEM, todo);
        getNote.launch(SecondActivityIntent);
    };

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView todoList_RecyclerView = findViewById(R.id.rv_todo);
        todoList_RecyclerView.setLayoutManager(layoutManager);
        this.todoAdapter = new TodoAdapter(todoClickListener, todoList);
        todoList_RecyclerView.setAdapter(todoAdapter);
    }

}