package com.martin.mvvm.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.martin.mvvm.db.AppDatabase;
import com.martin.mvvm.db.bean.User;
import com.martin.mvvm.db.dao.UserDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final LiveData<List<User>> userList;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        userList = userDao.getAllUsers();
    }

    public void insert(final User user) {
        executor.execute(() -> userDao.insertUser(user));
    }

    public LiveData<List<User>> getAllUsers() {
        return userList;
    }

    public void delete(final User user) {
        executor.execute(() -> userDao.deleteUser(user));
    }

    public void update(final User user) {
        executor.execute(() -> userDao.updateUser(user));
    }

    public void shutdown() {
        if (executor instanceof ExecutorService) {
            ExecutorService es = (ExecutorService) executor;
            es.shutdown();
        }
    }
}
