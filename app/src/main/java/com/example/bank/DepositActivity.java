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

public class DepositActivity extends AppCompatActivity {

    private UserDatabase db;
    private TextView txtBalance;
    private EditText etDeposit;
    private Button btnDeposit;
    private User user;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        initViews();
        db = UserDatabase.getInstance(this);
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);

        if (userId > -1) {
            user = db.userDao().selectSingleUserById(userId);
            txtBalance.setText(currencyFormat.format(user.getBalance()));
        }

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double depositAmt = Double.parseDouble(etDeposit.getText().toString());

                if (etDeposit.getText().toString().startsWith("0") && depositAmt >= 1) {
                    Toast.makeText(DepositActivity.this, "invalid_input", Toast.LENGTH_SHORT).show();
                }
                else if (depositAmt > 4294967295.99){
                    Toast.makeText(DepositActivity.this, "invalid_input", Toast.LENGTH_SHORT).show();
                }
                else if (etDeposit.getText().toString().contains(".")
                        && (etDeposit.getText().toString().length() - etDeposit.getText().toString().indexOf('.') - 1) > 2) {
                    Toast.makeText(DepositActivity.this, "invalid_input", Toast.LENGTH_SHORT).show();
                }

                else{
                    Double updatedBalance = user.getBalance() + depositAmt;
                    updatedBalance = Math.round(updatedBalance * 100)/100d;
                    user.setBalance(updatedBalance);
                    db.userDao().updateSingleUser(user);
                    txtBalance.setText(currencyFormat.format(user.getBalance()));
                    etDeposit.setText("");
                    Toast.makeText(DepositActivity.this, "Deposit Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        txtBalance = findViewById(R.id.txtBalance);
        etDeposit = findViewById(R.id.etDeposit);
        btnDeposit = findViewById(R.id.btnDeposit);

    }
}