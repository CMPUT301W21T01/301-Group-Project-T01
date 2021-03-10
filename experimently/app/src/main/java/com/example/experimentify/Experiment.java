package com.example.experimentify;

import android.location.Location;
import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Experiment implements Serializable {
    private boolean viewable;
    private int ownerID;
    private Image graph;
    private String experimentId;
    private ArrayList<Trial> trials;
    private String description;
    private String name;
    private String date;
    private int minTrials;

    //Setting region to a string for now until we implement location
    private String region;
    //private Location region;

    public Experiment(String description, String name, String region, int minTrials, String date) {
        this.description = description;
        this.name = name;
        this.date = date;
        this.region = region;
        this.minTrials = minTrials;
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
//TODO Ask about the variables below
    //private Location region //Region? - from requirements
    //private int minTrials //ex wont be shown until this amount of trials are submitted

}
