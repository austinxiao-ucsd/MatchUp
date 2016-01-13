package com.ucsd.stephen_h.matchup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PairedEventListPage extends AppCompatActivity {
    private PairedAdapter adapter;
    private List<Event> eventL = new ArrayList<Event>();
    private String matchedUsername;
    private String userTrueName;
    private ListView listV;
    private int profileImageID = R.drawable.profle1;
    private int getDate = 0, getMonth = 0, getYear = 0;
    private boolean match = false;
    private String matchEventString = "";
    private boolean alreadyMatched = false;
    private String currentID = "00";
    private String matchedEventID;
    private ParseUser currUser;
    //private ParseObject globalTemp;
    private EventTime newEventTime;
    private String iteratingUser;
    private String currDescription;
    private int i = 0;
    private int countSuccessAdmin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_event_list_page);
        updateEventList();

        ImageButton createNewEventButton = (ImageButton) findViewById(R.id.eventListPageActionBarAdd);
        createNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEventIntent = new Intent(PairedEventListPage.this, CreateNewEvent.class);

                startActivityForResult(addEventIntent, 1);
                //updateEventList();
            }
        });

        ImageButton refreshButton = (ImageButton) findViewById(R.id.eventListPageActionBarRefresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventL.clear();

                adapter.notifyDataSetChanged();

                SystemClock.sleep(500);

                updateEventList();

                Toast.makeText(PairedEventListPage.this, "Your matched event list is updated", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton myEventPageButton = (ImageButton) findViewById(R.id.pairedEventPageMyEventListButton);
        myEventPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myEventListIntent = new Intent(PairedEventListPage.this, MyEventListPage.class);

                myEventListIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(myEventListIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        ImageButton pairedEventPageProfileButton = (ImageButton) findViewById(R.id.pairedEventPageProfileButton);
        pairedEventPageProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(PairedEventListPage.this, ProfilePage.class);

                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(profileIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        ImageButton InterLinksButton = (ImageButton) findViewById(R.id.InterLinksButton);
        InterLinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InterQuestIntent = new Intent(PairedEventListPage.this, InterviewLinks.class);

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
                    updateEventList();
                }
        }
    }

    protected void updateEventList() {
        eventL.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    adapter = new PairedAdapter(
                            PairedEventListPage.this, R.layout.paired_event_item, eventL);

                    listV = (ListView) findViewById(R.id.pairedlist);
                    listV.setAdapter(adapter);

                   /* listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {

                            System.out.println("Adapter: get event at position: " + position);

                            Intent details = new Intent(PairedEventListPage.this, PairedEventDetailPage.class);
                            Bundle eventInfo = new Bundle();
                            Event event = eventL.get(position);
                            String matchedEventID = event.getMatchedObjectID();
                            String myEventID = event.getObjectID();
                            System.out.println("Adapter: matchedObjectID: " + matchedEventID + " myEventID: "+ myEventID);

                            eventInfo.putString("matchedEventID", matchedEventID);
                            eventInfo.putString("myEventID", myEventID);
                            details.putExtras(eventInfo);
                            view.getContext().startActivity(details);
                        }
                    });*/

                    for (ParseObject parse : list) {
                        //globalTemp = parse;
                        currDescription = parse.getString("description");

                        currentID = parse.getObjectId();

                        //if (!parse.getBoolean("pairedOrNot")) {
                        //    continue;
                        //}
                        if (parse.getInt("numberStatus") != 3) {
                            continue;
                        }

                        currUser = ParseUser.getCurrentUser();
                        iteratingUser = parse.getString("userName");

                        if (currUser.getUsername().compareTo(iteratingUser) != 0) {
                            i++;
                            System.out.println("Times when continue because of user name different" + i);
                            continue;
                        }
                        countSuccessAdmin++;
                        System.out.println("countSuccessAdmin: " + countSuccessAdmin + "  + description" + currDescription);

                        ///////

                        // set the fields
                        /*if (getYear == parse.getInt("year") && getMonth == parse.getInt("month")
                                && getDate == parse.getInt("day")){
                            match = true;
                        }*/

                        // update the get variables
                        getYear = parse.getInt("year");
                        getMonth = parse.getInt("month");
                        getDate = parse.getInt("day");

                        //Get the matchedEvent Parse Object
                        //String matchedEventID = parse.getString("pairedEventID");
                        //Get the current event ParseObject
                        //ParseObject matchedEvent = matchedOfIteratingEvent;
                        //System.out.println("In loop 1 " + "this it the current User name, always be admin or Yuchen in the same run" +currDescription);
                        // System.out.println(globalTemp.getString("userName"));
                        //if (currUser.getUsername().equals(iteratingUser)) {
                        // Reset the string label
                        //System.out.println("We are inside here yay");
                        //System.out.println(currDescription);
                        matchEventString = parse.getString("matchedDescription");
                        matchedUsername = parse.getString("matchedUserName");
                        matchedEventID = parse.getString("pairedEventID");

                        System.out.println("Mathed User:" + matchedUsername);

                        try {
                            newEventTime = getMatchedUserScheduledTime(matchedEventID);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        try {
                            userTrueName = getMatchedUserName(matchedUsername);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        //String info = "OutLoop: Mathed User:"+matchedUsername+" profileId:"+profileImageID;
                        //System.out.println(info);

                        try {
                            profileImageID = getProfileImageID(matchedUsername);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        Event newEvent = new Event(userTrueName + ": "
                                + matchEventString, profileImageID, newEventTime);
                        System.out.println(newEvent.getBriefContent());
                        newEvent.setObjectID(currentID);
                        newEvent.setMatchedObjectID(matchedEventID);
                        System.out.println("PairedList: matchedEventID: " + newEvent.getMatchedObjectID() + " myEventID: " + newEvent.getObjectID());

                        adapter.insert(newEvent, 0);
                                    /*}
                                    else {
                                        System.out.println("Not equal");
                                    }*/
                        System.out.println("In loop 2 " + "this it the current User name, always be admin or Yuchen in the same run" + currDescription);
                        /*ParseQuery<ParseObject> queryFindmatched = ParseQuery.getQuery("Event");
                        queryFindmatched.getInBackground(matchedEventID, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject matchedOfIteratingEvent, ParseException e) {
                                if (e == null) {
                                    //Get the current event ParseObject
                                    ParseObject matchedEvent = matchedOfIteratingEvent;
                                    System.out.println("In loop 1 " + "this it the current User name, always be admin or Yuchen in the same run" +currDescription);
                                   // System.out.println(globalTemp.getString("userName"));
                                    //if (currUser.getUsername().equals(iteratingUser)) {
                                        // Reset the string label
                                        System.out.println("We are inside here yay");
                                        System.out.println(currDescription);
                                        matchEventString = matchedOfIteratingEvent.getString("description");
                                        Event newEvent = new Event("[ " + currDescription + " ] belongs to *"+currUser.getUsername()+"* has already been matched to [ "
                                                + matchEventString + " ] belongs to *"+matchedOfIteratingEvent.getString("userName")+"* !", R.drawable.profle, newEventTime);
                                        adapter.insert(newEvent, 0);
                                    /*}
                                    else {
                                        System.out.println("Not equal");
                                    }*/
                        //System.out.println("In loop 2 " + "this it the current User name, always be admin or Yuchen in the same run" +currDescription);
                                /*} else {
                                    System.out.println("Exception");
                                }
                            }
                        });*/


                        // Reset the match boolean var to false
                        //match = false;
                    }
                } else {
                    Toast.makeText(PairedEventListPage.this, "No events found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected int getProfileImageID(String username) throws ParseException {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        List<ParseUser> users = query.find();
        ParseUser tempUser = users.get(0);
        int tempID = tempUser.getInt("profileImageID");

        String info1 = "OutLoop: Mathed User:"+username+" profileId:"+tempID;
        System.out.println(info1);
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

    protected EventTime getMatchedUserScheduledTime(String matchedEventID) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("objectId", matchedEventID);
        List<ParseObject> objects = query.find();
        ParseObject parse = objects.get(0);

        return new EventTime(parse.getInt("year"),
                parse.getInt("month"),
                parse.getInt("day"),
                parse.getInt("hour"),
                parse.getInt("minute"));
    }

    /**
     * Created by Stephen_H on 10/28/15.
     */

    public class PairedAdapter extends ArrayAdapter<Event> {
        private int resourceId;
        private int pos;
        private Context mcon;

        public PairedAdapter(Context context, int textViewResourceId, List<Event> objects) {
            super(context, textViewResourceId, objects);
            mcon = context;
            resourceId = textViewResourceId;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Event event = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.event_image);
            TextView titleView = (TextView) view.findViewById(R.id.event_title);
            TextView contentView = (TextView) view.findViewById(R.id.event_content);


            Point size = new Point();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(size);

            Button detailButton = (Button) view.findViewById(R.id.paired_detail_btn);

            System.out.println("Button not found: "+(detailButton==null));

            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent details = new Intent(mcon, PairedEventDetailPage.class);
                    Bundle eventInfo = new Bundle();
                    String matchedEventID = event.getMatchedObjectID();
                    String myEventID = event.getObjectID();
                    System.out.println("Adapter: matchedObjectID: " + matchedEventID + " myEventID: " + myEventID);

                    eventInfo.putString("matchedEventID", matchedEventID);
                    eventInfo.putString("myEventID", myEventID);
                    details.putExtras(eventInfo);
                    v.getContext().startActivity(details);
                }
            });

            int imageMaxSize = size.y / 13;

            imageView.setMaxHeight(imageMaxSize);
            imageView.setMaxWidth(imageMaxSize);
            imageView.setImageResource(event.getImageId());
            titleView.setText(event.getTitle());
            contentView.setHint(event.getBriefContent());

            return view;
        }

    }
}