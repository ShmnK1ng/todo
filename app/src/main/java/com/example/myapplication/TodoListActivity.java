package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;


public class TodoListActivity extends AppCompatActivity {

    static final String EXTRA_TODO = "EXTRA_TODO";
    private TodoAdapter todoAdapter;
    private TodoListViewModel viewModel;
    private Todo todo;

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> updateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    viewModel.updateTodo(result.getData().getParcelableExtra(EXTRA_TODO));
                }
            });

    TodoAdapter.OnTodoItemClickListener todoClickListener = todo -> {
        this.todo = todo;
        viewModel.onTodoItemClickState();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        this.viewModel = new ViewModelProvider(this).get(TodoListViewModel.class);
        final Observer<ArrayList<Todo>> todoListObserver = todos -> todoAdapter.refreshTodoList(todos);
        final Observer<Boolean> onButtonClickStateObserver = aBoolean -> startActivity();
        viewModel.getOnClickState().observe(this, onButtonClickStateObserver);
        viewModel.getTodoList().observe(this, todoListObserver);
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

    public void startActivity() {
        Intent startTodoTextNoteActivity = new Intent(this, TodoTextNoteActivity.class);
        if (viewModel.getCurrentOnButtonClickState() == Boolean.TRUE) {
            viewModel.updateOnButtonClickState();
        }
        if (viewModel.getCurrentOnTodoItemClickState() == Boolean.TRUE) {
            startTodoTextNoteActivity.putExtra(EXTRA_TODO, todo);
            viewModel.updateOnItemTodoClickState();
        }
        updateNote.launch(startTodoTextNoteActivity);
    }
}