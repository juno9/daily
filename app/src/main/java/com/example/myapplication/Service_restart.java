package com.example.myapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class Service_restart extends Service {

    public Service_restart() {
        Log.i("[Service_restart]", "생성자");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("[Service_restart]", "온크리에이트");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("[Service_restart]", "온디스트로이");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("[Service_restart]", "onStartCommand");

        // Notification 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Service Running")
                .setContentText("Restarting services...")
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Service Channel", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        startForeground(9, notification);

        // Service_chat 시작

        startService(new Intent(getApplicationContext(), Service_chat.class));

        // stopForeground와 stopSelf 호출 순서 조정
        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}

