package com.example.experimentify;

public class CountTrial extends Trial {

    /**
     * This method gsets the count trial value and experiment id
     * @param eID
     * @param userId
     */
    public CountTrial(String userId, String eID) {
        super(userId, eID);
        this.value = 1;
    }
}
