package com.example.experimentify;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.*;

import static org.junit.Assert.*;
//Experiment newExp, FirebaseFirestore db, String ownerID

/**
 * test for experiment controller methods (addtodb, edit in db, delete from db)
 * currently not working yet because cant initialize experimentControllerObject
 */
public class ExperimentControllerTests {

    private FirebaseFirestore db;
    private Experiment mockExp;
    private String ownerID;
    private ExperimentController testController;

    /**
     * tests adding Experiment to DB (which also adds the experiment ID to the associated owner in the users collection
     */
    @Test
    public void testAddExperimentToDB(){
        // make mock variables
        ownerID = "XkKjbNsZwLCwBe2zHmx2";
        mockExp = new Experiment("unitTest for adding to db","edmonton",123,"march 2nd", true);
        db = FirebaseFirestore.getInstance();


        //testController = new ExperimentController(MainActivity.this);

    }
}