package com.example.bank;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String password;
    private Double balance;


    public User(String userName, String password, Double balance) {
        this.userName = userName;
        this.password = password;
        this.balance = balance;
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
}