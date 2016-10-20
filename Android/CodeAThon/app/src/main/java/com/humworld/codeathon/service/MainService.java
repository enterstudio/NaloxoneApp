package com.humworld.codeathon.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.humworld.codeathon.receivers.MainReceiver;


/**
 * Created by Sys-3 on 10/14/2016.
 * Company Name Humworld
 */

public class MainService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyService > ", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("MyService > ", "onCreate");
        PendingIntent intervalPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent(this, MainReceiver.class), 0);
        AlarmManager intervalAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intervalAlarmManager.setRepeating(2, SystemClock.elapsedRealtime(), 60 * 1000, intervalPendingIntent);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("MyService > ", "onDestroy");
        super.onDestroy();
    }
}
