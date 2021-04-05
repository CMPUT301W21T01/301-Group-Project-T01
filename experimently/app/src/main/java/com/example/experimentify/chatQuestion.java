package com.example.experimentify;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class chatQuestion {
    private String description;
    private String UID;
    // Not sure how the date should be stored yet.
    private String date;
    private int numReplies;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String EID;
    private String QID;

    public chatQuestion(String description, String UID, String EID) {
        this.description = description;
        this.UID = UID;
        this.numReplies = 0;
        date = sdf.format(new Date(System.currentTimeMillis()));
        this.EID = EID;
    }

    //need to implement timestamp change date
    public chatQuestion(String description, String UID, String EID, String date) {
        this.description = description;
        this.UID = UID;
        this.numReplies = 0;
        //date = sdf.format(new Date(System.currentTimeMillis()));
        this.EID = EID;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public String getUID() {
        return UID;
    }

    public String getDate() {
        return date;
    }

    public String getQID() {
        return QID;
    }

    public void setQID(String QID) {
        this.QID = QID;
    }

    public int getNumReplies() {
        return numReplies;
    }

    public String getEID() {
        return EID;
    }
}
