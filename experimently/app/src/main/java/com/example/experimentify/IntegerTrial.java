package com.example.experimentify;

public class IntegerTrial extends Trial {
    private int value;

    public IntegerTrial(String userId, String eID, String trialLocation, String date, int userInteger) {
        super(userId, eID, trialLocation, date);
        value = userInteger;
    }

    public IntegerTrial(String userId, String eID, String trialLocation, String date, Location location, int userInteger) {
        super(userId, eID, trialLocation, date, location);
        value = userInteger;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
