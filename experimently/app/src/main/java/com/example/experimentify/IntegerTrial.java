package com.example.experimentify;

public class IntegerTrial extends Trial {
    private int value;

    public IntegerTrial(String userId, String eID, String trialLocation, int userInteger) {
        super(userId, eID, trialLocation);
        value = userInteger;
    }

    public IntegerTrial(String userId, String eID, String trialLocation, Location location, int userInteger) {
        super(userId, eID, trialLocation, location);
        value = userInteger;
    }

    public int getIntEntered() {
        return value;
    }

    public void setIntEntered(int intEntered) {
        this.value = intEntered;
    }

    public int getValue() {
        return value;
    }
}
