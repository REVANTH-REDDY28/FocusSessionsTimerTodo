package com.sunny.focussessions_simpletimertodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDao;
import com.sunny.focussessions_simpletimertodo.data.TodoSessionDatabase;
import com.sunny.focussessions_simpletimertodo.data.entities.Session;
import com.sunny.focussessions_simpletimertodo.data.entities.Todo;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    BottomNavigationView bottomNavigationView;

    TodoSessionDatabase todoSessionDatabase;
    TodoSessionDao todoSessionDao;
    List<Session> dataList;
    RecyclerView recyclerView;
    SessionAdapter sessionAdapter;
    Button resetButton;
    Activity activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        resetButton = findViewById(R.id.button_reset_sessions);
        //for todo session view
        todoSessionDatabase = TodoSessionDatabase.getInstance(this);
        todoSessionDao = todoSessionDatabase.todoSessionDao();
        dataList = todoSessionDao.getAllSessions();
//        Log.d(TAG, "onCreate: "+dataList.size());
        recyclerView = findViewById(R.id.recyclerView_sessions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        sessionAdapter = new SessionAdapter(this,dataList);
        recyclerView.setAdapter(sessionAdapter);
        activityContext = this;

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(HistoryActivity.this,"available in next update",Toast.LENGTH_SHORT).show();
                showWarningDialog();
//                todoSessionDao.deleteAllSessions();
            }
        });



        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.focus:
                        Intent intent = new Intent(HistoryActivity.this,FocusActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.todo:
                        Intent intent2 = new Intent(HistoryActivity.this,TodoActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                }
                return false;
            }
        });
    }

    private void showWarningDialog() {
        View view = LayoutInflater.from(activityContext).inflate(R.layout.pop_up_warn_delete_sessions,null);
        Button cancelBtn = view.findViewById(R.id.button_cancel_popUp);
        Button okBtn = view.findViewById(R.id.button_ok_popUp);


        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoSessionDao.deleteAllSessions();
                sessionAdapter.notifyItemRangeRemoved(0,dataList.size());
                dialog.dismiss();
                Toast.makeText(activityContext,"cleared sucessfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
}