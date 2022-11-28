package com.example.myapplication.data;

import android.database.Cursor;

import com.example.myapplication.utilities.AppIdentifier;

public class TodoDbHelperWrapper implements AppIdentifier {
    private final TodoDbHelper dbHelper;

    public TodoDbHelperWrapper(TodoDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public String getID() {
        String query = "SELECT " + TodoListContract.TodoEntry.TABLE_ID + " FROM " + TodoListContract.TodoEntry.TABLE_NAME;
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(TodoListContract.TodoEntry.TABLE_ID);
            if (id == -1) {
                cursor.close();
                return null;
            }
            String app_ID = cursor.getString(id);
            cursor.close();
            return app_ID;
        } else {
            return null;
        }
    }

    @Override
    public void setID(String id) {
        String insertQuery = "INSERT INTO " + TodoListContract.TodoEntry.TABLE_NAME + " (" + TodoListContract.TodoEntry.TABLE_ID
                + ") VALUES( '" + id + "')";
        dbHelper.getWritableDatabase().execSQL(insertQuery);
    }
}
