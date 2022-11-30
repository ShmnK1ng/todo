package com.example.myapplication.data;

import android.database.Cursor;

import com.example.myapplication.utilities.AppIdentifier;

public class TodoDbHelperWrapper implements AppIdentifier {
    private final TodoDbHelper dbHelper;
    private static final int COLUMN_NOT_EXIST = -1;
    private final static String GET_ID_QUERY = "SELECT " + TodoListContract.TodoListID.COLUMN_ID + " FROM " + TodoListContract.TodoListID.TABLE_NAME;

    public TodoDbHelperWrapper(TodoDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public String getID() {
        try (Cursor cursor = dbHelper.getReadableDatabase().rawQuery(GET_ID_QUERY, null)) {
            int id = cursor.getColumnIndex(TodoListContract.TodoListID.COLUMN_ID);
            if (cursor.moveToFirst() && id != COLUMN_NOT_EXIST) {
                return cursor.getString(id);
            } else {
                return null;
            }
        }
    }

    @Override
    public void setID(String id) {
        String insertQuery = "INSERT INTO " + TodoListContract.TodoListID.TABLE_NAME + " (" + TodoListContract.TodoListID.COLUMN_ID
                + ") VALUES( '" + id + "')";
        dbHelper.getWritableDatabase().execSQL(insertQuery);
    }
}
