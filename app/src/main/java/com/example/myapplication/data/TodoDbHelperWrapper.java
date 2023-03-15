package com.example.myapplication.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.Todo;
import com.example.myapplication.utilities.TodoDao;

import java.util.ArrayList;
import java.util.List;

public class TodoDbHelperWrapper implements TodoDao {
    private final TodoDbHelper dbHelper;

    public final static String SET_ID_QUERY = "INSERT INTO " + TodoListContract.TodoListID.TABLE_NAME + "("
            + TodoListContract.TodoListID.COLUMN_ID + ") VALUES ( '%s' ) ON CONFLICT (" + TodoListContract.TodoListID.COLUMN_ID + ") DO UPDATE SET "
            + TodoListContract.TodoListID.COLUMN_ID + "= '%s'";
    private final static String SAVE_TODO_QUERY = "INSERT INTO " +
            TodoListContract.TodoList.TABLE_NAME + " (" + TodoListContract.TodoList.COLUMN_TODO_ID
            + "," + TodoListContract.TodoList.COLUMN_TODO_TEXT + "," + TodoListContract.TodoList.COLUMN_TODO_ACCOUNT_ID + ") VALUES( '%s', '%s', '%s' )";
    private final static String EDIT_TODO_QUERY = "UPDATE " + TodoListContract.TodoList.TABLE_NAME +
            " SET " + TodoListContract.TodoList.COLUMN_TODO_TEXT + " = '%s' WHERE " +
            TodoListContract.TodoList.COLUMN_TODO_ID + " = '%s';";
    private final static String SAVE_TODO_LIST_QUERY = "REPLACE INTO " + TodoListContract.TodoList.TABLE_NAME + "(" + TodoListContract.TodoList.COLUMN_TODO_ID +
            "," + TodoListContract.TodoList.COLUMN_TODO_TEXT + "," + TodoListContract.TodoList.COLUMN_TODO_ACCOUNT_ID + ") VALUES ('%s', '%s', '%s' )";
    private final static String TODO_LIST_QUERY = "SELECT * FROM " + TodoListContract.TodoList.TABLE_NAME + " WHERE " +
            TodoListContract.TodoList.COLUMN_TODO_ACCOUNT_ID + "= '%s'";

    public TodoDbHelperWrapper(TodoDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void setUserUID(String id) {
        dbHelper.getWritableDatabase().execSQL(String.format(SET_ID_QUERY, id, id));
    }

    @Override
    public void saveTodo(Todo todo, String appUID) {
        dbHelper.getWritableDatabase().execSQL(String.format(SAVE_TODO_QUERY, todo.getUid(), todo.getTodoText(), appUID));
    }

    @Override
    public void editTodo(Todo todo) {
        dbHelper.getWritableDatabase().execSQL(String.format(EDIT_TODO_QUERY, todo.getTodoText(), todo.getUid()));
    }

    @Override
    public void saveTodoList(List<Todo> todoList, String accountUID) {
        SQLiteDatabase TodoDb = dbHelper.getWritableDatabase();
        TodoDb.beginTransaction();
        try {
            for (Todo todo : todoList) {
                TodoDb.execSQL(String.format(SAVE_TODO_LIST_QUERY, todo.getUid(), todo.getTodoText(), accountUID));
            }
            TodoDb.setTransactionSuccessful();
        } finally {
            TodoDb.endTransaction();
        }
    }

    @Override
    public ArrayList<Todo> getTodoList(String appUID) {
        ArrayList<Todo> todoList = new ArrayList<>();
        try (Cursor cursor = dbHelper.getReadableDatabase().rawQuery(String.format(TODO_LIST_QUERY, appUID), null)) {
            int todoIDColumnIndex = cursor.getColumnIndex(TodoListContract.TodoList.COLUMN_TODO_ID);
            int todoTextColumnIndex = cursor.getColumnIndex(TodoListContract.TodoList.COLUMN_TODO_TEXT);
            while (cursor.moveToNext()) {
                String todoID = cursor.getString(todoIDColumnIndex);
                String todoText = cursor.getString(todoTextColumnIndex);
                todoList.add(new Todo(todoID, todoText));
            }
        }
        return todoList;
    }
}
