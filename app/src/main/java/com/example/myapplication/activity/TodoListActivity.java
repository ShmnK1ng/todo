package com.example.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TodoAdapter;
import com.example.myapplication.data.TodoDbHelper;
import com.example.myapplication.data.TodoDbHelperWrapper;
import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AlertDialogUtils;
import com.example.myapplication.utilities.ConnectivityManagerWrapper;
import com.example.myapplication.viewmodel.TodoListViewModel;
import com.example.myapplication.viewmodel.TodoListViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoListActivity extends AppCompatActivity {

    static final String EXTRA_TODO = "EXTRA_TODO";
    private static final int SPAN_COUNT = 2;
    private TodoAdapter todoAdapter;
    private TodoListViewModel viewModel;
    private FloatingActionButton addNoteButton;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        viewModelInit();
        setObservers();
        addNoteButton = findViewById(R.id.activity_todo_list_add_note_button);
        addNoteButton.setOnClickListener(view -> viewModel.addTodoClicked());
        swipeRefreshLayout = findViewById(R.id.activity_todo_list_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshRequest());
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

    private void getTodolistError() {
        AlertDialogUtils.showAlertDialog(this, AlertDialogUtils.GET_TODO_LIST_ERROR, dialog -> viewModel.resetGetTodoListErrorEvent());
    }

    private void viewModelInit() {
        TodoDbHelper dbHelper = TodoDbHelper.getInstance(getApplicationContext());
        TodoDbHelperWrapper dbHelperWrapper = new TodoDbHelperWrapper(dbHelper);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManagerWrapper connectivityManagerWrapper = new ConnectivityManagerWrapper(cm);
        this.viewModel = new ViewModelProvider(this, new TodoListViewModelFactory(dbHelperWrapper, connectivityManagerWrapper, dbHelperWrapper)).get(TodoListViewModel.class);
    }

    private void setObservers() {
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
        ProgressBar progressBar = findViewById(R.id.activity_todo_list_progressBar);
        viewModel.getTodoListProgressEvent().observe(this, isEventStarted -> {
            if (isEventStarted) {
                progressBar.setVisibility(View.VISIBLE);
                addNoteButton.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                addNoteButton.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getTodoListErrorEvent().observe(this, isGetTodoListError -> {
            if (isGetTodoListError) {
                getTodolistError();
            }
        });
        viewModel.refreshTodoListEvent().observe(this, refreshStarted -> swipeRefreshLayout.setRefreshing(refreshStarted));
    }
}