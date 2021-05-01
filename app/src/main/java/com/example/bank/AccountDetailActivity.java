package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AccountDetailActivity extends AppCompatActivity {

    private UserDatabase db;
    private User user;
    private TextView txtWelcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        initViews();

        db = UserDatabase.getInstance(this);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId",-1);

        if (userId > -1) {
            user = db.userDao().selectSingleUserById(userId);
            txtWelcomeUser.setText("Welcome " + user.getUserName());
        }
    }

    private void initViews() {
        txtWelcomeUser = findViewById(R.id.txtWelcomeUser);
    }
}