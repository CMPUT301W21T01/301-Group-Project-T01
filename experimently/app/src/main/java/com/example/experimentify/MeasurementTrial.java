package com.example.experimentify;

public class MeasurementTrial extends Trial {
    private float value;

    public MeasurementTrial(String userId, String eID, String trialLocation, float measurement) {
        super(userId, eID, trialLocation);
        value = measurement;
    }
    public MeasurementTrial(String userId, String eID, String trialLocation, Location location, float measurement) {
        super(userId, eID, trialLocation, location);
        value = measurement;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float measurementEntered) {
        this.value = measurementEntered;
    }
}
