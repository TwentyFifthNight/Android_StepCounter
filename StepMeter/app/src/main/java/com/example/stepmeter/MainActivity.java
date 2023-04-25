package com.example.stepmeter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager = null;
    private boolean running = false;
    private float previousTotalSteps = 0;
    private float totalSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        FloatingActionButton FAB = findViewById(R.id.floatingActionButton);

        FAB.setOnClickListener(view -> {
            final Intent intent;
            intent = new Intent(this, ListActivity.class);
            this.startActivity(intent);
        });

        DataAlarm alarm = new DataAlarm();
        alarm.setAlarm(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepSensor == null){
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        }else{
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        TextView stepsTaken = findViewById(R.id.tv_stepsTaken);

        if(running){
            totalSteps = sensorEvent.values[0];
            loadData();
            int currentSteps = Math.round (totalSteps - previousTotalSteps);
            String output = "" + currentSteps;
            stepsTaken.setText(output);
        }
    }
/*
    public void resetSteps(){
        //ImageView circle = findViewById(R.id.stepsCircle);
        TextView stepsTaken = findViewById(R.id.tv_stepsTaken);

        stepsTaken.setText("0");
        previousTotalSteps = totalSteps;
        saveData();

        circle.setOnClickListener(view -> Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show());

        circle.setOnLongClickListener(view -> {
            stepsTaken.setText("0");
            previousTotalSteps = totalSteps;
            saveData();
            return true;
        });
    }*/

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("prevTotalSteps",previousTotalSteps);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getFloat("prevTotalSteps",0f);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private long calculateFlex(int hourOfTheDay, int periodInDays) {

        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        if (cal2.getTimeInMillis() < cal1.getTimeInMillis()) {
            // Add the worker periodicity.
            cal2.setTimeInMillis(cal2.getTimeInMillis() + TimeUnit.DAYS.toMillis(periodInDays));
        }

        long delta = (cal2.getTimeInMillis() - cal1.getTimeInMillis());

        return (Math.max(delta, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS));
    }
}