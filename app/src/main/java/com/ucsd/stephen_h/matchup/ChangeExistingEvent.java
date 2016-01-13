package com.ucsd.stephen_h.matchup;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.List;

public class ChangeExistingEvent extends AppCompatActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView dateSetText;
    private  TextView timeText;
    private DatePicker datePicker;

    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    private String formerDescription;
    private EditText description;
    private Button confirmButton;
    private ParseObject thisE;

    private String objectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        final Bundle bundleObjectID = getIntent().getExtras();
        String ID = bundleObjectID.getString("eventID");
        this.objectID = ID;

        dateSetText = (TextView) findViewById(R.id.date_set);
        timeText = (TextView) this.findViewById(R.id.tv);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setVisibility(View.GONE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(objectID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject currEvent, ParseException e) {
                //CYC: When it is the current event object!!!!!!! Then we search through all the events
                if (e == null) {
                    currentYear = currEvent.getInt("year");
                    currentMonth = currEvent.getInt("month");
                    currentDay = currEvent.getInt("day");
                    currentHour = currEvent.getInt("hour");
                    currentMinute = currEvent.getInt("minute");
                    formerDescription = currEvent.getString("description");

                    dateSetText.setText("Date chosen: " + currentYear + "-" + currentMonth + "-" + currentDay);
                    timeText.setText("Time chosen: " + currentHour + ":" + currentMinute + '\n');
                    description.setText(formerDescription);
                } else {

                }
            }
        });

        final Calendar c = Calendar.getInstance();
        /*int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);*/


        confirmButton = (Button) findViewById(R.id.confirmEventButton);
        description = (EditText) findViewById(R.id.eventDescription);
        confirmButton.setText("Save Changes");


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                String username = user.getUsername();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
                query.getInBackground(objectID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject currEvent, ParseException e) {
                        thisE = currEvent;
                        //CYC: When it is the current event object!!!!!!! Then we search through all the events
                        if (e == null) {
                            currEvent.put("year", currentYear);
                            currEvent.put("month", currentMonth);
                            currEvent.put("day", currentDay);
                            currEvent.put("hour", currentHour);
                            currEvent.put("minute", currentMinute);
                            currEvent.put("description", description.getText().toString());
                            //currEvent.put("pairedOrNot", false);
                            //currEvent.put("deletedEvents", false);
                            currEvent.put("numberStatus", 0);

                            ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
                            postACL.setPublicReadAccess(true);
                            postACL.setPublicWriteAccess(true);

                            try {
                                currEvent.save();
                                //finish();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                System.out.println("exception when add true to matchedEvent~~~~~~~");
                            }

                            if( currEvent.getString("pairedEventID") != null ) {
                                String pairedEventID = currEvent.getString("pairedEventID");
                                currEvent.remove("matchedUserName");
                                currEvent.remove("matchedDescription");
                                currEvent.remove("pairedEventID");
                                try {
                                    currEvent.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
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
                        } else {

                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
         /**       thisE.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        }
                    }

                }); **/


            }
        });

    }

    public void showDatePicker(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        dateSetText.setText("Date Chosen: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        datePicker.init(year, monthOfYear, dayOfMonth, null);
        currentYear = year;
        currentMonth = monthOfYear + 1;
        currentDay = dayOfMonth;
    }

    public void showTimePicker(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        //Set a message for user
        timeText.setText("Time chosen: ");
        //Display the user changed time on TextView
        timeText.setText(timeText.getText()  + String.valueOf(hourOfDay)
                + ":" + String.valueOf(minute) + "\n");
        currentHour = hourOfDay;
        currentMinute = minute;
    }
}
