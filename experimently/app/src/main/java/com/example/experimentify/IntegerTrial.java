package com.example.experimentify;

public class IntegerTrial extends Trial {

    public IntegerTrial(String userId, String eID, int userInteger) {
        super(userId, eID);
        value = userInteger;
    }
    /**
     * Sets the value of a integer trial
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

}
