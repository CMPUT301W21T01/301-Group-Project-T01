package com.example.experimentify;

import android.media.Image;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ExperimentTests {
    private Experiment mockExperiment;

    /**
     * tests the getters and setters of the experiment class
     */
    @Test
    public void test_Setter_Getters(){
        // String description, String region, long minTrials, String date, boolean locationRequired, String expType
        mockExperiment = new Experiment("this is a test description","edmonton",3,"march 23,2021",true,"binomial");

        // test all the getters
        // currently cannot call getOwner and getUID() because we can't mock the firebase store in tests (we depend on firebase to uniquely generate the UID for us)
        assertEquals(mockExperiment.getDescription(),"this is a test description");
        assertEquals(mockExperiment.getRegion(),"edmonton");
        assertEquals(mockExperiment.getMinTrials(),3);
        assertEquals(mockExperiment.getDate(),"march 23,2021");
        assertEquals(mockExperiment.isLocationRequired(),true);
        assertEquals(mockExperiment.getExpType(),"binomial");

        //we are still overhauling experiment class and its attributes, so this will only test the core getters and setters of things necessary
        mockExperiment.setDescription("changed desc!");
        mockExperiment.setRegion("changed region!");
        mockExperiment.setMinTrials(1);
        mockExperiment.setDate("changed date!");
        mockExperiment.setExpType("changed exp type!");
        mockExperiment.setLocationRequired(false);

        // now test the setters

        assertEquals(mockExperiment.getDescription(),"changed desc!");
        assertEquals(mockExperiment.getRegion(),"changed region!");
        assertEquals(mockExperiment.getMinTrials(),1);
        assertEquals(mockExperiment.getDate(),"changed date!");
        assertEquals(mockExperiment.isLocationRequired(),false);
        assertEquals(mockExperiment.getExpType(),"changed exp type!");
    }

}
