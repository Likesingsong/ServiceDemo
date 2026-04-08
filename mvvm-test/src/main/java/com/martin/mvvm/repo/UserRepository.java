package com.martin.mvvm.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.martin.mvvm.db.AppDatabase;
import com.martin.mvvm.db.bean.User;
import com.martin.mvvm.db.dao.UserDao;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> userList;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        userList = userDao.getAllUsers();
    }

    public void insert(final User user) {
        new Thread(() -> {
            userDao.insertUser(user);
        }).start();
    }

    public LiveData<List<User>> getAllUsers() {
        return userList;
    }

    public void delete(final User user) {
        new Thread(() -> {
            userDao.deleteUser(user);
        }).start();
    }

    public void update(final User user) {
        new Thread(() -> {
            userDao.updateUser(user);
        }).start();
    }
}
