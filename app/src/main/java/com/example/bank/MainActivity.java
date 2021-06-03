package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private TextView txtMessage;
    private Button btnLogin, btnSignUp;
    private EditText etUserName, etPassword, etInitialBalance;
    private UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        db = UserDatabase.getInstance(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                long currentTimeInMillis = calendar.getTimeInMillis();
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (userName.equals("") || password.equals("")) {
                    txtMessage.setText("The username/password field is empty.");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                    return;
                }

                User userNameCheck = db.userDao().selectSingleUserByName(userName);

                User loginCheck = db.userDao().selectSingleUser(userName, password);

                // User name does not exist
                if (userNameCheck == null) {
                    txtMessage.setText("Sorry! One or more of your login fields is incorrect.");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                }

                // Username exists
                else {

                    //Reset the login attempts if the user wait for enough time(30 min)
                    if (currentTimeInMillis - userNameCheck.getLastLoginTime() >= 1800000) {
                        userNameCheck.resetLoginFailedAttempts();
                        db.userDao().updateSingleUser(userNameCheck);
                    }

                    //Check if the user has tried more than 5 times
                    if (userNameCheck.getLoginFailedAttempts() >= 5) {
                        txtMessage.setText("You have already made too many attempts, you still need to wait for" + (currentTimeInMillis - userNameCheck.getLastLoginTime()) / 60000 + " minutes!");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);
                    }

                    // Correct user but wrong password
                    else if (loginCheck == null) {
                        userNameCheck.setLastLoginTime(currentTimeInMillis);
                        userNameCheck.incrementLoginFailedAttempts();
                        db.userDao().updateSingleUser(userNameCheck);
                        txtMessage.setText("Wrong password!You have tried " + userNameCheck.getLoginFailedAttempts() + " times!");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);

                        txtMessage.setText("Username or password is incorrect.");

                        //Reset the login attempts if the user wait for enough time(30 min)
                        if (currentTimeInMillis - userNameCheck.getLastLoginTime() >= 1800000) {
                            userNameCheck.resetLoginFailedAttempts();
                        }

                        //Check if the user has tried more than 5 times
                        if (userNameCheck.getLoginFailedAttempts() >= 5) {
                            txtMessage.setText("You have already made too many attempts, you still need to wait for" + (currentTimeInMillis - userNameCheck.getLastLoginTime()) / 60000 + " minutes!");
                            txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                            txtMessage.setVisibility(View.VISIBLE);
                        } else  {
                            userNameCheck.setLastLoginTime(currentTimeInMillis);
                            txtMessage.setText("Wrong password!You have tried " + userNameCheck.getLoginFailedAttempts() + " times!");
                            txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                            txtMessage.setVisibility(View.VISIBLE);
                        }

                    }
                    // Successful Login
                    else {


                        // Reset the LoginFailedAttempts to 0 and LastLoginTime
                        userNameCheck.setLastLoginTime(currentTimeInMillis);
                        userNameCheck.resetLoginFailedAttempts();
                        db.userDao().updateSingleUser(userNameCheck);
                        Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userId", loginCheck.getId());

                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                Double initialBalance = null;
                if(!etInitialBalance.getText().toString().equals("") && !etInitialBalance.getText().toString().equals(".")) initialBalance = Double.parseDouble(etInitialBalance.getText().toString());



                if (userName == null || password == null || initialBalance == null) {
                    txtMessage.setText("All fields must be complete for signup.");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                }

                else if (!userName.matches("[_\\-\\.0-9a-z]{1,127}")
                        || !password.matches("[_\\-\\.0-9a-z]{1,127}")) {
                    txtMessage.setText("Your user name and password must be between 1 - 127 characters long and could only include underscores, hyphens, dots, digits, and lowercase alphabetical characters.");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "invalid_input", Toast.LENGTH_SHORT).show();
                }

                else if (etInitialBalance.getText().toString().startsWith("0") && initialBalance >= 1) {
                }

                else if (etInitialBalance.getText().toString().contains(".")
                        && (etInitialBalance.getText().toString().length() - etInitialBalance.getText().toString().indexOf('.') - 1) > 2) {
                    txtMessage.setText("The initial balance can only have a maximum of 2 decimal points.");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                    return;
                }
                else {
                    User userCheck = db.userDao().selectSingleUserByName(userName);

                    if (userCheck == null) {
                        User user = new User(userName, password);
                        user.setBalance(initialBalance);
                        db.userDao().insertSingleUser(user);
                        txtMessage.setText("Sign up successful!");
                        txtMessage.setTextColor(getResources().getColor(R.color.positiveGreen));
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        txtMessage.setText("User already exists.");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

    }


    private void initViews() {
        txtMessage = findViewById(R.id.txtMessage);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etInitialBalance = findViewById(R.id.etInitialBalance);
    }
}

