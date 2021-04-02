package com.example.experimentify;

public class IntegerTrial extends Trial {
    private int integerCount;
    private int intEntered;

    public IntegerTrial(String userId, String eID, Location trialLocation) {
        super(userId, eID);
    }

    public void addIntCount() {
        integerCount = 1;
    }

    public int getIntEntered() {
        return intEntered;
    }

    public void setIntEntered(int intEntered) {
        this.intEntered = intEntered;
    }
}
