package com.example.experimentify;

public class IntegerTrial extends Trial {

    public IntegerTrial(String userId, String eID, int userInteger) {
        super(userId, eID);
        value = userInteger;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
