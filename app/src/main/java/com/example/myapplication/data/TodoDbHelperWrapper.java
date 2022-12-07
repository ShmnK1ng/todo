package com.example.myapplication.data;

import android.database.Cursor;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.AppIdentifier;
import com.example.myapplication.utilities.TodoDAO;

public class TodoDbHelperWrapper implements AppIdentifier, TodoDAO {
    private final TodoDbHelper dbHelper;
    private static final int PRIMARY_KEY_VALUE = 1;
    private static final int COLUMN_NOT_EXIST = -1;
    private final static String GET_ID_QUERY = "SELECT " + TodoListContract.TodoListID.COLUMN_ID +
            " FROM " + TodoListContract.TodoListID.TABLE_NAME;
    private final static String SET_ID_QUERY = "REPLACE INTO " + TodoListContract.TodoListID.TABLE_NAME + "(" + TodoListContract.TodoListID.COLUMN_PRIMARY_KEY +
            "," + TodoListContract.TodoListID.COLUMN_ID + ") VALUES ('%s', '%s')";
    private final static String SAVE_TODO_QUERY = "INSERT INTO " +
            TodoListContract.TodoList.TABLE_NAME + " (" + TodoListContract.TodoList.COLUMN_TODO_ID
            + "," + TodoListContract.TodoList.COLUMN_TODO_TEXT + ") VALUES( '%s', '%s' )";
    private final static String EDIT_TODO_QUERY = "UPDATE " + TodoListContract.TodoList.TABLE_NAME +
            " SET " + TodoListContract.TodoList.COLUMN_TODO_TEXT + " = '%s' WHERE " +
            TodoListContract.TodoList.COLUMN_TODO_ID + " = '%s';";

    public TodoDbHelperWrapper(TodoDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public String getID() {
        try (Cursor cursor = dbHelper.getReadableDatabase().rawQuery(GET_ID_QUERY, null)) {
            int index = cursor.getColumnIndex(TodoListContract.TodoListID.COLUMN_ID);
            String id = null;
            if (cursor.moveToFirst() && index != COLUMN_NOT_EXIST) {
                id = cursor.getString(index);
            }
            return id;
        }
    }

    @Override
    public void setID(String id) {
        dbHelper.getWritableDatabase()
                .execSQL(String.format(SET_ID_QUERY, PRIMARY_KEY_VALUE, id));
    }

    @Override
    public void saveTodo(Todo todo) {
        dbHelper.getWritableDatabase().execSQL(String.format(SAVE_TODO_QUERY, todo.getUid(), todo.getTodoText()));
    }

    @Override
    public void editTodo(Todo todo) {
        dbHelper.getWritableDatabase().execSQL(String.format(EDIT_TODO_QUERY, todo.getTodoText(), todo.getUid()));
    }
}
