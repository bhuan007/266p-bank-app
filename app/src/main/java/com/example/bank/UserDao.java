package com.example.bank;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

public interface UserDao {

    @Insert
    void insertSingleUser(User user);

    @Update
    void updateSingleUser(User user);

    @Delete
    void deleteSingleUser(User user);

    @Query("SELECT * FROM users WHERE userName=:userName AND password=:password")
    User selectSingleUser(String userName, String password);
}
