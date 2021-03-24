package com.example.experimentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public abstract class Trial {
    private String userId;
    private String trialLocation;
    private String trialId;
    private String eID; //exp id



    public Trial(String userId, String eID, String trialLocation){
        this.userId = userId;
        this.trialLocation = trialLocation;

    }

    public String getUserId() {
        return userId;
    }

    public String getTrialLocation() {
        return trialLocation;
    }

    public String getTrialId() {
        return trialId;
    }

    public String geteID() {
        return eID;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTrialLocation(String trialLocation) {
        this.trialLocation = trialLocation;
    }

    public void setTrialId(String trialId) {
        this.trialId = trialId;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }
}
