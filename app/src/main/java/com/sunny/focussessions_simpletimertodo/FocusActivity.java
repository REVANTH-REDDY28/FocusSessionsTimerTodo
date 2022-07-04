package com.sunny.focussessions_simpletimertodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDao;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FocusActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "FocusActivity";
    BottomNavigationView bottomNavigationView;

    //shared pref
    private static final String MESSAGE_ID = "message_prefs" ;

    //copy
    int setTime = 45;
    long alarmTimeInMillis = setTime*60*1000;
    boolean isAlarmSet = false;
    long alarmSetTime;
    TextView setTime_textView;
    ImageButton start_btn;
    ImageButton plus15_btn;
    ImageButton minus15_btn;
    ImageButton plus1_btn;
    ImageButton minus1_btn;
    TextView todayDateTextView;
    TextView tipsTextView;
    
    //spinner
    Spinner todoSpinnerView;
    List<Todo> todoList = new ArrayList<>();
    TodoSessionDatabase db;
    TodoSessionDao dao;
    ArrayAdapter<Todo> todoArrayAdapter;
    
    int selectedTodoId;

    Date todayDate = new Date();
    long todaysDateExactTime;
    List<Session> sessionList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);


//        Intent intent = getIntent();

////        Log.d(TAG, "onCreate: "+"00000000000000000000000000--"+intent.getIntExtra("into",0));
//        if(intent.getBooleanExtra("hasData",false)){
////            Log.d(TAG, "onCreate: "+"some data focus activity intent");
//        }else{
////            Log.d(TAG, "onCreate: "+"no data in focus activity intent");
//        }


        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        isAlarmSet = sharedPref.getBoolean(getString(R.string.isAlarmSet),false);
        alarmSetTime = sharedPref.getLong("alarmSetTime",System.currentTimeMillis()+6000);
//        Log.d(TAG, "onCreate: isAlarmSet "+isAlarmSet);


        if(isAlarmSet){
            startTimer(alarmSetTime-System.currentTimeMillis());
        }


        //***********
        tipsTextView = findViewById(R.id.textView_quote);
        setTime_textView = findViewById(R.id.textView_setTime);
        start_btn = findViewById(R.id.btn_start);
        plus15_btn = findViewById(R.id.btn_plus_15);
        minus15_btn = findViewById(R.id.btn_minus_15);
        plus1_btn = findViewById(R.id.btn_plus_1);
        minus1_btn = findViewById(R.id.btn_minus_1);
        todayDateTextView = findViewById(R.id.textView_todayDate);

        todoSpinnerView = findViewById(R.id.spinner);

