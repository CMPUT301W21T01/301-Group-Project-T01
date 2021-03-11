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
    private String name;
    private String date;
    private long minTrials; //db takes in a long not an int
    private boolean locationRequired;
    private String region;


    public Experiment(String description, String name, String region, long minTrials, String date, boolean locationRequired) {
        this.description = description;
        this.name = name;
        this.date = date;
        this.region = region;
        this.minTrials = minTrials;
        this.locationRequired = locationRequired;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
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

    //TODO Ask about the variables below
    //private Location region //Region? - from requirements
    //private int minTrials //ex wont be shown until this amount of trials are submitted

}
