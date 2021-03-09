package com.example.experimentify;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;

public class Experiment {
    private boolean viewable;
    private int ownerID;
    private Image graph;
    private String experimentId;
    private ArrayList<Trial> trials;
    private String description;
    private String name;
    private Location region;
    private String date;

    public Experiment(String description, String name, String date) {
        this.description = description;
        this.name = name;
        this.date = date;
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

    public Location getRegion() {
        return region;
    }
//TODO Ask about the variables below
    //private Location region //Region? - from requirements
    //private int minTrials //ex wont be shown until this amount of trials are submitted

}
