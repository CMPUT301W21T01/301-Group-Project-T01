package com.example.experimentify;

public class MeasurementTrial extends Trial {
    private int measurementCount;
    private float measurementEntered;


    public MeasurementTrial(String userId, String eID, String trialLocation) {
        super(userId, eID, trialLocation);
    }


    public void addMeasurement() {
        measurementCount = 1;
    }


    public float getMeasurementEntered() {
        return measurementEntered;
    }

    public void setMeasurementEntered(float measurementEntered) {
        this.measurementEntered = measurementEntered;
    }

}
