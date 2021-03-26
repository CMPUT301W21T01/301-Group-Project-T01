package com.example.experimentify;


import java.io.Serializable;

public class questions implements Serializable {

    private String content;
    private String userId;
    private String experiment;

    public questions(){
    }

    public questions(String content, String userId, String experiment) {
        this.content = content;
        this.userId = userId;
        this.experiment = experiment;
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
