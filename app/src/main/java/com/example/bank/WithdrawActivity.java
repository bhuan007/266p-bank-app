package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class WithdrawActivity extends AppCompatActivity {
    private UserDatabase db;
    private TextView txtBalance;
    private EditText etWithdraw;
    private Button btnWithdraw;
    private User user;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initViews();
        db = UserDatabase.getInstance(this);
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);

        if (userId > -1) {
            user = db.userDao().selectSingleUserById(userId);
            txtBalance.setText(currencyFormat.format(user.getBalance()));
        }

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double currentBal = user.getBalance();
                Double withdrawAmt = Double.parseDouble(etWithdraw.getText().toString());
                withdrawAmt = Math.round(withdrawAmt * 100) / 100d;

                if (currentBal >= withdrawAmt) {
                    Double updatedBalance = currentBal - withdrawAmt;
                    user.setBalance(updatedBalance);
                    db.userDao().updateSingleUser(user);
                    txtBalance.setText(currencyFormat.format(user.getBalance()));
                    etWithdraw.setText("");
                }
                else {
                    Toast.makeText(WithdrawActivity.this, "Cannot withdraw more than balance", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initViews() {
        txtBalance = findViewById(R.id.txtBalance);
        etWithdraw = findViewById(R.id.etWithdraw);
        btnWithdraw = findViewById(R.id.btnWithdraw);
    }
}