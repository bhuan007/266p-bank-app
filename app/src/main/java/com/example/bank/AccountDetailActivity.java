package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Statement;
import java.text.NumberFormat;

public class AccountDetailActivity extends AppCompatActivity {

    private UserDatabase db;
    private User user;
    private TextView txtWelcomeUser, txtBalance;
    private Button btnStatement, btnWithdraw, btnDeposit, btnLogos;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        initViews();

        updateAccountDetails();

        btnStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailActivity.this, StatementActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailActivity.this, WithdrawActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailActivity.this, DepositActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnLogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailActivity.this, BankLogoActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateAccountDetails();
    }

    private void updateAccountDetails() {
        db = UserDatabase.getInstance(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",-1);

        if (userId > -1) {
            user = db.userDao().selectSingleUserById(userId);
            txtWelcomeUser.setText("Welcome " + user.getUserName());
            txtBalance.setText(currencyFormat.format(user.getBalance()));
        }
    }

    private void initViews() {
        txtWelcomeUser = findViewById(R.id.txtWelcomeUser);
        txtBalance = findViewById(R.id.txtBalance);
        btnStatement = findViewById(R.id.btnStatement);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnLogos = findViewById(R.id.btnLogos);
    }
}