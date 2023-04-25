package com.example.stepmeter;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication sInstance;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        sInstance = this;
    }

    public static Context getAppContext(){
        return MyApplication.context;
    }

    public static MyApplication getInstance(){
        return MyApplication.sInstance;
    }
}
