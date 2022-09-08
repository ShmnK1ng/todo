package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class TodoListActivity extends AppCompatActivity {

    static final String EXTRA_TODO = "EXTRA_TODO";
    private TodoAdapter todoAdapter;
    private TodoListViewModel viewModel;

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> updateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    viewModel.updateTodo(result.getData().getParcelableExtra(EXTRA_TODO));
                }
            });

    TodoAdapter.OnTodoItemClickListener todoClickListener = todo -> viewModel.onTodoItemClicked(todo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        this.viewModel = new ViewModelProvider(this).get(TodoListViewModel.class);
        viewModel.getTodoList().observe(this, todos -> todoAdapter.refreshTodoList(todos));
        viewModel.OnItemClickEvent().observe(this, aBoolean -> {
            if (viewModel.onClicked()) OnItemClickedStartActivity();
        });
        viewModel.OnButtonClickEvent().observe(this, aBoolean -> {
            if (viewModel.onClicked()) OnButtonClickedStartActivity();
        });
        initRecyclerView();
        addButtonClickListener();
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView todoList_RecyclerView = findViewById(R.id.activity_todo_list_recyclerview);
        todoList_RecyclerView.setLayoutManager(layoutManager);
        this.todoAdapter = new TodoAdapter(todoClickListener, viewModel.getTodoListRepository());
        todoList_RecyclerView.setAdapter(todoAdapter);
    }

    public void addButtonClickListener() {
        findViewById(R.id.activity_todo_list_add_note_button).setOnClickListener(view -> viewModel.onButtonClicked());
    }

    public void OnButtonClickedStartActivity() {
        viewModel.resetOnClickedState();
        updateNote.launch(new Intent(this, TodoTextNoteActivity.class));
    }

    public void OnItemClickedStartActivity() {
        viewModel.resetOnClickedState();
        Intent startTodoTextNoteActivity = new Intent(this, TodoTextNoteActivity.class);
        startTodoTextNoteActivity.putExtra(EXTRA_TODO, viewModel.getTodo());
        updateNote.launch(startTodoTextNoteActivity);
    }
}