package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;


public class TodoListActivity extends AppCompatActivity {

    static final String EXTRA_TODO = "EXTRA_TODO";
    private final ArrayList<Todo> todoList = new ArrayList<>();
    private TodoAdapter todoAdapter;


    ActivityResultLauncher<Intent> updateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Todo todoEnteredText = result.getData().getParcelableExtra(EXTRA_TODO);
                    if (todoList.contains(todoEnteredText)) {
                        int itemPosition = todoList.indexOf(todoEnteredText);
                        todoList.set(itemPosition, todoEnteredText);
                        todoAdapter.notifyItemChanged(itemPosition);
                    } else {
                        todoList.add(todoEnteredText);
                        todoAdapter.notifyItemChanged(todoList.size());
                    }
                }
            });
    TodoAdapter.OnTodoItemClickListener todoClickListener = todo -> {
        Intent todoTextNoteIntent = new Intent(this, TodoTextNoteActivity.class);
        todoTextNoteIntent.putExtra(EXTRA_TODO, todo);
        updateNote.launch(todoTextNoteIntent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initRecyclerView();
        addButtonClickListener();

    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView todoList_RecyclerView = findViewById(R.id.activity_todo_list_recyclerview);
        todoList_RecyclerView.setLayoutManager(layoutManager);
        this.todoAdapter = new TodoAdapter(todoClickListener, todoList);
        todoList_RecyclerView.setAdapter(todoAdapter);
    }

    public void addButtonClickListener() {
        findViewById(R.id.activity_todo_list_add_note_button).setOnClickListener(view -> {
            Intent startTodoTextNoteActivity = new Intent(this, TodoTextNoteActivity.class);
            updateNote.launch(startTodoTextNoteActivity);
        });
    }

}