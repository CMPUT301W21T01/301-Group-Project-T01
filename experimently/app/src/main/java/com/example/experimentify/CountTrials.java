package com.example.experimentify;

public class CountTrials extends Trial {
    private int value = 1;

    public CountTrials(String userId, String eID, String trialLocation) {
        super(userId, eID, trialLocation);
    }

    //Overloaded constructor
    public CountTrials(String userId, String EID, String trialLocation, Location location){
        super(userId, EID, trialLocation, location);
    }

    public int getValue() {
        return value;
    }
}
