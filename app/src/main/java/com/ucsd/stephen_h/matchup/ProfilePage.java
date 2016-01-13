package com.ucsd.stephen_h.matchup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseUser;

public class ProfilePage extends AppCompatActivity {

    TextView nameView;
    TextView emailView;
    Integer profileId = 0;
    ImageButton chooseProfilePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        ParseUser currUsr =  ParseUser.getCurrentUser();
        final String name = currUsr.getString("firstName") + " " + currUsr.getString("lastName");
        final String userEmail = currUsr.getString("email");

        profileId = currUsr.getInt("profileImageID");

        nameView = (TextView) findViewById(R.id.profilePageName);
        emailView = (TextView) findViewById(R.id.profilePageEmail);

        nameView.setText(name);
        emailView.setText(userEmail);

        chooseProfilePageButton= (ImageButton) findViewById(R.id.profilePageChooseProfileImageButton);
        chooseProfilePageButton.setImageResource(profileId);
        chooseProfilePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseProfileIntent = new Intent(ProfilePage.this, ProfilePageChooseProfileImageActivity.class);

                startActivityForResult(chooseProfileIntent,1);
            }
        });

        final ImageButton myEventListPageButton = (ImageButton) findViewById(R.id.profilePageMyEventListButton);
        myEventListPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myEventListIntent = new Intent(ProfilePage.this, MyEventListPage.class);

                myEventListIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(myEventListIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        Button profilePageLogOutButton = (Button) findViewById(R.id.ProfilePageLogOutButton);
        profilePageLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfilePage.this, LoginPage.class);

                ParseUser.logOut();

                startActivity(loginIntent);

                finish();
            }
        });


        ImageButton pairedEventPageButton = (ImageButton) findViewById(R.id.profilePagePairedEventListButton);
        pairedEventPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pairedEventListIntent = new Intent(ProfilePage.this, PairedEventListPage.class);

                pairedEventListIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(pairedEventListIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        ImageButton InterLinksButton = (ImageButton) findViewById(R.id.InterLinksButton);
        InterLinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InterQuestIntent = new Intent(ProfilePage.this, InterviewLinks.class);

                InterQuestIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(InterQuestIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    ParseUser currUsr =  ParseUser.getCurrentUser();
                    profileId = currUsr.getInt("profileImageID");
                    chooseProfilePageButton.setImageResource(profileId);
                }
        }
    }
}
