package com.ucsd.stephen_h.matchup;

/**
 * Created by Stephen_H on 12/4/15.
 */
public class InterviewQuestion {
    private String type;
    private int imageId;
    private String description;


    public InterviewQuestion(String type, int imageId, String description) {
        this.type = type;
        this.imageId = imageId;
        this.description = description;
    }

    public String getQuestionType() {
        return type;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDescription() {
        return description;
    }
}
