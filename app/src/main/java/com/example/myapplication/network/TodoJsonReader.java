package com.example.myapplication.network;

import android.util.JsonReader;

import com.example.myapplication.Todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class TodoJsonReader {

    private static final String NAME_TODO_TEXT = "todoText";

    public List<Todo> readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readTodoList(reader);
        }
    }

    private List<Todo> readTodoList(JsonReader reader) throws IOException {
        String uid = null;
        List<Todo> todoList = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (!name.equals(NAME_TODO_TEXT)) {
                uid = name;
            }
            String todoText = readTextTodo(reader);
            Todo todo = new Todo(uid, todoText);
            todoList.add(todo);
        }
        reader.endObject();
        return todoList;
    }

    private String readTextTodo(JsonReader reader) throws IOException {
        String textTodo = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(NAME_TODO_TEXT)) {
                textTodo = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return textTodo;
    }
}
