package com.martin.mvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.martin.mvvm.base.BaseViewModel;
import com.martin.mvvm.db.bean.User;
import com.martin.mvvm.repo.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<User>> users;
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        users = repository.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void update(User user) {
        repository.update(user);
    }
}
