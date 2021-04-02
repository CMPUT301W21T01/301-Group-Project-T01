package com.example.experimentify;

public class CountTrial extends Trial {
    public int value = 1;

    public CountTrial(String userId, String eID) {
        super(userId, eID);
    }

    public int getValue() {
        return value;
    }
}
