package com.example.experimentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class Trial {
    private String userId;
    private String trialLocation;
    private String trialId;


    public Trial(String userId, String trialLocation, String trialId){
        this.userId = userId;
        this.trialLocation = trialLocation;
        this.trialId = trialId;
    }

    public class CountTrial {
        private Trial trial;
        private int totalCount;
        private int trialCount = 1;
        public CountTrial(Trial trial, int trialCount){
            this.trial = trial;
            this.trialCount = trialCount;
        }

        public void incrementCount(){
            totalCount += 1;

        }
        /*
        public void decrementCount(){
            count -=1;
        }
        */
        public int getTotalCount(){
            return totalCount;
        }

    }

    public class BinomialTrial{
        private Trial trial;
        private int fail;
        private int pass;
        public BinomialTrial(Trial trial, int pass, int fail){
            this.trial = trial;
            this.pass = pass;
            this.fail = fail;
        }

        public void incrementBinomialFail(){
            fail += 1;
        }

        public void incrementBinomialPass(){
            pass += 1;
        }

        public int getBinomialTrials(){
            int total = pass + fail;
            return total;
        }

        public int getBinomialPasses(){
            return pass;
        }

        public int getBinomialFails(){
            return fail;
        }
    }


    public class IntegerTrial{
        private int integerCount;
        private int intEntered;
        private Trial trial;
        public IntegerTrial(Trial trial, int intEntered){
            this.trial = trial;
            this.intEntered = intEntered;
            this.integerCount = 0;

        }
        public int getIntegerCount(){
            return integerCount;
        }
        public int getIntEntered(){
            return intEntered;
        }

    }

    public class MeasurementTrial{
        private int measurementCount;
        private Trial trial;
        private float measurementEntered;
        public MeasurementTrial(Trial trial, float measurementEntered){
            this.trial=trial;
            this.measurementEntered = measurementEntered;
        }
        public void createMeasurementTrial(){
            measurementCount += 1;
        }

        public float getMeasurementEntered(){return measurementEntered;}
    }



}
