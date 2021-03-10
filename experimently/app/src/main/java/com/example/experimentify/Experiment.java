package com.example.experimentify;

import android.location.Location;
import android.media.Image;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

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


    //Setting region to a string for now until we implement location
    private String region;
    //private Location region;

<<<<<<< HEAD
    public Experiment(String description, String name, String region, long minTrials, String date, boolean locationRequired) {
=======
  
    public Experiment(String description, String name, String region, long minTrials, String date. @Nullable boolean locationRequired) {
>>>>>>> 39df64984602c1663af388f164675eba327f843f
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
