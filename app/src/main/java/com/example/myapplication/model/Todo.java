package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;


public class Todo implements Parcelable {

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
    private String uid;
    private String todoText;

    public Todo(String uid, String todoText) {

        this.todoText = todoText;
        this.uid = uid;
    }

    public String getTodoText() {
        return this.todoText;
    }

    public String getUid() {
        return this.uid;
    }

    public void setTodoText(String todo_text) {
        this.todoText = todo_text;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(todoText);

    }

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
