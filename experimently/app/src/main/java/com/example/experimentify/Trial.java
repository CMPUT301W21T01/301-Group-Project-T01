package com.example.experimentify;

public abstract class Trial {
    private String UID; //user id
    private Location trialLocation; //might need to change to geohash/location
    private String TID; //trial id
    private String EID; //exp id
    private String date;
    protected Number value;

    public Trial(String UID, String EID){
        this.UID = UID;
        this.EID = EID;
        trialLocation = null;
    }
    /**
     * This method gets the user id
     *
     */
    public String getUID() {
        return UID;
    }
    /**
     * This method gets the trial location
     *
     */

    public Location getTrialLocation() {
        return trialLocation;
    }

    /**
     * This method gets the trial id
     *
     */
    public String getTID() {
        return TID;
    }
    /**
     * This method gets the experiment id
     *
     */

    public String getEID() {
        return EID;
    }
    /**
     * This method sets the user id
     * @param UID
     */

    public void setUID(String UID) {
        this.UID = UID;
    }
    /**
     * This method sets the trial location
     * @param trialLocation
     */

    public void setTrialLocation(Location trialLocation) {
        this.trialLocation = trialLocation;
    }

    /**
     * This method sets the trial id
     * @param TID
     */

    public void setTID(String TID) {
        this.TID = TID;
    }

    /**
     * This method sets experiment id
     * @param EID
     */

    public void setEID(String EID) {
        this.EID = EID;
    }

    /**
     * This method gets the trial date
     */

    public String getDate() {
        return date;
    }
    /**
     * This method sets the trial date
     * @param date
     */

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method gets the value
     */

    public Number getValue() {
        return value;
    }

}
