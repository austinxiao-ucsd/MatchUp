package com.ucsd.stephen_h.matchup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InterviewQuestionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_question_details);

        final Bundle bundleObjectID = getIntent().getExtras();
        String questionType = bundleObjectID.getString("QuestionType");

        TextView titleView = (TextView) findViewById(R.id.interviewQuestionTypeTitle);
        TextView questionView = (TextView) findViewById(R.id.questionDetail);

        switch (questionType) {
            case "Data Structures":
                titleView.setText("Data Structures");
                questionView.setText(R.string.data_structures);
                break;
            case "Programming Languages":
                titleView.setText("Programming Languages");
                questionView.setText(R.string.programming_languages);
                break;
            case "Time Complexity":
                titleView.setText("Time Complexity");
                questionView.setText(R.string.time_complexity);
                break;
            case "Algorithms":
                titleView.setText("Algorithms");
                questionView.setText(R.string.algorithms_questions);
                break;
            case "General":
                titleView.setText("General");
                questionView.setText(R.string.general_questions);
                break;
        }

        Button externalButton = (Button) findViewById(R.id.knowMoreButton);
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
    }
}
