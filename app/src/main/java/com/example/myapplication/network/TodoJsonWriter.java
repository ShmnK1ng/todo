package com.example.myapplication.network;

import android.util.JsonWriter;

import com.example.myapplication.model.Todo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public final class TodoJsonWriter {

    private static final String NAME_TODO_TEXT = "todoText";
    private static final String NAME_INIT_MESSAGE = "message";
    private static final String INIT_MESSAGE_TEXT = "App init";


    public void writeJson(OutputStream out, Todo todo) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writeMessage(writer, todo);
        writer.close();
    }

    public void writeInitJson(OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writer.beginObject();
        writer.name(NAME_INIT_MESSAGE).value(INIT_MESSAGE_TEXT);
        writer.endObject();
        writer.close();
    }

    private void writeMessage(JsonWriter writer, Todo todo) throws IOException {
        writer.beginObject();
        writer.name(NAME_TODO_TEXT).value(todo.getTodoText());
        writer.endObject();
    }
}
