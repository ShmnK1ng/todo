package com.example.myapplication;



import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private int numberItems;
    private Context parent;

    public TodoAdapter(int numberOfItems, Context parent) {
        numberItems = numberOfItems;
        this.parent = parent;

    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIDForTodoList = R.layout.grid_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIDForTodoList, parent, false);
        TodoViewHolder viewHolder = new TodoViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, SecondActivity.class);
                parent.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView todoTextView;

        void bind(int todoIndex) {
            todoTextView.setText(String.valueOf(todoIndex));
        }

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

















}
