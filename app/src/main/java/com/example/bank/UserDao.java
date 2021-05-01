package com.example.bank;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    void insertSingleUser(User user);

    @Update
    void updateSingleUser(User user);

    @Delete
    void deleteSingleUser(User user);

    @Query("SELECT * FROM users WHERE userName=:userName AND password=:password")
    User selectSingleUser(String userName, String password);

    @Query("SELECT * FROM users WHERE userName=:userName")
    User selectSingleUserByName(String userName);

    @Query("SELECT * FROM users WHERE id=:id")
    User selectSingleUserById(int id);
}
