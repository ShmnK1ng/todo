package com.example.myapplication;
import android.app.Activity;
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
    private TodoViewModel viewModel;


    ActivityResultLauncher<Intent> updateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Todo todoEnteredText = result.getData().getParcelableExtra(EXTRA_TODO);
                    if (viewModel.containsTodo(todoEnteredText)) {
                        int itemPosition = viewModel.getPosition(todoEnteredText);
                        viewModel.setTodo(itemPosition, todoEnteredText);
                        todoAdapter.notifyItemChanged(itemPosition);
                    } else {
                        viewModel.addTodo(todoEnteredText);
                        todoAdapter.notifyItemChanged(viewModel.getTodoListSize());
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
        this.viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        initRecyclerView();
        addButtonClickListener();

    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView todoList_RecyclerView = findViewById(R.id.activity_todo_list_recyclerview);
        todoList_RecyclerView.setLayoutManager(layoutManager);
        this.todoAdapter = new TodoAdapter(todoClickListener, viewModel.getTodolist());
        todoList_RecyclerView.setAdapter(todoAdapter);
    }

    public void addButtonClickListener() {
        findViewById(R.id.activity_todo_list_add_note_button).setOnClickListener(view -> {
            Intent startTodoTextNoteActivity = new Intent(this, TodoTextNoteActivity.class);
            updateNote.launch(startTodoTextNoteActivity);
        });
    }
}