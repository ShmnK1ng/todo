package com.example.myapplication.data;

import android.provider.BaseColumns;

public final class TodoListContract {

    public static final class TodoListID implements BaseColumns {
        public final static String TABLE_NAME = "TodoList_ID";
        public final static String COLUMN_PRIMARY_KEY = "TodoList_id";
        public final static String COLUMN_ID = "id";
    }

    public static final class TodoList implements BaseColumns {
        public final static String TABLE_NAME = "TodoList";
        public final static String COLUMN_TODO_ID = "Todo_id";
        public final static String COLUMN_TODO_TEXT = "TEXT";
    }
}
