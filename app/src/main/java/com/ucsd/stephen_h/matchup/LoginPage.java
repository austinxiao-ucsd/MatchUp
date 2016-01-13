package com.ucsd.stephen_h.matchup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class LoginPage extends AppCompatActivity {
    private TextView info;
    private boolean isMainLobbyStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<String> permissions = Arrays.asList("user_birthday", "user_location", "user_friends", "email", "public_profile");
        // initiate instance of CallbackManager

        setContentView(R.layout.activity_login_page);
        // set view
        info = (TextView)findViewById(R.id.info);

        //Parse.initialize(this, "7RPfK8zjuezFnx7dZuGcKLSXf3ihcDQjozhzcnN4",
                //"g9YgLbNW5uxicD7CCrUyULr2fobau4g5hXPqhR8z");

        Button signUpButton = (Button) findViewById(R.id.loginPageNewUserButton);
        Button signInButton = (Button) findViewById(R.id.loginPageSignInButton);
        Button forgetPasswordButton = (Button) findViewById(R.id.loginPageForgetPasswordButton);



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpPageIntent = new Intent(LoginPage.this, RegisterPage.class);

                startActivity(signUpPageIntent);

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.loginPageUsername);
                EditText password = (EditText) findViewById(R.id.loginPagePassword);

                String usernameText = username.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                ParseUser.logInInBackground(usernameText, passwordText, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Toast.makeText(LoginPage.this, "Login Succeed!", Toast.LENGTH_SHORT).show();
                            String firstName = parseUser.getString("firstName");
                            Intent intent = new Intent(LoginPage.this, PairedEventListPage.class);
                            //intent.putExtra("extra_data", firstName);

                            Toast.makeText(LoginPage.this, "Welcome, " + firstName + "!", Toast.LENGTH_SHORT).show();

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginPage.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                /*if(username.getText().toString().equals("abc") && password.getText().toString().equals("123")) {
                    Toast.makeText(LoginPage.this, "Succeed!", Toast.LENGTH_SHORT).show();

                    Intent mainPageIntent = new Intent(LoginPage.this, MyEventListPage.class);

                    startActivity(mainPageIntent);

                    finish();

                } else {
                    Toast.makeText(LoginPage.this, "Failed!", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPasswordActivityIntent = new Intent(LoginPage.this, ForgetPasswordActivity.class);
                startActivity(forgetPasswordActivityIntent);
            }
        });
        // to register the custom callback
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/**
        List<String> permissions = Arrays.asList("user_birthday", "user_location", "user_friends", "email", "public_profile");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });**/

    }


}
