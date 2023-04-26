package com.example.stepmeter.dbclasses;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.stepmeter.MyApplication;
import com.example.stepmeter.StepsCountViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SaveData extends Thread implements SensorEventListener {

    private float previousTotalSteps = 0;
    private float newTotalSteps = 0;
    private volatile boolean running = false;

    private final Context context;
    StepsCountViewModel mStepCountViewModel;

    public SaveData(Context context){
        this.context = context;
    }

    public void run() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        running = true;

        if(stepSensor != null){
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
            while (running);

            MyApplication myApplication = new MyApplication();
            mStepCountViewModel = new StepsCountViewModel(myApplication.getInstance());
            loadData();
            int save = Math.round(newTotalSteps - previousTotalSteps);

            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

            mStepCountViewModel.insert(new StepsCount(format.format(cal1.getTime()), save+""));
            saveDataToPref(context);
            Log.d("Alarm","Success");
            return;
        }
        Log.d("Alarm","Failure");
    }

    private void loadData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getFloat("prevDaily",0f);
    }

    private void saveDataToPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("prevDaily",newTotalSteps);
        editor.putFloat("prevTotalSteps",newTotalSteps);
        editor.apply();
        Log.d("Alarm","preferences");
        Log.d("Alarm",Float.toString(newTotalSteps));
        Log.d("Alarm",Float.toString(sharedPreferences.getFloat("prevTotalSteps",0f)));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(running){
            newTotalSteps = sensorEvent.values[0];
            running = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
