package com.ucsd.stephen_h.matchup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PairedEventDetailPage extends AppCompatActivity {

    private TextView nameView;
    private TextView emailView;
    private TextView descriptionView;
    private ImageView profileImage;
    private TextView matchedScheduledTimeView;
    private TextView myDescriptionView;
    private TextView myScheduledTimeView;
    private List<Event> eventList = new ArrayList<Event>();
    private ListView listView;
    private PairedEventListPage eventPair;
    private int getMin = 0, getHour = 0, getDate = 0, getMonth = 0, getYear = 0;
    private int matchedMin, matchedHour, matchedDay, matchedMonth, matchedYear;
    private int myMin, myHour, myDay, myMonth, myYear;
    private boolean match = false;
    private String matchedEventID;
    private String myEventID;
    private ParseObject myEvent;
    private ParseObject matchedEvent;
    private String displayName = "No matched event found";
    private String matchedUsername;
    private String matchedUserTrueName;
    private String matchedUserEmail;
    private String EventIdToDelete="Empty";
    private String toDeleteDescription;
    private String[] arrEventsDeleted;
    private List<String> allDeclinedEvents;
    private int displayYear;
    private int displayMonth;
    private int displayDay = 0;
    private int displayHour;
    private int numberStatus;

    protected void updateEvent() {
        //CYC: partly copy from MyEventListPAge

        nameView = (TextView) findViewById(R.id.eventDetailPageName);
        emailView = (TextView) findViewById(R.id.eventDetailPageEmail);
        descriptionView = (TextView) findViewById(R.id.eventDetailPageDescription);
        matchedScheduledTimeView = (TextView) findViewById(R.id.eventDetailPageScheduledTime);
        myScheduledTimeView = (TextView) findViewById(R.id.eventDetailPageMyScheduledTime);

        matchedUsername = matchedEvent.getString("userName");

        System.out.println("Get username: "+matchedUsername);

        int imageID = R.drawable.profle1;
        try {
            imageID = getProfileImageID(matchedUsername);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        try {
            matchedUserTrueName = getMatchedUserName(matchedUsername);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            matchedUserEmail = getMatchedUserEmail(matchedUsername);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        matchedYear = matchedEvent.getInt("year");
        matchedMonth = matchedEvent.getInt("month");
        matchedDay = matchedEvent.getInt("day");
        matchedHour = matchedEvent.getInt("hour");
        matchedMin = matchedEvent.getInt("minute");

        String matchedUserIntendedTime;

        if(matchedHour < 10 && matchedMin < 10) {
            matchedUserIntendedTime = "Matched user's intended Time: " + matchedMonth +"/" + matchedDay + "/" +
                    matchedYear + " " + "0" + matchedHour + ":" + "0" + matchedMin;
        }
        else if(matchedHour < 10) {
            matchedUserIntendedTime = "Matched user's intended Time: " + matchedMonth +"/" + matchedDay + "/" +
                    matchedYear + " " + "0" + matchedHour + ":" + matchedMin;
        }
        else if(matchedMin < 10) {
            matchedUserIntendedTime = "Matched user's intended Time: " + matchedMonth +"/" + matchedDay + "/" +
                    matchedYear + " " + matchedHour + ":" + "0" + matchedMin;
        }
        else {
            matchedUserIntendedTime = "Matched user's intended Time: " + matchedMonth +"/" + matchedDay + "/" +
                    matchedYear + " " + matchedHour + ":" + matchedMin;
        }

        matchedScheduledTimeView.setText(matchedUserIntendedTime + "\n");

        myYear = myEvent.getInt("year");
        myMonth = myEvent.getInt("month");
        myDay = myEvent.getInt("day");
        myHour = myEvent.getInt("hour");
        myMin = myEvent.getInt("minute");

        String myIntendedTime;

        if(myHour < 10 && myMin < 10) {
            myIntendedTime = "My intended Time: " + myMonth +"/" + myDay + "/" +
                    myYear + " " + "0" + myHour + ":" + "0" + myMin;
        }
        else if(myHour < 10) {
            myIntendedTime = "My intended Time: " + myMonth +"/" + myDay + "/" +
                    myYear + " " + "0" + myHour + ":" + myMin;
        }
        else if(myMin < 10) {
            myIntendedTime = "My intended Time: " + myMonth +"/" + myDay + "/" +
                    myYear + " " + myHour + ":" + "0" + myMin;
        }
        else {
            myIntendedTime = "Myintended Time: " + myMonth +"/" + myDay + "/" +
                    myYear + " " + myHour + ":" + myMin;
        }

        myScheduledTimeView.setText(myIntendedTime);
        myDescriptionView = (TextView) findViewById(R.id.eventDetailPageMyEventDescription);
        String myDescription = "My event: " + myEvent.getString("description");
        myDescriptionView.setText(myDescription);

        profileImage = (ImageView) findViewById(R.id.eventDetailPageAvatar);
        profileImage.setImageResource(imageID);

        System.out.println("Name before setText: " + displayName);
        nameView.setText("Matched user: " + matchedUserTrueName);
        emailView.setText("Email: " + matchedUserEmail);
        descriptionView.setText("Matched event: " + matchedEvent.getString("description"));

    }



    protected int getProfileImageID(String username) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        List<ParseUser> users = query.find();
        ParseUser tempUser = users.get(0);
        int tempID = tempUser.getInt("profileImageID");

        return tempID;
    }

    protected String getMatchedUserName(String username) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        List<ParseUser> users = query.find();
        ParseUser tempUser = users.get(0);
        String tempName = tempUser.getString("firstName") + " " + tempUser.getString("lastName");

        return tempName;
    }

    protected String getMatchedUserEmail(String username) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        List<ParseUser> users = query.find();
        ParseUser tempUser = users.get(0);
        return tempUser.getString("email");
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_page);

        ParseUser currUsr = ParseUser.getCurrentUser();

        final String name = currUsr.getString("firstName") + " " + currUsr.getString("lastName");
        final String userEmail = currUsr.getString("email");

        // CYC: get the objectID of the event that we are trying to match
        final Bundle bundleObjectID = getIntent().getExtras();
        String matchedTempEventID = bundleObjectID.getString("matchedEventID");
        this.matchedEventID = matchedTempEventID;

        String myTempEventID = bundleObjectID.getString("myEventID");
        this.myEventID = myTempEventID;

        //Yuchen: Get the thisEvent, which is the current Event that the current user chose
        ParseQuery<ParseObject> queryFindMatched = ParseQuery.getQuery("Event");
        queryFindMatched.getInBackground(matchedEventID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject currentEvent, ParseException e) {
                if (e == null) {
                    //Get the current event ParseObject
                    System.out.println("you mei you zhi xing");
                    matchedEvent = currentEvent;

                    ParseQuery<ParseObject> queryFindMatched2 = ParseQuery.getQuery("Event");
                    queryFindMatched2.getInBackground(myEventID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject event, ParseException e) {
                            if (e == null) {
                                //Get the current event ParseObject
                                myEvent = event;
                                System.out.println("matchedEventID: " + matchedEventID + " myEventID: " + myEventID);
                                updateEvent();

                            } else {
                                System.out.println("Bu Ke Neng Mei You Zhao Dao");
                            }
                        }
                    });
                } else {
                    System.out.println("Bu Ke Neng Mei You Zhao Dao");
                }
            }
        });


/*        String thisEvemtID = matchedEvent.getString("pairedEventID");

        ParseQuery<ParseObject> queryFindCurr = ParseQuery.getQuery("Event");
        queryFindCurr.getInBackground(thisEvemtID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject currentEvent, ParseException e) {
                if (e == null) {
                    //Get the current event ParseObject
                    thisEvent = currentEvent;
                    System.out.println("you mei you zhi xing");
                } else {
                    System.out.println("Bu Ke Neng Mei You Zhao Dao");
                }
            }
        });*/

        //final String matchedName = displayName;

        //nameView = (TextView) findViewById(R.id.invitationPageName);
        //emailView = (TextView) findViewById(R.id.invitationPageEmail);

        //nameView.setText(matchedName);

        //emailView.setText("His/Her Email Address: ");


    }

}
