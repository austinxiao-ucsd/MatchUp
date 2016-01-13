package com.ucsd.stephen_h.matchup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgetPasswordActivity extends AppCompatActivity {

    String emailAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        final EditText emailAddress = (EditText) findViewById(R.id.forgetPasswordEmailAddress);

        Button submitButton = (Button) findViewById(R.id.forgetPasswordSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddressText = emailAddress.getText().toString().trim();
                checkEmailId();
            }
        });
    }

    protected void checkEmailId() {
        if(TextUtils.isEmpty(emailAddressText)) {
            Toast.makeText(ForgetPasswordActivity.this, "Please enter the email address", Toast.LENGTH_LONG).show();
        }
        else if(!emailAddressText.contains("@")) {
            Toast.makeText(ForgetPasswordActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
        }
        else {
            resetPassowrd(emailAddressText);
        }
    }

    protected void resetPassowrd(final String emailAddress) {
        ParseUser.requestPasswordResetInBackground(emailAddress, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(ForgetPasswordActivity.this, "A mail is sent to " + emailAddress, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
