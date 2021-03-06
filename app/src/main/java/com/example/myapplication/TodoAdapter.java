package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private final OnTodoItemClickListener onClickListener;
    private final ArrayList<Todo> todoList;

    public TodoAdapter(OnTodoItemClickListener onClickListener, ArrayList<Todo> todoList) {

        this.onClickListener = onClickListener;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int todoListLayout = R.layout.item_todo;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(todoListLayout, parent, false);

        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.bind(todo);
        holder.itemView.setOnClickListener(view -> onClickListener.OnItemClick(todo));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public interface OnTodoItemClickListener {
        void OnItemClick(Todo todo);
    }

}