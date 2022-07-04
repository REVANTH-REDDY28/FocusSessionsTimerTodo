package com.sunny.focussessions_simpletimertodo.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class Todo {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int todo_id;
    public String todo_text;
    public int time_spent = 0;


    public Todo(int todo_id, String todo_text, int time_spent) {
        this.todo_id = todo_id;
        this.todo_text = todo_text;
        this.time_spent = time_spent;
    }

    @Ignore
    public Todo(String todo_text) {
        this.todo_text = todo_text;
    }

    @Ignore
    public Todo(int todo_id,String todo_text){
        this.todo_id = todo_id;
        this.todo_text = todo_text;
    }



    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getTodo_text() {
        return todo_text;
    }

    public void setTodo_text(String todo_text) {
        this.todo_text = todo_text;
    }

    public int getTime_spent() {
        return time_spent;
    }

    public void setTime_spent(int time_spent) {
        this.time_spent = time_spent;
    }

    @NonNull
    @Override
    public String toString() {
        return todo_text;
    }
}
