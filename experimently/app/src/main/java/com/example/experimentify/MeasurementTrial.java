package com.example.experimentify;

public class MeasurementTrial extends Trial {

    public MeasurementTrial(String userId, String eID, double measurement) {
        super(userId, eID);
        this.value = measurement;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
