package com.sunny.focussessions_simpletimertodo.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.util.Date;
import java.util.List;

@Dao
public interface TodoSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTodo(Todo todo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSession(Session session);

    @Transaction
    @Query("SELECT * FROM todo_table")
    public List<Todo> getAllTodos();

    @Transaction
    @Query("SELECT * FROM session_table")
    public List<Session> getAllSessions();

    @Query("SELECT * FROM session_table WHERE date = :dateInTime")
    public Session getSessionWithDate(long dateInTime);

    @Query("DELETE FROM todo_table WHERE todo_id =:todoId")
    public void deleteTodoByTodoId(int todoId);

    @Query("SELECT * FROM session_table WHERE session_id =:sessionId")
    public Session getSessionBySessionId(int sessionId);

    @Update(entity = Session.class)
    public void updateSession(Session session);

    @Query("SELECT * FROM todo_table WHERE todo_id =:todoId")
    public Todo getTodoWithTodoId(int todoId);

    @Update(entity = Todo.class)
    public void updateTodo(Todo todo);

    @Query("DELETE FROM session_table")
    public void deleteAllSessions();
}
