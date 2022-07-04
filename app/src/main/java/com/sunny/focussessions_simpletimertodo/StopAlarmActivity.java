package com.sunny.focussessions_simpletimertodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StopAlarmActivity extends AppCompatActivity {
//    private static final String TAG = "StopAlarmActivity";
    Button stopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
//        Log.d(TAG, "onCreate: "+1);
        stopButton = findViewById(R.id.button_stop);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(StopAlarmActivity.this,RingService.class);
                stopService(serviceIntent);
                finish();
            }
        });
    }
}