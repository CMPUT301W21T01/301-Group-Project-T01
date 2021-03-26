package com.example.experimentify;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class chatQuestion {
    private String description;
    private String uID;
    // Not sure how the date should be stored yet.
    private String date;
    private int numReplies;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public chatQuestion(String description, String uID) {
        this.description = description;
        this.uID = uID;
        this.numReplies = 0;
        date = sdf.format(new Date(System.currentTimeMillis()));
    }

    public String getDescription() {
        return description;
    }

    public String getuID() {
        return uID;
    }

    public String getDate() {
        return date;
    }

    public int getNumReplies() {
        return numReplies;
    }

}
