package com.example.experimentify;

public class BinomialTrial extends Trial{
    private int value;
    public BinomialTrial(String userId, String eID, String trialLocation, String date) {
        super(userId, eID, trialLocation, date);
    }

    public BinomialTrial(String userId, String eID, String trialLocation, String date, Location location, int result) {
        super(userId, eID, trialLocation, date, location);
        value = result;
    }

}


