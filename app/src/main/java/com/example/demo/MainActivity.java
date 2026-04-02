package com.example.demo;

import android.car.Car;
import android.car.hardware.property.CarPropertyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.services.MyService;
import com.example.demo.services.VcPropertyService;

public class MainActivity extends AppCompatActivity {

    private static final String NO_TIME = "No Time";
    private Car mCar;
    private MyService service;
    private VcPropertyService vcPropertyService;
    private boolean isVcServiceBound = false;
    private boolean isServiceBound = false;

    private final ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.LocalBinder binder = (MyService.LocalBinder) iBinder;
            service = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
            isServiceBound = false;
        }
    };

    private final ServiceConnection vcPropertyServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            VcPropertyService.LocalBinder binder = (VcPropertyService.LocalBinder) iBinder;
            vcPropertyService = binder.getService();
            isVcServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            vcPropertyService = null;
            isVcServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStartService.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyService.class);
            // bindService(intent, myServiceConnection, BIND_AUTO_CREATE);
        });

        binding.btnStopService.setOnClickListener(v -> {
            if (isServiceBound) {
                unbindService(myServiceConnection);
                isServiceBound = false;
                service = null;
            }
            if (isVcServiceBound) {
                unbindService(vcPropertyServiceConnection);
                isVcServiceBound = false;
                vcPropertyService = null;
            }
            binding.tvTimeDisplay.setText(NO_TIME);
        });

        binding.btnStartVcService.setOnClickListener(v -> {
            startCarService();
            if (vcPropertyService != null) {
                CarPropertyManager manager = vcPropertyService.getCarPropertyManager();
            } else {
                Log.w("MainActivity", "VcPropertyService is null after bindService");
            }
        });

        binding.btnGetCar.setOnClickListener(v -> {
            if (service != null) {
                try {
                    mCar = service.getCarInstance();
                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to get Car instance: " + e.getMessage());
                }
            } else {
                Log.w("MainActivity", "Service is null when trying to get Car instance");
            }
        });

        binding.btnCarManager.setOnClickListener(view -> {
            if (mCar != null) {
                try {
                    CarPropertyManager carPropertyManager = (CarPropertyManager) mCar
                            .getCarManager(Car.PROPERTY_SERVICE);
                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to get CarPropertyManager instance: "
                            + e.getMessage());
                }
            } else {
                Log.w("MainActivity", "Car is null when get CarPropertyManager instance");
            }
        });

        binding.btnShowTime.setOnClickListener(v -> {
            if (service != null) {
                String currentTime = service.getCurrentTime();
                binding.tvTimeDisplay.setText(currentTime);
            } else {
                Log.w("MainActivity", "Service is null when get current time");
            }
        });
    }

    private void startCarService() {
        Intent intent = new Intent(MainActivity.this, VcPropertyService.class);
        bindService(intent, vcPropertyServiceConnection, BIND_AUTO_CREATE);
    }
}
