package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView txtMessage;
    private Button btnLogin, btnSignUp;
    private EditText etUserName, etPassword;

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
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                User userNameCheck = db.userDao().selectSingleUserByName(userName);

                User userLoginCheck = db.userDao().selectSingleUser(userName, password);

                // User name does not exist
                if (userNameCheck == null) {
                    txtMessage.setText("This user does not exist!");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
                }

                // Username exists
                else {
                    if (userLoginCheck == null) {
                        txtMessage.setText("Wrong password!");
                        txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        // Todo: Login succesful and send user to next activity
                        Toast.makeText(MainActivity.this, "Yay you logged in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);
                        intent.putExtra("userId", userLoginCheck.getId());
                        startActivity(intent);

                    }
                }


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                User userCheck = db.userDao().selectSingleUserByName(userName);

                if (userCheck == null) {
                    db.userDao().insertSingleUser(new User(userName, password, 0d));
                    txtMessage.setText("Sign Up Successful!");
                    txtMessage.setTextColor(getResources().getColor(R.color.positiveGreen));
                    txtMessage.setVisibility(View.VISIBLE);
                }
                else {
                    txtMessage.setText("This user already exists!");
                    txtMessage.setTextColor(getResources().getColor(R.color.negativeRed));
                    txtMessage.setVisibility(View.VISIBLE);
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
    }
}