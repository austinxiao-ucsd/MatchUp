package com.ucsd.stephen_h.matchup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.List;

public class MyEventListPage extends AppCompatActivity {


    //private boolean ifEventConfirmed = false;

    private List<Event> eventList = new ArrayList<Event>();
    private ListView listView;
    private EventAdapter adapter;
    private PairedEventListPage eventPair;
    private int getMin = 0, getHour = 0, getDate = 0, getMonth = 0, getYear = 0;
    private boolean match = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_list_page);

        //Intent intent = getIntent();
        //ParseUser currUsr =  ParseUser.getCurrentUser();
        //final String firstName = currUsr.getString("firstName");

        //final String firstName = intent.getStringExtra("extra_data");
        updateEventList();
        //Toast.makeText(MyEventListPage.this, "Welcome, "+firstName+"!", Toast.LENGTH_SHORT).show();

        //initEvents();


        //Toast.makeText(this,"list size" + eventList.size(),Toast.LENGTH_LONG).show();
        ImageButton createNewEventButton = (ImageButton) findViewById(R.id.eventListPageActionBarAdd);
        createNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEventIntent = new Intent(MyEventListPage.this, CreateNewEvent.class);
                startActivityForResult(addEventIntent, 1);
            }
        });

        ImageButton refreshButton = (ImageButton) findViewById(R.id.eventListPageActionBarRefresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventList.clear();

                adapter.notifyDataSetChanged();

                SystemClock.sleep(500);

                updateEventList();

                Toast.makeText(MyEventListPage.this, "Your event list is updated", Toast.LENGTH_SHORT).show();
            }
        });



        ImageButton myEventPageProfileButton = (ImageButton) findViewById(R.id.myEventPageProfileButton);
        myEventPageProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MyEventListPage.this, ProfilePage.class);

                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(profileIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        ImageButton pairedEventPageButton = (ImageButton) findViewById(R.id.myEventPagePairedEventListButton);
        pairedEventPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pairedEventListIntent = new Intent(MyEventListPage.this, PairedEventListPage.class);

                pairedEventListIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(pairedEventListIntent);

                finish();

                overridePendingTransition(0,0);
            }
        });

        ImageButton InterLinksButton = (ImageButton) findViewById(R.id.InterLinksButton);
        InterLinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent InterQuestIntent = new Intent(MyEventListPage.this, InterviewLinks.class);

                InterQuestIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(InterQuestIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

 /**       Button popUpButton = (Button) findViewById(R.id.delete_btn);
        popUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popUpIntent = new Intent(MyEventListPage.this, MatchDialog.class);

                popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(popUpIntent);

                finish();

                overridePendingTransition(0, 0);
     //           R.id.delete_btn;
     //           MatchDialog cdd=new MatchDialog(MyEventListPage.this);
     //           cdd.show();
            }
        });
**/
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
        //final List<Event> newEvents = new ArrayList<Event>();
        eventList.clear();
        //SystemClock.sleep(2000);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    //Toast.makeText(MyEventListPage.this, "parse list size is " + list.size(), Toast.LENGTH_LONG).show();

                    adapter = new EventAdapter(
                            MyEventListPage.this, R.layout.my_event_item, eventList);

                    listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(adapter);



                    // check if equals
                    //List<Event> events = new ArrayList<Event>();
                    for (ParseObject parse : list) {
                        if(parse.getBoolean("deletedEvents")){
                            continue;
                        }
                        if (parse.getInt("numberStatus") == 3) {
                            continue;
                        }

                        EventTime newEventTime = new EventTime(parse.getInt("year"),
                                parse.getInt("month"),
                                parse.getInt("day"),
                                parse.getInt("hour"),
                                parse.getInt("minute"));

                        Event newEvent = new Event(parse.getString("description"),
                                R.drawable.profle, newEventTime);

                        //CYC: set objectID for the event
                        newEvent.setObjectID(parse.getObjectId());

                        adapter.insert(newEvent, 0);

                    }


                } else {
                    Toast.makeText(MyEventListPage.this, "No events found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Created by Stephen_H on 10/28/15.
     */

    public class EventAdapter extends ArrayAdapter<Event> {
        private int resourceId;
        private Context mcon;

        public EventAdapter(Context context, int textViewResourceId, List<Event> objects) {
            super(context, textViewResourceId, objects);
            mcon = context;
            resourceId = textViewResourceId;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Event event = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            ParseUser user = ParseUser.getCurrentUser();
            int imageId = user.getInt("profileImageID");

            ImageView imageView = (ImageView) view.findViewById(R.id.event_image);
            TextView titleView = (TextView) view.findViewById(R.id.event_title);
            TextView contentView = (TextView) view.findViewById(R.id.event_content);
            //Handle buttons and add onClickListeners
            //    deleteBtn.setTag(position);
            //    deleteBtn.setOnClickListener(new View.OnClickListener() {
            //       public void onClick(View v) {//DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
            //       }
            //   });
            /** Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
             deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LayoutInflater layoutInflater
            = (LayoutInflater)getBaseContext()
            .getSystemService(LAYOUT_INFLATER_SERVICE);
            }

            Intent popUp = new Intent(Event.this.getIntent());
            popUp.
            }**/
            // });
            Button matchBtn = (Button)view.findViewById(R.id.match_btn);
            matchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //        Intent addEventIntent = new Intent((Activity) getContext(), MatchDialog.class);
                    //    v.getContext().startActivity(new Intent((Activity) getContext(), MatchDialog.class));


                    //CYC: pass event information into invitation page
                    Intent details = new Intent(mcon, Invitation.class);
                    Bundle eventInfo = new Bundle();
                    String eventID = event.getObjectID();
                    eventInfo.putString("eventID", eventID);
                    details.putExtras(eventInfo);
                    v.getContext().startActivity(details);
                }
            });

            Button changeBtn = (Button)view.findViewById(R.id.change_btn);
            changeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventID = event.getObjectID();
                    Intent changeEventIntent = new Intent(mcon, ChangeExistingEvent.class);
                    Bundle eventInfo = new Bundle();
                    eventInfo.putString("eventID", eventID);
                    changeEventIntent.putExtras(eventInfo);
                    startActivityForResult(changeEventIntent,1);
                    updateEventList();
                }
            });




            // modifying the remove button
            Button removeBtn = (Button)view.findViewById(R.id.remove_btn);
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CYC: pass event information into invitation page
                    String eventID = event.getObjectID();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
                    query.getInBackground(eventID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject currEvent, ParseException e) {
                            //CYC: When it is the current event object!!!!!!! Then we search through all the events
                            if (e == null) {
                                //currEvent.put("deletedEvents", true);
                                if( currEvent.getString("pairedEventID") != null ) {
                                    String pairedEventID = currEvent.getString("pairedEventID");
                                    //currEvent.put("pairedEventID", null);
                                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Event");
                                    //query2.whereEqualTo("objectID", pairedEventID);
                                    try {
                                        ParseObject theEvent = query2.get(pairedEventID);
                                        theEvent.put("numberStatus", 0);
                                        theEvent.remove("matchedUserName");
                                        theEvent.remove("matchedDescription");
                                        theEvent.remove("pairedEventID");
                                        theEvent.save();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                                currEvent.deleteInBackground();
                                updateEventList();
                            /*try {
                                currEvent.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }*/
                            } else {

                            }
                        }
                    });

                /*Intent details = new Intent(mcon, MyEventListPage.class);
                Bundle eventInfo = new Bundle();
                eventInfo.putString("eventID", eventID);
                details.putExtras(eventInfo);
                v.getContext().startActivity(details);*/
                    Toast.makeText(v.getContext(), "Your event is deleted", Toast.LENGTH_SHORT).show();
                }
            });

            Point size = new Point();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(size);


            int imageMaxSize = size.y / 13;
            imageView.setImageResource(imageId);
            imageView.setMaxHeight(imageMaxSize);
            imageView.setMaxWidth(imageMaxSize);
            titleView.setText(event.getTitle());
            contentView.setHint(event.getBriefContent());

            return view;
        }

    }

}
