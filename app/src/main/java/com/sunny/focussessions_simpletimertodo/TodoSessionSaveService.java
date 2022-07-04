package com.sunny.focussessions_simpletimertodo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sunny.focussessions_simpletimertodo.data.TodoSessionDao;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.util.Date;

public class TodoSessionSaveService extends Service {
    private static final String MESSAGE_ID = "message_prefs" ;
//    private static final String TAG = "TodoSessionSaveService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        saveTimeToSession();
        saveTimeToTodo();
        return START_NOT_STICKY;
    }

    private void saveTimeToSession() {
        int foucusedTimeInMin = getFocusedTime();
        long todaysDate = getTodaysExactDate();
        TodoSessionDatabase todoSessionDatabase = TodoSessionDatabase.getInstance(this);
        TodoSessionDao todoSessionDao = todoSessionDatabase.todoSessionDao();

        if(todoSessionDao.getSessionWithDate(todaysDate) != null){
            Session session = todoSessionDao.getSessionWithDate(todaysDate);
            int totalAlarmTime = session.getTime_spent() + foucusedTimeInMin;
            session.setTime_spent(totalAlarmTime);

            todoSessionDao.updateSession(session);
        }//            Log.d(TAG, "saveTimeToSession: what session is null");


    }

    private long getTodaysExactDate() {
        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        long todaysDate = sharedPref.getLong(getString(R.string.todaysDateExactTime),1);
        return todaysDate;
    }

    private int getFocusedTime() {
        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        long alarmStartDate = sharedPref.getLong(getString(R.string.alarmStartDate),1);
        Date currentDate = new Date();
        long focusedTime = currentDate.getTime()-alarmStartDate;
        Long l = (focusedTime/1000)/60;
        int foucusedTimeInMinutes = l.intValue();
        return foucusedTimeInMinutes;
    }

    private void saveTimeToTodo() {
        int foucusedTimeInMin = getFocusedTime();

        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        int todoId = sharedPref.getInt(getString(R.string.spinnerSelectedTodoId),0);


        if(todoId != 0){
            TodoSessionDatabase todoSessionDatabase = TodoSessionDatabase.getInstance(this);
            TodoSessionDao todoSessionDao = todoSessionDatabase.todoSessionDao();


            Todo todo = todoSessionDao.getTodoWithTodoId(todoId);
            int totalTime = todo.time_spent+foucusedTimeInMin;
            todo.setTime_spent(totalTime);
            todoSessionDao.updateTodo(todo);

        }
    }
}
