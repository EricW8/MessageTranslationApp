package com.example.chattranslation.model;

import com.google.firebase.Timestamp;

public class FeedbackModel {
    private Timestamp createdTimestamp;
    private String userId;
    private String feedback;

    public FeedbackModel(Timestamp createdTimestamp, String userId, String feedback) {
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.feedback = feedback;
    }

    public FeedbackModel() {
    }


    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
