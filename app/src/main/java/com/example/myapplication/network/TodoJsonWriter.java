package com.example.myapplication.network;

import android.util.JsonWriter;

import com.example.myapplication.todo.Todo;

import java.io.IOException;
import java.io.StringWriter;

public final class TodoJsonWriter {

    public void writeJson(StringWriter out, Todo todo) throws IOException {
        JsonWriter writer = new JsonWriter(out);
        writer.setIndent("  ");
        writeMessage(writer, todo);
        writer.close();
    }

    public void writeMessage(JsonWriter writer, Todo todo) throws IOException {
        writer.beginObject();
        writer.name("UID").value(todo.getUid());
        writer.name("todoText").value(todo.getTodoText());
        writer.endObject();
    }
}
