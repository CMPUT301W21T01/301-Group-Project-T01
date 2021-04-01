package com.example.experimentify;

public class MeasurementTrial extends Trial {
    private float value;

    public MeasurementTrial(String userId, String eID, float measurement) {
        super(userId, eID);
        value = measurement;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float measurementEntered) {
        this.value = measurementEntered;
    }
}
