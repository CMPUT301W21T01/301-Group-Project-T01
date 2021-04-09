package com.example.experimentify;

import android.icu.text.SimpleDateFormat;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;


/**
 * This is a class that models an answer to a question.
 */
public class chatAnswer {
    private String description;
    private String UID;
    // Not sure how the date should be stored yet.
    private String date;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String EID;
    private String QID;

    public chatAnswer(String description, String UID, String EID, String QID) {
        this.description = description;
        this.UID = UID;
        date = sdf.format(new Date(System.currentTimeMillis()));
        this.EID = EID;
        this.QID = QID;
    }

    //need to implement timestamp change date
    public chatAnswer(String description, String UID, String EID, String date, String QID) {
        this.description = description;
        this.UID = UID;
        //date = sdf.format(new Date(System.currentTimeMillis()));
        this.EID = EID;
        this.date = date;
        this.QID = QID;
    }

    /**
     * This method gets the answer description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method gets the answer userid
     */
    public String getUID() {
        return UID;
    }
    /**
     * This method gets the answer date
     */
    public String getDate() {
        return date;
    }

    /**
     * This method sets the answer question id
     * @param QID
     */
    public void setQID(String QID) {
        this.QID = QID;
    }
    /**
     * This method gets the answer experiment id
     */
    public String getEID() {
        return EID;
    }
    /**
     * This method gets the answer question id
     */
    public String getQID() {
        return QID;
    }


}
