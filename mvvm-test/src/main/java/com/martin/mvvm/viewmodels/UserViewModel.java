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
    private final MutableLiveData<String> name = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");

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
    }

    public void insertUser() {
        if (name.getValue() != null && !name.getValue().isEmpty()
                && password.getValue() != null && !password.getValue().isEmpty() ) {
            User user = new User();
            user.setName(name.getValue());
            user.setPassword(password.getValue());
            repository.insert(user);
            name.postValue("");
            password.postValue("");
        }
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}
