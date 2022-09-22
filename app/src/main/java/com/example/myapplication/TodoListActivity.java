package com.example.myapplication;

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
    private static final int SPAN_COUNT = 2;
    private TodoAdapter todoAdapter;
    private TodoListViewModel viewModel;

    private final ActivityResultLauncher<Intent> updateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (intent != null) {
                    viewModel.updateTodo(intent.getParcelableExtra(EXTRA_TODO));
                }
            });
    private final TodoAdapter.OnTodoItemClickListener todoClickListener = todo -> viewModel.todoItemClicked(todo);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        initRecyclerView();
        this.viewModel = new ViewModelProvider(this).get(TodoListViewModel.class);
        viewModel.itemClickEvent().observe(this, todo -> {
            if (todo != null) {
                startEditTodo(todo);
            }
        });
        viewModel.getTodoList().observe(this, todoAdapter::refreshTodoList);
        viewModel.addTodoEvent().observe(this, isItemClicked -> {
            if (isItemClicked) {
                startAddTodo();
            }
        });
        findViewById(R.id.activity_todo_list_add_note_button).setOnClickListener(view -> viewModel.addTodoClicked());
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView todoListRecyclerView = findViewById(R.id.activity_todo_list_recyclerview);
        todoListRecyclerView.setLayoutManager(layoutManager);
        this.todoAdapter = new TodoAdapter(todoClickListener);
        todoListRecyclerView.setAdapter(todoAdapter);
    }

    private void startAddTodo() {
        viewModel.resetClickState();
        updateNote.launch(new Intent(this, TodoTextNoteActivity.class));
    }

    private void startEditTodo(Todo todo) {
        viewModel.resetClickState();
        Intent startEditTodo = new Intent(this, TodoTextNoteActivity.class);
        startEditTodo.putExtra(EXTRA_TODO, todo);
        updateNote.launch(startEditTodo);
    }
}