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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private TextView txtMessage;
    private Button btnLogin, btnSignUp;
    private EditText etUserName, etPassword, etInitialBalance;

    private UserDatabase db;


    public class InvalidInputException extends Exception{
        public InvalidInputException(){
            txtMessage.setText("invalid_input");
            txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
            txtMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        db = UserDatabase.getInstance(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (userName.equals("") || password.equals("")) {
                    txtMessage.setText("The username/password field is empty.");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                    return;
                }
                Double initialBalance = null;

                if (!etInitialBalance.getText().toString().equals("")) {
                    initialBalance = Double.parseDouble(etInitialBalance.getText().toString());
                }

                User userNameCheck = db.userDao().selectSingleUserByName(userName);

                String sql = "SELECT * FROM users WHERE userName='" + userName + "'" + " AND password='" + password + "'";
                Cursor userCursor = db.query(sql, null);

                // User name does not exist
                if (userNameCheck == null) {
                    txtMessage.setText("This user does not exist!");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                }

                // Username exists
                else {
                    if (userCursor.getCount() <= 0) {
                        txtMessage.setText("Wrong password!");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                    else if (etInitialBalance.getText().toString().startsWith("0") && initialBalance != null && initialBalance >= 1) {
                        txtMessage.setText("Wrong input balance!");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                    else if (initialBalance != null && initialBalance > 4294967295.99){
                        txtMessage.setText("Wrong input balance!");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                    else if (etInitialBalance.getText().toString().contains(".")
                        && (etInitialBalance.getText().toString().length() - etInitialBalance.getText().toString().indexOf('.') - 1) > 2) {
                            txtMessage.setText("Wrong input balance!");
                            txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                            txtMessage.setVisibility(View.VISIBLE);

                    }
                    else {
                        // Successful Login

                        if (initialBalance != null) userNameCheck.setBalance(initialBalance);
                        db.userDao().updateSingleUser(userNameCheck);

                        Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (userCursor.moveToFirst()) {
                            intent.putExtra("userId", userCursor.getInt(userCursor.getColumnIndex("id")));

                            userCursor.close();
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
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
                if(!etInitialBalance.getText().toString().equals("")) initialBalance = Double.parseDouble(etInitialBalance.getText().toString());


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
                        db.userDao().insertSingleUser(user);
                        user.setBalance(initialBalance);
                        db.userDao().updateSingleUser(user);
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

