package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Receiver_reboot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("[RebootReceiver]","온 리시브");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                context.startForegroundService(new Intent(context, Service_restart.class));
                Log.i("Receiver_reboot","빌드버전 O이상 Service_restart 시작함");
            }
        } else {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                context.startService(new Intent(context, Service_restart.class));
                Log.i("Receiver_reboot","빌드버전 O이하 Service_restart 시작함");
            }
        }
    }

}