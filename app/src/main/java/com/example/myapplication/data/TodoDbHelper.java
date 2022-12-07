package com.example.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoListContract.TodoListID.TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TodoListContract.TodoListID.COLUMN_ID + " TEXT NOT NULL)";

    private static final String DATABASE_NAME = "TodoList.db";
    private static final int DATABASE_VERSION = 1;
    private static volatile TodoDbHelper instance;
    private static final String SQL_ADD_PRIMARY_KEY = "INSERT INTO " + TodoListContract.TodoListID.TABLE_NAME + " (%s, %s) VALUES ('%s', '%s')";

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
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing
    }
}
