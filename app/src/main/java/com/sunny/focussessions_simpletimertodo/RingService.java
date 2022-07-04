package com.sunny.focussessions_simpletimertodo;

import  static com.sunny.focussessions_simpletimertodo.App.CHANNEL_1_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class RingService extends Service {
//    private static final String TAG = "RingService";
    private static final String ACTION_STOP = "stop";
    private static final String ACTION_START_FOREGROUND_SERVICE = "startService";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this,R.raw.handouts_music);
        mediaPlayer.setLooping(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!=null){
            String action = intent.getAction();

            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
//                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_STOP:
                    stopForegroundService();
//                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();

            }
        }
        return START_NOT_STICKY;

    }

    private void stopForegroundService() {

//        Log.d(TAG, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    private void startForegroundService() {
//        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onStartCommand: "+"11");
        Intent notificationIntent = new Intent(this,StopAlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_1_ID);

        // Add Stop button intent in notification.
        Intent stopIntent = new Intent(this, RingService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        NotificationCompat.Action stopAction = new NotificationCompat.Action(android.R.drawable.ic_menu_close_clear_cancel, "Stop", pendingStopIntent);
        builder.addAction(stopAction);

        Notification notification = builder
                .setContentTitle("time completed")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentIntent(pendingIntent)
                .build();




//        Log.d(TAG, "onStartCommand: "+"12");


        startForeground(1,notification);
//        Log.d(TAG, "onStartCommand: "+"13");
        mediaPlayer.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
