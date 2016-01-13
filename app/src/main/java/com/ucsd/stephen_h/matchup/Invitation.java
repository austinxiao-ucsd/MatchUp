package com.ucsd.stephen_h.matchup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Invitation extends AppCompatActivity {

    private TextView nameView;
    private TextView emailView;
    private TextView descriptionView;
    private ImageView profileImage;
    private List<Event> eventList = new ArrayList<Event>();
    private ListView listView;
    private PairedEventListPage eventPair;
    private int getMin = 0, getHour = 0, getDate = 0, getMonth = 0, getYear = 0;
    private int currentMin, currentHour, currentDay, currentMonth, currentYear;
    private boolean match = false;
    private String objectID;
    private ParseObject thisEvent;
    private ParseObject matchedEvent;
    private String displayName = "No matched event found";
    private String matchedUsername;
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

    protected String updateEventList() {
        //CYC: partly copy from MyEventListPAge
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject currentEvent, ParseException e) {
                //CYC: When it is the current event object!!!!!!! Then we search through all the events
                if (e == null) {
                    thisEvent = currentEvent;
                    currentYear = currentEvent.getInt("year");
                    currentMonth = currentEvent.getInt("month");
                    currentDay = currentEvent.getInt("day");
                    currentHour = currentEvent.getInt("hour");
                    System.out.println(currentYear + currentMonth + currentDay + currentHour);

                    ////////////
                    match = false;
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Event");
                    //query2.whereEqualTo("user", ParseUser.getCurrentUser());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e2) {
                            if (e2 == null) {
                                int i = 0, c = 0;

                                for (ParseObject parse : objects) {
                                    EventTime newEventTime = new EventTime(parse.getInt("year"),
                                            parse.getInt("month"),
                                            parse.getInt("day"),
                                            parse.getInt("hour"),
                                            parse.getInt("minute"));
                                    System.out.println("Year: " + parse.getInt("year") + " Month: " + parse.getInt("month") + " Date: " + parse.getInt("day") + " Hour: " + parse.getInt("hour"));
                                    System.out.println(parse.getString("userName"));

                                    /*if(parse.getBoolean("deletedEvents")){
                                        continue;
                                    }*/

                                    //Yuchen: If the status of events are in waiting for accept or paired, then skip these events
                                    if((parse.getInt("numberStatus")==1) || (parse.getInt("numberStatus")==2) || (parse.getInt("numberStatus")==3)) {
                                        continue;
                                    }
                                    //Continue to the next loop if the event iterating belongs to the same user as the current event does
                                    if ((thisEvent.getString("userName").equals(parse.getString("userName"))) || (thisEvent.getObjectId().equals(parse.getObjectId()))) {
                                        System.out.println("These events are the same ");
                                        continue;
                                    }
                                    System.out.println(i);
                                    i++;

                                    if (parse.getList("declinedEvents") != null) {
                                        allDeclinedEvents = thisEvent.getList("declinedEvents");
                                    }


                                    // if An event that has the same time found
                                    if ((currentYear == parse.getInt("year")) && (currentMonth == parse.getInt("month"))
                                            && (currentDay == parse.getInt("day")) && (currentHour == parse.getInt("hour"))
                                /*&& (!(thisEvent.getObjectId().equals(parse.getObjectId())))*/) {
                                        match = true;
                                        if (allDeclinedEvents != null) {
                                            // check if the event has been declined
                                            for (c = 0; c < allDeclinedEvents.size(); c++) {
                                                System.out.println("matchedEventObjectID: " + parse.getObjectId() + "  objectIDinsideArray" + allDeclinedEvents.get(c));
                                                if (parse.getObjectId().equals(allDeclinedEvents.get(c))) {
                                                    match = false;


                                                }
                                            }
                                        }
                                        //If the paired event is already matched with another event
                                        if (parse.getInt("numberStatus") > 0) {
                                            match = false;
                                        }
                                        if (match) {
                                            matchedEvent = parse;
                                            matchedUsername = parse.getString("userName");
                                            try {
                                                displayName = getMatchedUserName(matchedUsername);
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                            try {
                                                matchedUserEmail = getMatchedUserEmail(matchedUsername);
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                            System.out.println(parse.getString("userName"));
                                            displayYear = parse.getInt("year");
                                            displayMonth = parse.getInt("month");
                                            displayDay = parse.getInt("day");
                                            displayHour = parse.getInt("hour");
                                            // stores the initial string
                                            EventIdToDelete = parse.getObjectId();
                                            toDeleteDescription = parse.getString("description");
                                            System.out.println(displayName);
                                            //thisEvent.put("pairedEventID", matchedEvent.getObjectId());
                                            //matchedEvent.put("pairedEventID", thisEvent.getObjectId());
                                            //thisEvent.put("matchedDescription", matchedEvent.getString("description"));
                                            //matchedEvent.put("matchedDescription", thisEvent.getString("description"));
                                            try {
                                                thisEvent.save();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                            try {
                                                matchedEvent.save();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    }
                                    System.out.println(match);

                                    if (match) {
                                        nameView = (TextView) findViewById(R.id.eventDetailPageName);
                                        emailView = (TextView) findViewById(R.id.eventDetailPageEmail);
                                        descriptionView = (TextView) findViewById(R.id.eventDetailPageDescription);

                                        int imageID = R.drawable.profle1;
                                        try {
                                            imageID = getProfileImageID(matchedUsername);
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        profileImage = (ImageView) findViewById(R.id.eventDetailPageAvatar);
                                        profileImage.setImageResource(imageID);

                                        System.out.println("Name before setText: " + displayName);
                                        nameView.setText("Matched user: " + displayName);
                                        emailView.setText("Email: " + matchedUserEmail);
                                        descriptionView.setText("Event description: " + toDeleteDescription);
                                        break;
                                    }
                                }
                                if (!match) {
                                    nameView = (TextView) findViewById(R.id.eventDetailPageName);
                                    emailView = (TextView) findViewById(R.id.eventDetailPageEmail);
                                    descriptionView = (TextView) findViewById(R.id.eventDetailPageDescription);
                                    nameView.setText("No User Found");
                                    emailView.setText("No Matched Event from Other Users Found");
                                    descriptionView.setText("");
                                }
                            } else {

                            }
                        }
                    });
                } else {

                }
            }
        });

        return displayName;
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


    protected void differentStates() {
        //When fisrt come to the Invitation page, we update the page by looking for the event
        System.out.println("diu lei lou mou");
        numberStatus = thisEvent.getInt("numberStatus");
        //If this event has not been found by other events and has not found other events
        //Yuchen: Search through all the events to find the matching event
        if (numberStatus == 0) {
            final String matchedName = updateEventList();

        }
        //If this event has found another event and accept that event, and is waiting for another's acception
        else if (numberStatus == 1) {

            //Get the matchedEvent Parse Object
            String matchedEventID = thisEvent.getString("pairedEventID");
            ParseQuery<ParseObject> queryFindmatched = ParseQuery.getQuery("Event");
            queryFindmatched.getInBackground(matchedEventID, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject currentEvent, ParseException e) {
                    if (e == null) {
                        //Get the current event ParseObject
                        matchedEvent = currentEvent;
                        //Display the matchedEvent's userName and description
                        //JiaXi: These chunk of line should be inside the if statement, otherwise, fatal exception
                        displayName = matchedEvent.getString("userName");

                        int imageIDOne = R.drawable.profle1;
                        try {
                            imageIDOne = getProfileImageID(displayName);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        profileImage = (ImageView) findViewById(R.id.eventDetailPageAvatar);
                        profileImage.setImageResource(imageIDOne);

                        toDeleteDescription = matchedEvent.getString("description");
                        nameView = (TextView) findViewById(R.id.eventDetailPageName);
                        emailView = (TextView) findViewById(R.id.eventDetailPageEmail);
                        System.out.println("Name before setText: " + displayName);
                        nameView.setText("Matched user is: " + displayName);
                        emailView.setText("Event description: " + toDeleteDescription);
                    } else {
                    }
                }
            });

        }
        //If this event has been found by another event, and another event is waiting for this event's acception
        else if (numberStatus == 2 ) {
            //Get the matchedEvent ParseObject
            String matchedEventID = thisEvent.getString("pairedEventID");
            ParseQuery<ParseObject> queryFindmatched = ParseQuery.getQuery("Event");
            queryFindmatched.getInBackground(matchedEventID, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject currentEvent, ParseException e) {
                    if (e == null) {
                        //Get the current event ParseObject
                        matchedEvent = currentEvent;
                        //Display the matchedEvent's userName and description
                        displayName = matchedEvent.getString("userName");

                        int imageIDTwo = R.drawable.profle1;
                        try {
                            imageIDTwo = getProfileImageID(displayName);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        profileImage = (ImageView) findViewById(R.id.eventDetailPageAvatar);
                        profileImage.setImageResource(imageIDTwo);

                        toDeleteDescription = matchedEvent.getString("description");
                        nameView = (TextView) findViewById(R.id.eventDetailPageName);
                        emailView = (TextView) findViewById(R.id.eventDetailPageEmail);
                        System.out.println("Name before setText: " + displayName);
                        nameView.setText("Matched user is: " + displayName);
                        emailView.setText("Event description: " + toDeleteDescription);
                    } else {
                    }
                }
            });
        }
        //If both are accepted
        else if (numberStatus == 3) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        ParseUser currUsr =  ParseUser.getCurrentUser();

        final String name = currUsr.getString("firstName") + " " + currUsr.getString("lastName");
        final String userEmail = currUsr.getString("email");

        // CYC: get the objectID of the event that we are trying to match
        final Bundle bundleObjectID = getIntent().getExtras();
        String ID = bundleObjectID.getString("eventID");
        this.objectID = ID;


        //Yuchen: Get the thisEvent, which is the current Event that the current user chose
        ParseQuery<ParseObject> queryFindCurr = ParseQuery.getQuery("Event");
        queryFindCurr.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject currentEvent, ParseException e) {
                if (e == null) {
                    //Get the current event ParseObject
                    thisEvent = currentEvent;
                    System.out.println("you mei you zhi xing");
                    differentStates();
                } else {
                    System.out.println("Bu Ke Neng Mei You Zhao Dao");
                }
            }
        });



        System.out.println("!!!!setString" + displayName);
        int integer = 99999999;
        System.out.println(integer);
        //final String matchedName = displayName;

        //nameView = (TextView) findViewById(R.id.invitationPageName);
        //emailView = (TextView) findViewById(R.id.invitationPageEmail);

        //nameView.setText(matchedName);

        //emailView.setText("His/Her Email Address: ");



        Button invitationPageAcceptButton = (Button) findViewById(R.id.InvitationPageAcceptButton);
        invitationPageAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //matchedEvent.put("pairedOrNot", true);
                //thisEvent.put("pairedOrNot", true);

                //If this event has not been found by other events and has not found other events
                if (numberStatus == 0) {

                    if(!match) {
                        Toast.makeText(Invitation.this, "No event to accept!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        thisEvent.put("numberStatus", 1);
                        matchedEvent.put("numberStatus", 2);
                        thisEvent.put("pairedEventID", matchedEvent.getObjectId());
                        matchedEvent.put("pairedEventID", thisEvent.getObjectId());
                        thisEvent.put("matchedDescription", matchedEvent.getString("description"));
                        matchedEvent.put("matchedDescription", thisEvent.getString("description"));
                        thisEvent.put("matchedUserName", matchedEvent.getString("userName"));
                        matchedEvent.put("matchedUserName", thisEvent.getString("userName"));
                    }

                }
                //If this event has found another event and accept that event, and is waiting for another's acception
                else if (numberStatus == 1) {
                    thisEvent.put("numberStatus", 1);
                    matchedEvent.put("numberStatus", 2);
                }
                //If this event has been found by another event, and another event is waiting for this event's acception
                else if (numberStatus == 2 ) {
                    thisEvent.put("numberStatus", 3);
                    matchedEvent.put("numberStatus", 3);
                }
                //If both are accepted
                else if (numberStatus == 3) {

                }

                try {
                    thisEvent.save();
                    matchedEvent.save();
                    System.out.println("Successfully add true to this Event~~~~~");
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("exception when adding true to this event!!!!");
                }

                //String matchedEventObjectID = matchedEvent.getObjectId();
                //Experient
                /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
                query.getInBackground(matchedEvent.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject currEvent, ParseException e) {
                        //CYC: When it is the current event object!!!!!!! Then we search through all the events
                        if (e == null) {
                            currEvent.put("pairedOrNot", true);
                            try {
                                currEvent.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                System.out.println("exception when add true to matchedEvent~~~~~~~");
                            }
                        } else {

                        }
                    }
                });*/
                    //endExperient

                    //      matchedEvent.saveInBackground();


                if(match) {
                    System.out.println("matchedEventID!!!!!!!!!!!!!!!!!: " + matchedEvent.getObjectId());
                    System.out.println("thisEventID!!!!!!!!!!!!!!!!!!!: " + thisEvent.getObjectId());
                }
                    //System.out.println("Truth val again --------> " + matchedEvent.getBoolean("pairedOrNot"));
                    // Navigate back to the Myeventlist page

                    finish();
                }
            }

            );

        Button invitationPageDeclineButton = (Button) findViewById(R.id.InvitationPageDeclineButton);
        invitationPageDeclineButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //        Intent loginIntent = new Intent(Invitation.this, LoginPage.class);
            //        startActivity(loginIntent);
            //        ParseUser.logOut();
            //        finish();

            //If this event has not been found by other events and has not found other events
            if (numberStatus == 0) {
                if(!match) {
                    Toast.makeText(Invitation.this, "No event to accept!", Toast.LENGTH_LONG).show();
                }
                else {
                    thisEvent.put("numberStatus", 0);
                    matchedEvent.put("numberStatus", 0);
                    thisEvent.put("pairedEventID", "not matched");
                    matchedEvent.put("pairedEventID", "not matched");
                }
            }
            //If this event has found another event and accept that event, and is waiting for another's acception
            else if (numberStatus == 1) {
                thisEvent.put("numberStatus", 0);
                matchedEvent.put("numberStatus", 0);
                thisEvent.put("pairedEventID", "not matched");
                matchedEvent.put("pairedEventID", "not matched");
            }
            //If this event has been found by another event, and another event is waiting for this event's acception
            else if (numberStatus == 2 ) {
                thisEvent.put("numberStatus", 0);
                matchedEvent.put("numberStatus", 0);
                thisEvent.put("pairedEventID", "not matched");
                matchedEvent.put("pairedEventID", "not matched");
            }
            //If both are accepted
            else if (numberStatus == 3) {

            }




            arrEventsDeleted = new String[999];
            arrEventsDeleted[0] = EventIdToDelete;
            System.out.println("The currentEvent " + arrEventsDeleted[0]);
            System.out.println("The Object ID is -> " + EventIdToDelete);
            System.out.println("Object ID is from -> [" + displayName + "]");
            System.out.println("Event Description -> [" + toDeleteDescription + "]");

            //Add the current event ID to its matched event's banned list
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
            query.getInBackground(EventIdToDelete, new GetCallback<ParseObject>() {
                public void done(ParseObject event, ParseException e) {
                    if (e == null) {
                        System.out.println("---->   ENTERED!!!!!!!!");
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.

                        List<String> matchedEventBannedList = event.getList("declinedEvents");
                        if (matchedEventBannedList != null) {
                            matchedEventBannedList.toString();
                            matchedEventBannedList.add(objectID);
                            //event.put("declinedEvents", Arrays.asList(objectID));
                            event.put("declinedEvents", matchedEventBannedList);
                        } else {
                            event.put("declinedEvents", Arrays.asList(objectID));
                        }
                        //    event.put("acceptedPair", "test");
                        event.saveInBackground();
                    }
                }
            });

            //Add the matched event's ID to the current Event's banned list
            ParseQuery<ParseObject> queryMatched = ParseQuery.getQuery("Event");
            queryMatched.getInBackground(objectID, new GetCallback<ParseObject>() {
                public void done(ParseObject event, ParseException e) {
                    if (e == null) {
                        System.out.println("---->   ENTERED!!!!!!!!");
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        List<String> currentEventBannedList = event.getList("declinedEvents");
                        if (currentEventBannedList != null) {
                            System.out.println(currentEventBannedList.size());
                            currentEventBannedList.toString();
                            currentEventBannedList.add(EventIdToDelete);
                            //event.put("declinedEvents", Arrays.asList(EventIdToDelete));
                            event.put("declinedEvents", currentEventBannedList);
                            //    event.put("acceptedPair", "test");
                        } else {
                            event.put("declinedEvents", Arrays.asList(EventIdToDelete));
                        }
                        event.saveInBackground();
                    }
                }
            });
            Intent intent = new Intent(Invitation.this, PairedEventListPage.class);
            setResult(RESULT_OK, intent);
            finish();

        }
        }

        );

        }

            }