package com.example.experimentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class Trial {
    private int userId;
    private Location trialLocation;
    private int count;
    private int fail;
    private int pass;
    private int integerCount;
    private int measurement;

    public void createCountTrial(){
        count += 1;
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

    public void createIntegerCount(){
        integerCount +=1;
    }

    /*
    public int returnIntegerCount(Experiment exp){


    }
    */

    public void createMeasurementTrial(Experiment exp){
        measurement += 1;
    }



}
