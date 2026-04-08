package com.martin.mvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;

public class BaseViewModel extends AndroidViewModel implements LifecycleObserver {
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
