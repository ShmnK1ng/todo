package com.example.myapplication.network;

import android.util.JsonReader;
import android.util.JsonToken;

import com.example.myapplication.model.Todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class TodoJsonReader {

    private static final String NAME_TODO_TEXT = "todoText";
    private static final String NAME_APP_ID = "name";

    private static final String NAME_INIT_MESSAGE = "message";

    public List<Todo> readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readTodoList(reader);
        }
    }

    public String readJsonFromServer(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String appID = null;
            while (reader.hasNext()) {
                if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                } else if (reader.peek() == JsonToken.END_OBJECT) {
                    reader.endObject();
                }
                if (reader.peek() == JsonToken.NAME) {
                    String name = reader.nextName();
                    if (name.equals(NAME_APP_ID)) {
                        appID = reader.nextString();
                    }
                }
            }
            return appID;
        }
    }


    private List<Todo> readTodoList(JsonReader reader) throws IOException {
        List<Todo> todoList = new ArrayList<>();

        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
            if (reader.peek() == JsonToken.END_OBJECT) {
                reader.endObject();
            }
            if (reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                if (!name.equals(NAME_TODO_TEXT) && !name.equals(NAME_INIT_MESSAGE)) {
                    String todoText = readTextTodo(reader);
                    Todo todo = new Todo(name, todoText);
                    todoList.add(todo);
                } else {
                    reader.skipValue();
                }
            }
        }
        return todoList;
    }

    private String readTextTodo(JsonReader reader) throws IOException {
        String textTodo = null;

        if (reader.peek() == JsonToken.BEGIN_OBJECT) {
            reader.beginObject();
        }
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                if (name.equals(NAME_TODO_TEXT)) {
                    textTodo = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
        }
        if (reader.peek() == JsonToken.END_OBJECT) {
            reader.endObject();
        }
        return textTodo;
    }
}
