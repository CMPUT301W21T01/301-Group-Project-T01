package com.example.experimentify;

public class CountTrial extends Trial {
    private int value = 1;

    public CountTrial(String userId, String eID, String trialLocation, String date) {
        super(userId, eID, trialLocation, date);
    }

    //Overloaded constructor
    public CountTrial(String userId, String EID, String trialLocation, String date, Location location){
        super(userId, EID, trialLocation, date, location);
    }

    public int getValue() {
        return value;
    }
}
