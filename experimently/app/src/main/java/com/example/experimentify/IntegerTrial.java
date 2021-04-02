package com.example.experimentify;

public class IntegerTrial extends Trial {
    private int value;

    public IntegerTrial(String userId, String eID, int userInteger) {
        super(userId, eID);
        value = userInteger;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
