package com.example.stepmeter;

import android.app.Application;

public class MyApplication extends Application {

    public void onCreate(){
        super.onCreate();
    }

    public MyApplication getInstance(){
        return MyApplication.this;
    }
}