//        Todo[] todos = {
//                new Todo("sleep"),
//                new Todo("work"),
//                new Todo("eat"),
//        };
//
//        Date date = new Date();
//        Session[] sessions = {
//                new Session(date.getTime()),
//                new Session(date.getTime()+86400000),
//                new Session(date.getTime()+86400000*2),
//                new Session(date.getTime()+86400000*3)
//
//        };
//        TodoSessionDao myDao = TodoSessionDatabase.getInstance(this).todoSessionDao();
//        for(Todo todo: todos){
//            myDao.insertTodo(todo);
//        }
//
//        for(Session session: sessions){
//            myDao.insertSession(session);
//        }
//
//        Log.d(TAG, "onCreate: "+myDao.getAllTodos());
//        Log.d(TAG, "onCreate: "+myDao.getAllSessions());



        db = TodoSessionDatabase.getInstance(this);
        dao = db.todoSessionDao();
        todoList.add(0,new Todo(0,"select any todo to focus on"));
        if(dao.getAllTodos()!=null){
            todoList.addAll(dao.getAllTodos());
            sessionList = dao.getAllSessions();
        }


        checkTodayDateInDatabase();

        setTodaysDateToTextView();

        todoSpinnerSetUp();


        start_btn.setOnClickListener(this);
        plus15_btn.setOnClickListener(this);
        minus15_btn.setOnClickListener(this);
        plus1_btn.setOnClickListener(this);
        minus1_btn.setOnClickListener(this);


        //********
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.focus);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.history:
                        Intent intent = new Intent(FocusActivity.this,HistoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.todo:
                        Intent intent2 = new Intent(FocusActivity.this,TodoActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                }
                return false;
            }
        });

        setRandomQuote();




    }

    private void setRandomQuote() {
        String[] quoteArray = {
                "Break big tasks into small chunks",
                "Don't Skip Your Breaks.",
                "Avoid Multitasking.",
                "Get enough sleep",
                "Drink water regularly",
                "Be more mindful",
                "Have a plan to help you stay on track."
        };
        String day = new SimpleDateFormat("EEEE").format(todayDate).toUpperCase();

        switch (day){
            case "MONDAY":tipsTextView.setText(quoteArray[0]);break;
            case "TUESDAY":tipsTextView.setText(quoteArray[1]);break;
            case "WEDNESDAY":tipsTextView.setText(quoteArray[2]);break;
            case "THURSDAY":tipsTextView.setText(quoteArray[3]);break;
            case "FRIDAY":tipsTextView.setText(quoteArray[4]);break;
            case "SATURDAY":tipsTextView.setText(quoteArray[5]);break;
            case "SUNDAY":tipsTextView.setText(quoteArray[6]);break;
        }


    }

    private void setTodaysDateToTextView() {
        String day = new SimpleDateFormat("EEEE").format(todayDate).toUpperCase();
        todayDateTextView.setText(String.format("%s",day));
    }

    private void checkTodayDateInDatabase() {
        todaysDateExactTime = todayDate.getTime();
        if(sessionList.size()!=0){
            int latestDateInDatabase = sessionList.size();
            Date lastDate = new Date(sessionList.get(latestDateInDatabase-1).getDate());
            int dateCompare = todayDate.compareTo(lastDate);

            SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
            String todayDateString = ft.format(todayDate);
            String lastDateString = ft.format(lastDate);
            int dateCompareStrings = todayDateString.compareTo(lastDateString);

            if(dateCompareStrings!=0){

                SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(getString(R.string.todaysDateExactTime),todaysDateExactTime);
                editor.apply();
                Session todaySession = new Session(todaysDateExactTime);
                dao.insertSession(todaySession);
//                Log.d(TAG, "checkTodayDateInDatabase: todays date newly added");
            }//                Log.d(TAG, "checkTodayDateInDatabase: "+"today day is already added to the database");


//            Log.d(TAG, "checkTodayDateInDatabase: "+dateCompare);
        }else{
            SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(getString(R.string.todaysDateExactTime),todaysDateExactTime);
            editor.apply();

            Session todaySession = new Session(todayDate.getTime());
            dao.insertSession(todaySession);
        }

    }


    private void todoSpinnerSetUp() {
            todoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,todoList);
            todoSpinnerView.setAdapter(todoArrayAdapter);
            todoSpinnerView.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_plus_15:
               int checkTime = setTime + 15;
                if (checkTime >= 240) {
                    setTime = 240;
                } else {
                    setTime += 15;
                }
                setTime_textView.setText(String.valueOf(setTime));
                break;

            case R.id.btn_minus_15:
                checkTime = setTime - 15;
                if (checkTime <= 1) {
                    setTime = 1;
                } else {
                    setTime -= 15;
                }
                setTime_textView.setText(String.valueOf(setTime));
                break;

            case R.id.btn_plus_1:
                checkTime = setTime + 1;
                if (checkTime >= 240) {
                    setTime = 240;
                } else {
                    setTime += 1;
                }
                setTime_textView.setText(String.valueOf(setTime));
                break;

            case R.id.btn_minus_1:
                checkTime = setTime - 1;
                if (checkTime <= 1) {
                    setTime = 1;
                } else {
                    setTime -= 1;
                }
                setTime_textView.setText(String.valueOf(setTime));
                break;
            case R.id.btn_start:
                saveAlarmStartDate();
                startBroadCast();



        }
    }

    private void saveAlarmStartDate() {
        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Date date = new Date();
        editor.putLong(getString(R.string.alarmStartDate),date.getTime());
        editor.apply();
    }

    private void startBroadCast() {

        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        Log.d(TAG, "startBroadCast: "+setTime);
        alarmTimeInMillis = System.currentTimeMillis()+(setTime*60*1000);
        editor.putBoolean(getString(R.string.isAlarmSet),true);
        editor.putLong("alarmSetTime",alarmTimeInMillis);
        editor.apply();
        startTimer(setTime*60*1000);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AlertReceiver.class);
        intent.putExtra("check",1);
//        Log.d(TAG, "startBroadCast: --------setTime----"+setTime);
        intent.putExtra("alarmTime",setTime);

//                Log.d(TAG, "onClick: "+System.currentTimeMillis());
//                intent.putExtra("alarm","time "+System.currentTimeMillis());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),1,intent,0);

//        Log.d(TAG, "startBroadCast: "+alarmTimeInMillis);

        if(Build.VERSION.SDK_INT < 23){
            if(Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTimeInMillis,pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP,alarmTimeInMillis,pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmTimeInMillis    ,pendingIntent);
        }
    }

    private void startTimer(long setTime) {

        Intent intent = new Intent(this,ShowTimerActivity.class);
        intent.putExtra("time",setTime);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedTodoId = adapterView.getSelectedItemPosition();
//        Log.d(TAG, "onItemSelected: "+todoList.get(selectedTodoId));

        SharedPreferences sharedPref = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.spinnerSelectedTodoId),todoList.get(selectedTodoId).getTodo_id());
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}