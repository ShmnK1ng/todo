package com.example.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_TODO_LIST_ID_TABLE =
            "CREATE TABLE " + TodoListContract.TodoListID.TABLE_NAME + " (" + TodoListContract.TodoListID.COLUMN_PRIMARY_KEY +
                    " TEXT PRIMARY KEY, " + TodoListContract.TodoListID.COLUMN_ID + " TEXT NOT NULL)";
    private static final String DATABASE_NAME = "TodoList.db";
    private static final int DATABASE_VERSION = 1;
    private static volatile TodoDbHelper instance;
    private static final String SQL_CREATE_TODO_LIST_TABLE =
            "CREATE TABLE " + TodoListContract.TodoList.TABLE_NAME + " (" + TodoListContract.TodoList.COLUMN_TODO_ID
                    + " TEXT PRIMARY KEY, " + TodoListContract.TodoList.COLUMN_TODO_TEXT + " TEXT NOT NULL)";

    private TodoDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TodoDbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (TodoDbHelper.class) {
                if (instance == null) {
                    instance = new TodoDbHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO_LIST_ID_TABLE);
        db.execSQL(SQL_CREATE_TODO_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing
    }
}
