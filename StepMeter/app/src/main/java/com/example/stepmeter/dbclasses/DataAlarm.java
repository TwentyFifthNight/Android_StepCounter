package com.example.stepmeter.dbclasses;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class DataAlarm extends BroadcastReceiver {

    private static boolean instance = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        SaveData thread = new SaveData(context);
        thread.start();
    }

    public void setAlarm(Context context)
    {
        if(!instance) {
            instance = true;
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, DataAlarm.class);
            Log.d("Alarm", "Before");
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

            Calendar cal = Calendar.getInstance();

            if(cal.get(Calendar.HOUR_OF_DAY) != 23 || cal.get(Calendar.MINUTE) < 59){
                PendingIntent pending = PendingIntent.getBroadcast(context, 1, i, PendingIntent.FLAG_IMMUTABLE);
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pending);
            }

            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.DAY_OF_YEAR,1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);


            am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60 * 60 * 24, pi); // Millisec * Second * Minute
            Log.d("Alarm", "After");
        }
    }

    public void cancelAlarm(Context context)
    {
        instance = false;
        Intent intent = new Intent(context, DataAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
