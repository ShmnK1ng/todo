package com.example.myapplication;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {

    public TextView TextViewItem;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        TextViewItem = itemView.findViewById(R.id.tv_text_item);

    }
    public void bind (Todo todo) {
        TextViewItem.setText(todo.getTodoText());

    }
}