package com.example.stepmeter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepmeter.dbclasses.DataAlarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


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

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                int ACTIVITY_RECOGNITION_CODE = 1;
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.ACTIVITY_RECOGNITION
                }, ACTIVITY_RECOGNITION_CODE);
            }
        }

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

        if(totalSteps != 0){
            loadData();
            TextView stepsTaken = findViewById(R.id.tv_stepsTaken);
            int currentSteps = Math.round (totalSteps - previousTotalSteps);
            String output = Integer.toString(currentSteps);
            stepsTaken.setText(output);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("Sensor","Changed");
        if(running){
            TextView stepsTaken = findViewById(R.id.tv_stepsTaken);
            totalSteps = sensorEvent.values[0];
            loadData();
            int currentSteps = Math.round (totalSteps - previousTotalSteps);
            String output = Integer.toString(currentSteps);
            stepsTaken.setText(output);
        }
    }

    private void loadData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_MULTI_PROCESS);
        previousTotalSteps = sharedPreferences.getFloat("prevTotalSteps",0f);
        Log.d("loadData","loaded");
        Log.d("loadData",Float.toString(previousTotalSteps));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}