package com.ucsd.stephen_h.matchup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterPage extends AppCompatActivity {

    ImageButton chooseProfilePageButton;
    int imageId = R.drawable.profle1;

    boolean pswMatched = true;
    boolean atContained = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {
            currentUser.logOut();
        }

        chooseProfilePageButton = (ImageButton) findViewById(R.id.registerPageChooseProfileImageButton);
        final EditText newUserName = (EditText) findViewById(R.id.registerPageUserName);
        final EditText newPassword = (EditText) findViewById(R.id.registerPagePassword);
        final EditText newPasswordAgain = (EditText) findViewById(R.id.registerPagePasswordAgain);
        final EditText newEmail = (EditText) findViewById(R.id.registerPageEmail);
        final EditText newFirstName = (EditText) findViewById(R.id.registerPageFirstName);
        final EditText newLastName = (EditText) findViewById(R.id.registerPageLastName);

        Button signUpButton = (Button) findViewById(R.id.registerPageSignUpButton);

        chooseProfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseProfilePageIntent = new Intent(RegisterPage.this, RegisterPageChooseProfileImageActivity.class);
                startActivityForResult(chooseProfilePageIntent, 1);
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();

                pswMatched = true;
                atContained = true;

                String usrname = newUserName.getText().toString().trim();
                String password = newPassword.getText().toString().trim();
                String passwordAgain = newPasswordAgain.getText().toString().trim();
                String email = newEmail.getText().toString().trim();
                String firstName = newFirstName.getText().toString().trim();
                String lastName = newLastName.getText().toString().trim();


                if(!(password.equals(passwordAgain))) {
                    Toast.makeText(RegisterPage.this, "Password must be same !", Toast.LENGTH_LONG)
                            .show();
                    pswMatched = false;

                }

                if(!(email.contains("@"))){
                    Toast.makeText(RegisterPage.this, "Email must contain @ !", Toast.LENGTH_LONG)
                            .show();
                    atContained = false;
                }

                if( pswMatched && atContained == true ){
                    user.setUsername(usrname);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("profileImageID",imageId);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Toast.makeText(RegisterPage.this, "Succeed!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Toast.makeText(RegisterPage.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    imageId = data.getIntExtra("profileImageID", R.drawable.profle1);
                    chooseProfilePageButton.setImageResource(imageId);
                }
                break;
            default:
        }
    }
}
