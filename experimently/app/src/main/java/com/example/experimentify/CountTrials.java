package com.example.experimentify;

public class CountTrials extends Trial {
    private int value = 1;

    public CountTrials(String userId, String eID, String trialLocation, String date) {
        super(userId, eID, trialLocation, date);
    }

    //Overloaded constructor
    public CountTrials(String userId, String EID, String trialLocation, String date, Location location){
        super(userId, EID, trialLocation, date, location);
    }

    public int getValue() {
        return value;
    }
}
