package com.example.experimentify;

import com.firebase.geofire.GeoLocation;

public abstract class Trial {
    private String UID; //user id
    private Location trialLocation; //might need to change to geohash/location
    private String TID; //trial id
    private String EID; //exp id

    public Trial(String UID, String EID){
        this.UID = UID;
        this.trialLocation = trialLocation;
        this.EID = EID;
    }

    public String getUID() {
        return UID;
    }

    public Location getTrialLocation() {
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

    public void setTrialLocation(Location trialLocation) {
        this.trialLocation = trialLocation;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }
}
