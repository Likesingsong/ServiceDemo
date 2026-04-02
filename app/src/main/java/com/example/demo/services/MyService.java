package com.example.demo.services;

import android.app.Service;
import android.car.Car;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {
    private final IBinder binder = new LocalBinder();

    private Car car;

    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try {
                // Simulate some background work
                Log.i("BackService", "Background Work Started");
            } catch (Exception e) {
                Log.e("BackService", "Error in background work", e);
            }
            Log.d("BackService", "Background Work Completed");
            stopSelf();
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getCurrentTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "current time: " + sdf.format(now);
    }

    public Car getCarInstance() {
        if (car == null) {
            car = Car.createCar(this);
        }
        return car;
    }
}
