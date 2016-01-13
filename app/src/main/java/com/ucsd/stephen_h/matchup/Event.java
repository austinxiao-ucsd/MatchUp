package com.ucsd.stephen_h.matchup;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup.LayoutParams;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.PopupWindow;
        import android.widget.TextView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import static android.support.v4.app.ActivityCompat.startActivity;


/**
 * Created by Stephen_H on 10/28/15.
 */
public class Event {

    private EventTime eventTime;

    private String title;
    private String briefContent;
    private int imageId;
    private String objectID;
    private String matchedObjectID;

    public Event(String title, int imageId, EventTime eventTime) {
        this.title = title;
        this.imageId = imageId;
        this.eventTime = eventTime;

        if(eventTime.getHour() < 10 && eventTime.getMinute() < 10) {
            this.briefContent = "Scheduled Time: " + eventTime.getMonth() +"/" + eventTime.getDay() + "/" +
                    eventTime.getYear() + " " + "0" + eventTime.getHour() + ":" + "0" + eventTime.getMinute();
        }
        else if(eventTime.getHour() < 10) {
            this.briefContent = "Scheduled Time: " + eventTime.getMonth() +"/" + eventTime.getDay() + "/" +
                    eventTime.getYear() + " " + "0" + eventTime.getHour() + ":" + eventTime.getMinute();
        }
        else if(eventTime.getMinute() < 10) {
            this.briefContent = "Scheduled Time: " + eventTime.getMonth() +"/" + eventTime.getDay() + "/" +
                    eventTime.getYear() + " " + eventTime.getHour() + ":" + "0" + eventTime.getMinute();
        }
        else {
            this.briefContent = "Scheduled Time: " + eventTime.getMonth() +"/" + eventTime.getDay() + "/" +
                    eventTime.getYear() + " " + eventTime.getHour() + ":" + eventTime.getMinute();
        }
        //R.id.delete_btn;
    //    Button deleteBtn = (Button)findViewById(R.id.delete_btn);
     //   MatchDialog cdd = new MatchDialog(Event.this);
        //cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
     //   cdd.show();

    }

    /**LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button popupButton, insidePopupButton;
    TextView popupText;
    @Override public void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_dialog);
        init();
        popupInit();
    }
    public void init()
    { popupButton = (Button) findViewById(R.id.delete_btn);
        popupText = new TextView(this);
        insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        insidePopupButton.setText("OK");
        popupText.setText("This is Popup Window.press OK to dismiss it.");
        popupText.setPadding(0, 0, 0, 20);
        layoutOfPopup.setOrientation(1);
        layoutOfPopup.addView(popupText);
        layoutOfPopup.addView(insidePopupButton);
    }
    public void popupInit()
    { popupButton.setOnClickListener(this);
        insidePopupButton.setOnClickListener(this);
        popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup);
    }
    @Override public void onClick(View v)
    { if (v.getId() == R.id.delete_btn)
    { popupMessage.showAsDropDown(popupButton, 0, 0); }
    else { popupMessage.dismiss(); } }
*/
    public void setObjectID(String id) {
        this.objectID = id;
    }
    public void setMatchedObjectID(String id) {
        this.matchedObjectID = id;
    }
    public String getMatchedObjectID() {
        return this.matchedObjectID;
    }


    public String getObjectID() {
        return this.objectID;
    }
    public String getTitle() {
        return this.title;
    }
    //MatchDialog cdd = new MatchDialog(Event.this);
    //cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    //cdd.show();

    public int getImageId() {
        return this.imageId;
    }

    public String getBriefContent() {
        return this.briefContent;
    }

    public EventTime getEventTime() { return this.eventTime; }
}
