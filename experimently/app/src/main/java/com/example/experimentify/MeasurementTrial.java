package com.example.experimentify;

public class MeasurementTrial extends Trial {
    private float value;

    public MeasurementTrial(String userId, String eID, String trialLocation, String date, float measurement) {
        super(userId, eID, trialLocation, date);
        value = measurement;
    }
    public MeasurementTrial(String userId, String eID, String trialLocation, String date, Location location, float measurement) {
        super(userId, eID, trialLocation, date, location);
        value = measurement;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float measurementEntered) {
        this.value = measurementEntered;
    }
}
