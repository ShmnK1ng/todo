package com.example.myapplication;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private final int numberItems;
    private final OnTodoItemClickListener onClickListener;


    public TodoAdapter(int numberOfItems, Context parent, OnTodoItemClickListener onClickListener) {
        numberItems = numberOfItems;
        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIDForTodoList = R.layout.grid_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIDForTodoList, parent, false);

        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.itemView.setOnClickListener(view -> onClickListener.OnItemClick(position));

    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    public interface OnTodoItemClickListener {
        void OnItemClick(int position);
    }

}