package com.ucsd.stephen_h.matchup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InterviewLinks extends AppCompatActivity  {

    private List<InterviewQuestion> interviewQuestionList = new ArrayList<InterviewQuestion>();
    private ListView listView;
    private InterviewQuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_links);

        ImageButton IQPairedEventButton = (ImageButton) findViewById(R.id.IQPairedEventListButton);
        IQPairedEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IQPairedIntent = new Intent(InterviewLinks.this, PairedEventListPage.class);

                IQPairedIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(IQPairedIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        ImageButton IQEventlistButton = (ImageButton) findViewById(R.id.InterQuestPageMyEventListButton);

        IQEventlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventlistIntent = new Intent(InterviewLinks.this, MyEventListPage.class);

                eventlistIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(eventlistIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });
        ImageButton InterQuestPageProfileButton = (ImageButton) findViewById(R.id.InterQuestPageProfileButton);
        InterQuestPageProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(InterviewLinks.this, ProfilePage.class);

                profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(profileIntent);

                finish();

                overridePendingTransition(0, 0);
            }
        });

        initQuestionLibrary();

        adapter = new InterviewQuestionAdapter(
                InterviewLinks.this, R.layout.interview_question_item, interviewQuestionList);

        listView = (ListView) findViewById(R.id.interviewQuestionListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                System.out.println("Adapter: get event at position: " + position);

                Intent details = new Intent(InterviewLinks.this, InterviewQuestionDetailsActivity.class);
                Bundle eventInfo = new Bundle();
                InterviewQuestion event = interviewQuestionList.get(position);

                eventInfo.putString("QuestionType", event.getQuestionType());
                details.putExtras(eventInfo);
                view.getContext().startActivity(details);
            }
        });
        /*
        Button externalButton = (Button) findViewById(R.id.button);
        externalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://leetcode.com/"));
                startActivity(intent);
            }
        });


        // Creating the item spinner
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.TypeQuestsSpinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Data Structures");
        categories.add("Programming Languages");
        categories.add("Time Complexity");
        categories.add("Algorithms");
        categories.add("General");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        */
    } // end of overwrite
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
     //   String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
     //   Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        TextView changeStruct = (TextView) findViewById(R.id.textView2);
        switch (position) {
            case 0:
                Toast.makeText(parent.getContext(), "You selected: Data Structures", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(parent.getContext(), "You selected: Programming Languages", Toast.LENGTH_SHORT).show();
                changeStruct.setText(getString(R.string.programming_languages));
                break;
            case 2:
                Toast.makeText(parent.getContext(), "You selected: Time Complexity", Toast.LENGTH_SHORT).show();
                changeStruct.setText(getString(R.string.time_complexity));
                break;
            case 3:
                Toast.makeText(parent.getContext(), "You selected: Algorithms", Toast.LENGTH_SHORT).show();
                changeStruct.setText(getString(R.string.algorithms_questions));
                break;
            case 4:
                Toast.makeText(parent.getContext(), "You selected: General", Toast.LENGTH_SHORT).show();
                changeStruct.setText(getString(R.string.general_questions));
                break;
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }*/

    private void initQuestionLibrary() {
        interviewQuestionList.add(new InterviewQuestion("Data Structures", R.drawable.datastruture, "Efficient way of storing data"));
        interviewQuestionList.add(new InterviewQuestion("Programming Languages", R.drawable.programminglanguage, "This makes programming so interesting"));
        interviewQuestionList.add(new InterviewQuestion("Time Complexity", R.drawable.timecomplexity, "We want things to run faster"));
        interviewQuestionList.add(new InterviewQuestion("Algorithms", R.drawable.algorithm, "This tackles common problems efficiently"));
        interviewQuestionList.add(new InterviewQuestion("General", R.drawable.general, "Just some random stuff"));
    }

    public class InterviewQuestionAdapter extends ArrayAdapter<InterviewQuestion> {
        private int resourceId;
        private Context mcon;

        public InterviewQuestionAdapter(Context context, int textViewResourceId, List<InterviewQuestion> objects) {
            super(context, textViewResourceId, objects);
            mcon = context;
            resourceId = textViewResourceId;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InterviewQuestion interview = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            int imageId = interview.getImageId();

            ImageView imageView = (ImageView) view.findViewById(R.id.question_image);
            TextView titleView = (TextView) view.findViewById(R.id.question_title);
            TextView contentView = (TextView) view.findViewById(R.id.question_brief_introduction);


            Point size = new Point();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getSize(size);


            int imageMaxSize = size.y / 13;
            imageView.setImageResource(imageId);
            imageView.setMaxHeight(imageMaxSize);
            imageView.setMaxWidth(imageMaxSize);
            titleView.setText(interview.getQuestionType());
            contentView.setHint(interview.getDescription());

            return view;
        }

    }

}
