package com.example.experimentify;


import android.media.Image;



import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is a class that models an experiment in which trials can be conducted on.
 */
public class Experiment implements Serializable {
    private boolean viewable;
    private String ownerID;
    private Image graph;
    private String experimentId;
    private ArrayList<Trial> trials;
    private String description;
    private String date;
    private long minTrials; //db takes in a long not an int
    private boolean locationRequired;
    private String region;
    private boolean ended;
    private String uid;


    public Experiment(String description, String region, long minTrials, String date, boolean locationRequired) {
        this.description = description;
        this.date = date;
        this.region = region;
        this.minTrials = minTrials;
        this.locationRequired = locationRequired;
        ended = false;
        viewable = true;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public long getMinTrials() {
        return minTrials;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isLocationRequired() {
        return locationRequired;
    }

    public boolean isViewable() {
        return viewable;
    }

    public boolean isEnded() {
        return ended;
    }
    public String getUID(){return uid;}

    public void setUID(String id){this.uid = id;}



    //TODO Ask about the variables below
    //private Location region //Region? - from requirements
    //private int minTrials //ex wont be shown until this amount of trials are submitted

}
