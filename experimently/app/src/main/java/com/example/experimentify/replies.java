package com.example.experimentify;

import java.util.Date;

public class replies {

    private String content;
    private String userId;
    private String experiment;
    private Date dateReply;

    public replies(){
    }

    public replies(String content, String userId, String experiment, Date dateReply) {
        this.content = content;
        this.userId = userId;
        this.experiment = experiment;
        this.dateReply = dateReply;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }



}
