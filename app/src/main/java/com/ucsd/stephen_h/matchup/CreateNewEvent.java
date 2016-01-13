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
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

public class CreateNewEvent extends AppCompatActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView dateSetText;
    private DatePicker datePicker;

    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    private EditText description;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        dateSetText = (TextView) findViewById(R.id.date_set);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setVisibility(View.GONE);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int monthOfYear = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        dateSetText.setText("Today's date: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        confirmButton = (Button) findViewById(R.id.confirmEventButton);
        description = (EditText) findViewById(R.id.eventDescription);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                String username = user.getUsername();
                final ParseObject event = new ParseObject("Event");
                event.put("user", user);
                event.put("userName", username);
                event.put("year", currentYear);
                event.put("month", currentMonth);
                event.put("day", currentDay);
                event.put("hour", currentHour);
                event.put("minute", currentMinute);
                event.put("description", description.getText().toString());
                //event.put("pairedOrNot", false);
                //event.put("deletedEvents", false);
                event.put("numberStatus", 0);
                event.put("pairedEventID", "not matched");
                //ParseACL.setDefaultACL(PublicKey true);

                //event.setACL(new Parse.ACL());
                ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
                postACL.setPublicReadAccess(true);
                postACL.setPublicWriteAccess(true);
                event.setACL(postACL);
                System.out.println("This is the public write access of created event!!:   "+postACL.getPublicWriteAccess());

                event.saveInBackground(new SaveCallback() {
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

                });
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
        TextView tv = (TextView) this.findViewById(R.id.tv);
        //Set a message for user
        tv.setText("Time chosen: ");
        //Display the user changed time on TextView
        if(hourOfDay < 10 && minute <10) {
            tv.setText(tv.getText() + "0" + String.valueOf(hourOfDay)
                    + ":" + "0" + String.valueOf(minute) + "\n");
        }
        else if (hourOfDay < 10) {
            tv.setText(tv.getText() + "0" + String.valueOf(hourOfDay)
                    + ":"  + String.valueOf(minute) + "\n");
        }
        else if (minute < 10) {
            tv.setText(tv.getText()  + String.valueOf(hourOfDay)
                    + ":"  + "0" + String.valueOf(minute) + "\n");
        }
        else {
            tv.setText(tv.getText()  + String.valueOf(hourOfDay)
                    + ":" + String.valueOf(minute) + "\n");
        }
        currentHour = hourOfDay;
        currentMinute = minute;
    }
}
