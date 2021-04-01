package com.example.experimentify;

public abstract class Trial {
    private String UID; //user id
    private String trialLocation; //might need to change to geohash/location
    private String TID; //trial id
    private String EID; //exp id
    public Location location;

    public Trial(String UID, String EID, String trialLocation){
        this.UID = UID;
        this.trialLocation = trialLocation;
        this.EID = EID;
        location = null;
    }

    public Trial(String UID, String EID, String trialLocation, Location location){
        this.UID = UID;
        this.trialLocation = trialLocation;
        this.EID = EID;
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

//    public abstract int getInteger();
//
//    public abstract float getFloat();
}
