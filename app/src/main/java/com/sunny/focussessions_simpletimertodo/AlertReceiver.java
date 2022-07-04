package com.sunny.focussessions_simpletimertodo;





import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.sunny.focussessions_simpletimertodo.data.TodoSessionDao;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.util.List;

public class AlertReceiver extends BroadcastReceiver {
    private static final String ACTION_START_FOREGROUND_SERVICE = "startService";
    private static final String TAG = "AlertReceiver";
    //shared pref
    private static final String MESSAGE_ID = "message_prefs" ;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "broadcastReceived", Toast.LENGTH_LONG).show();
        saveTimeToSession(context,intent);
        saveTimeToTodo(context,intent);
//        Log.d(TAG, "onReceive: "+intent.getIntExtra("check",0));
        startRingService(context);
    }

    private void saveTimeToTodo(Context context,Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        int todoId = sharedPref.getInt(context.getString(R.string.spinnerSelectedTodoId),0);

        if(todoId != 0){
            TodoSessionDatabase todoSessionDatabase = TodoSessionDatabase.getInstance(context);
            TodoSessionDao todoSessionDao = todoSessionDatabase.todoSessionDao();

            int alarmTime = intent.getIntExtra("alarmTime",1);

            Todo todo = todoSessionDao.getTodoWithTodoId(todoId);
//            Log.d(TAG, "saveTimeToTodo: successTodoTime :"+todo.getTime_spent());
            int totalTime = todo.getTime_spent()+alarmTime;
            todo.setTime_spent(totalTime);
            todoSessionDao.updateTodo(todo);
//            Log.d(TAG, "saveTimeToTodo: successAlarmTime :"+alarmTime);
//            Log.d(TAG, "saveTimeToTodo: successTotalTime :"+totalTime);

        }
    }

    private void saveTimeToSession(Context context,Intent intent) {

        SharedPreferences sharedPref = context.getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        long todaysDate = sharedPref.getLong(context.getString(R.string.todaysDateExactTime),1);

        TodoSessionDatabase todoSessionDatabase = TodoSessionDatabase.getInstance(context);
        TodoSessionDao todoSessionDao = todoSessionDatabase.todoSessionDao();


//        Log.d(TAG, "todaysDate"+todaysDate);
        int alarmTime = intent.getIntExtra("alarmTime",1);

        if(todoSessionDao.getSessionWithDate(todaysDate) != null){
            Session session = todoSessionDao.getSessionWithDate(todaysDate);
//            Log.d(TAG, "saveTimeToSession: successSessionTime :"+session.getTime_spent());
            int totalAlarmTime = session.getTime_spent() + alarmTime;
            session.setTime_spent(totalAlarmTime);

            todoSessionDao.updateSession(session);
//            Log.d(TAG, "saveTimeToSession: successAlarmTime :"+alarmTime);
//            Log.d(TAG, "saveTimeToSession: successTotalTime :"+totalAlarmTime);
        }else{
//            Log.d(TAG, "saveTimeToSession: what session is null");
        }




    }

    private void startRingService(Context context) {
        Intent serviceIntent = new Intent(context,RingService.class);
        serviceIntent.setAction(ACTION_START_FOREGROUND_SERVICE);
        context.startService(serviceIntent);
    }
}
