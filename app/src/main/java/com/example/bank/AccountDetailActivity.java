package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Statement;
import java.text.NumberFormat;

public class AccountDetailActivity extends AppCompatActivity {

    private UserDatabase db;
    private User user;
    private TextView txtWelcomeUser, txtBalance;
    private Button btnWithdraw, btnDeposit, btnLogos, btnLogout;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private int userId;

    private static final String TAG = "AccountDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        initViews();

        updateAccountDetails();


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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                AccountDetailActivity.this.finish();
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
            Log.d(TAG, "user balance is " + user.getBalance());
            txtBalance.setText(currencyFormat.format(user.getBalance()));
        }
    }

    private void initViews() {
        txtWelcomeUser = findViewById(R.id.txtWelcomeUser);
        txtBalance = findViewById(R.id.txtBalance);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnLogos = findViewById(R.id.btnLogos);
        btnLogout = findViewById(R.id.btnLogout);
    }
}