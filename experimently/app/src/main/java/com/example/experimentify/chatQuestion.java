package com.example.experimentify;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

/**
 * This is a class that models an question which people can respond to.
 */
public class chatQuestion {
    private String description;
    private String UID;
    // Not sure how the date should be stored yet.
    private String date;
    private long numReplies;
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
    public chatQuestion(String description, String UID, String EID, String date, String QID) {
        this.description = description;
        this.UID = UID;
        this.numReplies = 0;
        //date = sdf.format(new Date(System.currentTimeMillis()));
        this.EID = EID;
        this.date = date;
        this.QID = QID;
    }
    /**
     * This method gets the question description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method gets the question user id
     */
    public String getUID() {
        return UID;
    }
    /**
     * This method gets the question date
     */
    public String getDate() {
        return date;
    }

    /**
     * This method gets the question id
     */
    public String getQID() {
        return QID;
    }

    /**
     * This method sets the question id
     * @param QID
     */
    public void setQID(String QID) {
        this.QID = QID;
    }

    /**
     * This method sets the question replies
     */
    public long getNumReplies() {
        return numReplies;
    }
    /**
     * This method sets the question id
     * @param numReplies
     */
    public void setNumReplies(long numReplies) {
        this.numReplies = numReplies;
    }

    /**
     * This method gets the question experiment id
     */
    public String getEID() {
        return EID;
    }
    /**
     * This method increments the num replies
     */
    public void incrementNumReplies(){
        numReplies++;
    }
}
