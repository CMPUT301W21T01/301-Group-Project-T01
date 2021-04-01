package com.example.experimentify;

public abstract class Trial {
    private String UID; //user id
    private String trialLocation; //might need to change to geohash/location
    private String TID; //trial id
    private String EID; //exp id
    private Location location;
    private String date;


    public Trial(String UID, String EID, String trialLocation, String date){
        this.UID = UID;
        this.trialLocation = trialLocation;
        this.EID = EID;
        this.date = date;
        location = null;

    }

    public Trial(String UID, String EID, String trialLocation, String date, Location location){
        this.UID = UID;
        this.trialLocation = trialLocation;
        this.EID = EID;
        this.date = date;
        this.location = location;
    }

    public String getUID() {
        return UID;
    }

    public String getTrialLocation() {
        return trialLocation;
    }

    public String getTID() {
        return TID;
    }

    public String getEID() {
        return EID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setTrialLocation(String trialLocation) {
        this.trialLocation = trialLocation;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    //    public abstract int getInteger();
//
//    public abstract float getFloat();
}
