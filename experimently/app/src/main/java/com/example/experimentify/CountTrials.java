package com.example.experimentify;

public class CountTrials extends Trial {

    private int count;
    private Location location;

    public CountTrials(String userId, String eID, Location trialLocation) {
        super(userId, eID);
        location = null;
    }

    public CountTrials(String userId, String EID, String trialLocation, Location location){
        super(userId, EID);
        this.location = location;
    }


    public void addCount(){
        count =1;
    }

}
