package com.example.demo.services;

import android.app.Service;
import android.car.Car;
import android.car.hardware.property.CarPropertyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class VcPropertyService extends Service {

    private static final String TAG = "VcPropertyService";
    private final IBinder binder = new VcPropertyService.LocalBinder();
    private CarPropertyManager carPropertyManager;
    private boolean isCarServiceConnected = false;
    private Car car;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("VcPropertyService", "onCreate: VcPropertyService creating");
        setUp(this);
        Log.d("VcPropertyService", "onCreate: VcPropertyService create done");
    }

    private void setUp(Context context) {
        try {
            connectCarService(context);
        } catch (Exception e) {
            Log.e(TAG, "setUp failed, " + e.getMessage());
        }
    }

    public class LocalBinder extends Binder {
        public VcPropertyService getService() {
            return VcPropertyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Get Car instance.
     * @return Car instance, or null if not connected
     */
    public Car getCar() {
        return car;
    }

    /**
     * Get CarPropertyManager instance.
     * @return CarPropertyManager instance, or null if not connected
     */
    public CarPropertyManager getCarPropertyManager() {
        return carPropertyManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCarServiceConnected = false;
    }

    /**
     * Check if CarService is connected and CarPropertyManager is available.
     * @return true if CarService is connected and CarPropertyManager is available, false otherwise
     */
    public boolean isCarServiceConnected() {
        return isCarServiceConnected && car != null && carPropertyManager != null;
    }

    private void connectCarService(Context context) throws Exception {
        if (context == null) {
            Log.e(TAG, "connectCarService: context is null");
            throw new IllegalStateException("Context is null, cannot connect to CarService");
        }

        Log.i(TAG, "connectCarService: Connecting to CarService");

        final CountDownLatch connectionLatch = new CountDownLatch(1);
        final AtomicBoolean serviceConnected = new AtomicBoolean(false);

        Handler handler = new Handler(getMainLooper());
        Car.CarServiceLifecycleListener lifecycleListener = (car, ready) -> {
            if (ready) {
                try {
                    if (car == null) {
                        Log.w(TAG, "onLifecycleChanged: Car instance is null");
                        return;
                    }
                    carPropertyManager = (CarPropertyManager) car.getCarManager(Car.PROPERTY_SERVICE);
                    serviceConnected.set(carPropertyManager != null);
                    if (serviceConnected.get() && carPropertyManager != null) {
                        isCarServiceConnected = true;
                        Log.i(TAG, "onLifecycleChanged: CarPropertyManager is ready");
                    } else {
                        Log.e(TAG, "onLifecycleChanged: CarPropertyManager is null");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onLifecycleChanged: Failed to connect, " + e.getMessage());
                    isCarServiceConnected = false;
                } finally {
                    connectionLatch.countDown();
                }
            }
        };

        car = Car.createCar(context, handler, Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER, lifecycleListener);

        if (car == null) {
            throw new IllegalStateException("Failed to create Car instance");
        }

        try {
            if (!connectionLatch.await(2, java.util.concurrent.TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timed out waiting for CarService connection");
            }

            if (!serviceConnected.get()) {
                throw new IllegalStateException("Failed to connect to CarService");
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to connect to Car service: " + e.getMessage());
            try {
                if (car != null) {
                    car.disconnect();
                }
            } catch (Exception ex) {
                // Ignore disconnect errors
            } finally {
                car = null;
                carPropertyManager = null;
                isCarServiceConnected = false;
            }
        }
    }
}
