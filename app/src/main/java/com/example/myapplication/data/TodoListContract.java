package com.example.myapplication.data;

import android.provider.BaseColumns;

public final class TodoListContract {

    public static final class TodoListID implements BaseColumns {
        public final static String TABLE_NAME = "TodoList_ID";
        public final static String COLUMN_ID = "id";
    }
}
