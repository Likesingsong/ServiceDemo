package com.martin.mvvm.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.martin.mvvm.base.BaseViewModel;
import com.martin.mvvm.db.bean.User;
import com.martin.mvvm.repo.UserRepository;

import java.util.List;

public class UserViewModel extends BaseViewModel {
    private final UserRepository repository;
    private final LiveData<List<User>> users;
    private final MutableLiveData<Boolean> showAddUserDialog = new MutableLiveData<>();

    public LiveData<Boolean> getShowAddUserDialog() {
        return showAddUserDialog;
    }

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        users = repository.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void insert(User user) {
        repository.insert(user);
        showAddUserDialog.setValue(false);
    }

    public void cancelInsert() {
        if (Boolean.TRUE.equals(showAddUserDialog.getValue())) {
            showAddUserDialog.setValue(false);
        }
    }

    public void insertUser() {
        showAddUserDialog.setValue(true);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}
