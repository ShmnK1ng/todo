package com.example.myapplication.data;

import android.provider.BaseColumns;

public final class TodoListContract {

    private TodoListContract() {
    }

    public static final class TodoEntry implements BaseColumns {
        public final static String TABLE_ID = "identifier";
        public final static String TABLE_NAME = "todos";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_TEXT = "text";
    }

}
