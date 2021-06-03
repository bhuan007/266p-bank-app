package com.example.bank;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "users",
indices = {@Index(value = {"userName"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userName;
    private String password;
    private Double balance;
    private Integer loginFailedAttempts;
    private long lastLoginTime;


    public User(String userName, String password, Double balance, Integer loginFailedAttempts, long lastLoginTime) {
        this.userName = userName;
        this.password = password;
        this.balance = balance;
        this.loginFailedAttempts = loginFailedAttempts;
        this.lastLoginTime = lastLoginTime;
    }

    @Ignore
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Ignore
    public User(int id, String userName, String password, Double balance) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLoginFailedAttempts() {
        return loginFailedAttempts;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void resetLoginFailedAttempts() {
        this.loginFailedAttempts = 0;
    }

    public void incrementLoginFailedAttempts() {
        this.loginFailedAttempts++;
    }
    public void setLastLoginTime(long LastLoginTime) {
        this.lastLoginTime = LastLoginTime;
    }

}
