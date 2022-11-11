package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TodoAdapter;
import com.example.myapplication.model.Todo;
import com.example.myapplication.sharedpreferences.SharedPreferencesWrapper;
import com.example.myapplication.viewmodel.TodoListViewModel;
import com.example.myapplication.viewmodel.TodoListViewModelFactory;

public class TodoListActivity extends AppCompatActivity {

    static final String EXTRA_TODO = "EXTRA_TODO";
    static final String APP_PREFERENCES = "todo_settings";
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
        SharedPreferences serverID = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferencesWrapper sharedPreferencesWrapper = new SharedPreferencesWrapper(serverID);
        setContentView(R.layout.activity_todo_list);
        initRecyclerView();
        this.viewModel = new ViewModelProvider(this, new TodoListViewModelFactory(sharedPreferencesWrapper)).get(TodoListViewModel.class);
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
        viewModel.getTodoListEvent().observe(this, isEventStarted -> {
            if (isEventStarted) {
                findViewById(R.id.activity_todo_list_progressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.activity_todo_list_add_note_button).setVisibility(View.GONE);
            } else {
                findViewById(R.id.activity_todo_list_progressBar).setVisibility(View.INVISIBLE);
                findViewById(R.id.activity_todo_list_add_note_button).setVisibility(View.VISIBLE);
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