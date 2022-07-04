package com.sunny.focussessions_simpletimertodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDao;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;

public class ShowTimerActivity extends AppCompatActivity {
    private static final String TAG = "ShowTimerActivity";

    //shared pref
    private static final String MESSAGE_ID = "message_prefs" ;

    TextView timerTextView;
    long millisInFuture;
    int dummyMillis = 3000;
    ImageButton stopButton;
    CountDownTimer timer;
    private BottomNavigationView bottomNavigationView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timer);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        timerTextView = findViewById(R.id.textView_timer);
        stopButton = findViewById(R.id.btn_stop);

        millisInFuture = 50000;
        long setTime = getIntent().getLongExtra("time",60000);
        millisInFuture = setTime;
//        startTimer(millisInFuture);

        timer = new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.isAlarmSet), false);
                editor.apply();

                Intent intent = new Intent(ShowTimerActivity.this, FocusActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        };
        timer.start();


        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//problem2
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startTodoSessionSaveService();
//                }
                //problem1
//                saveTimes();
                timer.cancel();
                saveTimes();

                SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.isAlarmSet),false);
                editor.apply();
                //unregister alarm
//                Log.d(TAG, "onClick: "+"unregistering alarm initiated");
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(),AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1,intent,0);
                alarmManager.cancel(pendingIntent);

//                Log.d(TAG, "onClick: "+"unregistering alarm sucessfull");





                Intent focusActivityIntent = new Intent(ShowTimerActivity.this,FocusActivity.class);
                intent.putExtra("hasData",true);
                intent.putExtra("into",1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(focusActivityIntent);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.focus);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.history:
                        Intent intent = new Intent(ShowTimerActivity.this,HistoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.todo:
                        Intent intent2 = new Intent(ShowTimerActivity.this,TodoActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                }
                return false;
            }
        });


    }

    private void saveTimes() {
        saveTimeToTodo();
        saveTimeToSession();
    }


    private void startTodoSessionSaveService() {
        Intent serviceIntent = new Intent(this,TodoSessionSaveService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
    }

    private void saveTimeToTodo() {
        int foucusedTimeInMin = getFocusedTime();
//        Log.d(TAG, "saveTimeToTodo: focusedTimeInMin :"+foucusedTimeInMin);
//        int foucusedTimeInMin = 2;

        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        int todoId = sharedPref.getInt(getString(R.string.spinnerSelectedTodoId),0);


        if(todoId != 0){
            TodoSessionDatabase todoSessionDatabase = TodoSessionDatabase.getInstance(this);
            TodoSessionDao todoSessionDao = todoSessionDatabase.todoSessionDao();


            Todo todo = todoSessionDao.getTodoWithTodoId(todoId);
//            Log.d(TAG, "saveTimeToTodo: todoTime :"+todo.getTime_spent());
            int totalTime = todo.getTime_spent()+foucusedTimeInMin;
//            Log.d(TAG, "saveTimeToTodo: totalTime :"+totalTime);
            todo.setTime_spent(totalTime);
            todoSessionDao.updateTodo(todo);

        }
    }

    private void saveTimeToSession() {
        int foucusedTimeInMin = getFocusedTime();
//        Log.d(TAG, "saveTimeToSession: focusedTimeInMin :"+foucusedTimeInMin);
        long todaysDate = getTodaysExactDate();
        TodoSessionDatabase todoSessionDatabase = TodoSessionDatabase.getInstance(this);
        TodoSessionDao todoSessionDao = todoSessionDatabase.todoSessionDao();

        if(todoSessionDao.getSessionWithDate(todaysDate) != null){
            Session session = todoSessionDao.getSessionWithDate(todaysDate);
//            Log.d(TAG, "saveTimeToSession: sessionTime :"+session.getTime_spent());
            int totalAlarmTime = session.getTime_spent() + foucusedTimeInMin;
//            Log.d(TAG, "saveTimeToSession: totalTime :"+totalAlarmTime);
            session.setTime_spent(totalAlarmTime);

            todoSessionDao.updateSession(session);
        }//            Log.d(TAG, "saveTimeToSession: what session is null");


    }

    private long getTodaysExactDate() {
        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        long todaysDate = sharedPref.getLong(getString(R.string.todaysDateExactTime),1);
//        Log.d(TAG, "getTodaysExactDate: "+todaysDate);
        return todaysDate;
    }

    private int getFocusedTime() {
        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID, Context.MODE_PRIVATE);
        long alarmStartDate = sharedPref.getLong(getString(R.string.alarmStartDate),1);
//        Log.d(TAG, "getFocusedTime: alarmStartDate :"+alarmStartDate);


        Date currentDate = new Date();
//        Log.d(TAG, "getFocusedTime: currentDate :"+currentDate.getTime());
        long focusedTime = currentDate.getTime()-alarmStartDate;
//        Log.d(TAG, "getFocusedTime: focusedTime :"+focusedTime);
        Long l = (focusedTime/1000)/60;
        int foucusedTimeInMinutes = l.intValue();
        return foucusedTimeInMinutes;
    }

    private void startTimer(long millisInFuture) {
        new CountDownTimer(millisInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(MessageFormat.format("{0}:{1}:{2}", f.format(hour), f.format(min), f.format(sec)));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.isAlarmSet),false);
                editor.apply();

                Intent intent = new Intent(ShowTimerActivity.this,FocusActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }
        }.start();
    }
}