package com.example.myapplication;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Receiver_Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("[AlarmReceiver]", "onReceive 호출됨");

        Intent in = new Intent(context, Service_restart.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i("[AlarmReceiver]", "Android 12(S) 이상에서 서비스 실행");
            // Android 12 이상
            if (isAppForeground(context)) {
                // 앱이 포어그라운드 상태일 때만 실행
                context.startForegroundService(in);
                Log.i("[AlarmReceiver]", "포어그라운드 상태에서 Service_restart 실행");
            } else {
                Log.i("[AlarmReceiver]", "앱이 백그라운드 상태이므로 서비스 실행 생략");
                // 필요시 사용자에게 알림을 띄우거나 WorkManager로 작업 예약
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("[AlarmReceiver]", "Android O 이상에서 포어그라운드 서비스 실행");
            // Android Oreo 이상
            context.startForegroundService(in);
        } else {
            Log.i("[AlarmReceiver]", "Android O 미만에서 일반 서비스 실행");
            // Android Oreo 미만
            context.startService(in);
        }
    }

    /**
     * 앱이 포어그라운드 상태인지 확인
     */
    private boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && processInfo.processName.equals(context.getPackageName())) {
                    return true;
                }
            }
        } else {
            // Android Q 이전 버전
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return processInfo.processName.equals(context.getPackageName());
                }
            }
        }

        return false;
    }
}
