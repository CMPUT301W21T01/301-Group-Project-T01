package com.example.experimentify;

public class CountTrials extends Trial {
    private int count;
    public CountTrials(String userId, String eID, String trialLocation) {
        super(userId, eID, trialLocation);
    }


    public void addCount(){
        count =1;
    }

}
