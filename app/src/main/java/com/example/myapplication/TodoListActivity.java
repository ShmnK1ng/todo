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


public class TodoListActivity extends AppCompatActivity {

    static final String EXTRA_TODO = "EXTRA_TODO";
    static final String REMOVE_TODO = "REMOVE_TODO";
    ArrayList<Todo> todoList = new ArrayList<>();
    TodoAdapter todoAdapter;
    private int itemPosition;
    Todo todoEnteredText;


    ActivityResultLauncher<Intent> getNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    todoEnteredText = result.getData().getParcelableExtra(EXTRA_TODO);
                    itemPosition = todoList.indexOf(todoEnteredText);
                    todoList.set(itemPosition, todoEnteredText);
                    todoAdapter.notifyItemChanged(itemPosition);
                }
            });
    ActivityResultLauncher<Intent> addNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    todoEnteredText = result.getData().getParcelableExtra(EXTRA_TODO);
                    itemPosition = todoList.indexOf(todoEnteredText);
                    todoList.set(itemPosition, todoEnteredText);
                    todoAdapter.notifyItemChanged(itemPosition);
                }
                if (result.getResultCode() == Activity.RESULT_CANCELED && result.getData() != null) {
                    todoEnteredText = result.getData().getParcelableExtra(REMOVE_TODO);
                    itemPosition = todoList.indexOf(todoEnteredText);
                    todoList.remove(itemPosition);
                    todoAdapter.notifyItemChanged(itemPosition);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        todoList.add(new Todo(UUID.randomUUID().toString(), "first"));
        todoList.add(new Todo(UUID.randomUUID().toString(), "second"));
        todoList.add(new Todo(UUID.randomUUID().toString(), "third"));
        initRecyclerView();
        addButtonClickListener();

    }

    TodoAdapter.OnTodoItemClickListener todoClickListener = todo -> {
        Intent todoTextNoteIntent = new Intent(this, TodoTextNoteActivity.class);
        todoTextNoteIntent.putExtra(EXTRA_TODO, todo);
        getNote.launch(todoTextNoteIntent);
    };

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
            todoList.add(new Todo(UUID.randomUUID().toString(), ""));
            Todo newTodo = todoList.get(todoList.size() - 1);
            startTodoTextNoteActivity.putExtra(EXTRA_TODO, newTodo);
            addNote.launch(startTodoTextNoteActivity);
        });
    }

}