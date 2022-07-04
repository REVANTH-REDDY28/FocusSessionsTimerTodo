package com.sunny.focussessions_simpletimertodo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {
//    private static final String TAG = "LoliApp";
    public static final String CHANNEL_1_ID = "FocusSessionsSimple1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
//        Log.d(TAG, "createNotificationChannels: "+"1");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            Log.d(TAG, "createNotificationChannels: "+"2");
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
//            Log.d(TAG, "createNotificationChannels: "+"3");
            manager.createNotificationChannel(channel1);
//            Log.d(TAG, "createNotificationChannels: "+"4");

        }
    }
}
