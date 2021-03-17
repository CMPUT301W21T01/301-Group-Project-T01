package com.example.experimentify;


import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is a class that models an experiment in which trials can be conducted on.
 */
public class Experiment implements Parcelable {
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
    private boolean editable;
    private String expType;

    public Experiment(String description, String region, long minTrials, String date, boolean locationRequired) {
        this.description = description;
        this.date = date;
        this.region = region;
        this.minTrials = minTrials;
        this.locationRequired = locationRequired;
        editable = true;
        ended = false;
        viewable = true;
    }

    protected Experiment(Parcel in) {
        viewable = in.readByte() != 0;
        ownerID = in.readString();
        experimentId = in.readString();
        description = in.readString();
        date = in.readString();
        minTrials = in.readLong();
        locationRequired = in.readByte() != 0;
        region = in.readString();
        ended = in.readByte() != 0;
        uid = in.readString();
        editable = in.readByte() != 0;
        expType = in.readString();
    }

    public static final Creator<Experiment> CREATOR = new Creator<Experiment>() {
        @Override
        public Experiment createFromParcel(Parcel in) {
            return new Experiment(in);
        }

        @Override
        public Experiment[] newArray(int size) {
            return new Experiment[size];
        }
    };

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

    public String getExperimentId(){return experimentId;}

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

    public String getUID() {
        return uid;
    }

    public void setUID(String id) {
        this.uid = id;
    }

    public boolean isEditable() {
        return editable;
    }

    public String getExpType(){return expType;}

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(region);
        dest.writeLong(minTrials);
        dest.writeBoolean(locationRequired);
    }

    //TODO Ask about the variables below
    //private Location region //Region? - from requirements
    //private int minTrials //ex wont be shown until this amount of trials are submitted

}
