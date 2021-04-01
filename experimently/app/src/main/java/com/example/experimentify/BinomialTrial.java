package com.example.experimentify;

public class BinomialTrial extends Trial{
    private int value;
    public BinomialTrial(String userId, String eID, String trialLocation) {
        super(userId, eID, trialLocation);
    }

    public BinomialTrial(String userId, String eID, Location location, String trialLocation, int result) {
        super(userId, eID, trialLocation, location);
        value = result;
    }

}


