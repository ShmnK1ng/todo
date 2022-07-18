package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewItem;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewItem = itemView.findViewById(R.id.item_todo_text);

    }

    public void bind(Todo todo) {
        textViewItem.setText(todo.getTodoText());

    }

}