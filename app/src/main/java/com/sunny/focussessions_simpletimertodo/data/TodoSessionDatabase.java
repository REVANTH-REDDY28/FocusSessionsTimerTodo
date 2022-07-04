package com.sunny.focussessions_simpletimertodo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

@Database(entities = {Session.class,Todo.class},
            version = 1)
public abstract class TodoSessionDatabase extends RoomDatabase {
    public abstract TodoSessionDao todoSessionDao();
    public static TodoSessionDatabase INSTANCE;

    public static synchronized TodoSessionDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TodoSessionDatabase.class, "school_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

}
