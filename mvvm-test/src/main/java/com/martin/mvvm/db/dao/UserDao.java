package com.martin.mvvm.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.martin.mvvm.db.bean.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user ORDER BY name ASC")
    LiveData<List<User>> getAllUsers();

    @Update
    void updateUser(User user);

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);
}
