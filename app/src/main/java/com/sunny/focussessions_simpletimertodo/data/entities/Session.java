package com.sunny.focussessions_simpletimertodo.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "session_table")
public class Session {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int session_id;

    public long date;
    public int time_spent=0;

    public Session(int session_id, long date, int time_spent) {
        this.session_id = session_id;
        this.date = date;
        this.time_spent = time_spent;
    }
    @Ignore
    public Session(long date) {
        this.date = date;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
        return "\n"+"sessionId: "+session_id
                +" date: "+new SimpleDateFormat(" dd/MM/yyyy ").format(new Date(date))+
                " timeSpent: "+time_spent;
    }
}
