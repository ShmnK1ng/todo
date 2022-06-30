package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;


public class Todo implements Parcelable {

    private String todo_text;
    private final String uid;

    public Todo(String uid, String todo_text) {

        this.todo_text = todo_text;
        this.uid = uid;
    }

    public String getTodoText() {
        return this.todo_text;
    }

    public void setTodoText(String todo_text) {
        this.todo_text = todo_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(todo_text);

    }

    public static final Parcelable.Creator<Todo> CREATOR = new Parcelable.Creator<Todo>() {

        @Override
        public Todo createFromParcel(Parcel parcel) {
            String uid = parcel.readString();
            String todo_text = parcel.readString();
            return new Todo(uid, todo_text);
        }

        @Override
        public Todo[] newArray(int i) {
            return new Todo[i];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;
        Todo todo = (Todo) o;
        return uid.equals(todo.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
