package com.example.experimentify;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

//Experiment newExp, FirebaseFirestore db, String ownerID

/**
 * TO DO, SOME WEIRD MOCKING ERRORS WE ARE RUNNING INTO
 * test for experiment controller methods (addtodb, edit in db, delete from db)
 * CURRENTLY FAILS BECAUSE WE ARE NOT MOCKING ANDROID.OS.LOOPER IN 30 PLACES (caused by: java.lang.RuntimeException: Method getMainLooper in android.os.Looper not mocked. See http://g.co/androidstudio/not-mocked for details.
 * 	at android.os.Looper.getMainLooper(Looper.java)
 * 	at com.google.firebase.FirebaseApp$UiExecutor.<clinit>(FirebaseApp.java:696)
 * 	... 28 more)
 *
 */
public class ExperimentControllerTests {

    private FirebaseFirestore db;
    private Experiment mockExp;
    private String ownerID;
    private ExperimentController testController;



    private String dbID;
    private String dbOwnerID;
    private String dbDescription;
    private String dbRegion;
    private String dbDate;

    private ArrayList<String> ownedExp;
    private ArrayList<String> emptyExp;


    /**
     * tests adding Experiment to DB (which also adds the experiment ID to the associated owner in the users collection
     * we make use of the empty constructor (for testing only) since the program will give an error about being unable to pass it a
     * valid context (the context passed in is not necessary for testing these methods
     */
    @Test
    public void testAddExperimentToDB(){
        // make mock variables

        ownerID = "Gnt580viu6ErzEnqTFiS";
        mockExp = new Experiment("unitTest for adding to db","edmonton",123,"march 2nd", true,"binomial");
        db = FirebaseFirestore.getInstance();


        testController = new ExperimentController();

        // add the mock experiment to the DB
        testController.addExperimentToDB(mockExp, db, ownerID);

        // now prove it worked by querying the db in the experiment collection
        DocumentReference docRef = db.collection("Experiments").document(mockExp.getUID());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("unit test success", "DocumentSnapshot data: " + document.getData());
                        dbID = document.getData().get("uid").toString();
                        dbOwnerID = document.getData().get("ownerID").toString();
                        dbDescription = document.getData().get("description").toString();
                        dbRegion = document.getData().get("region").toString();
                        dbDate = document.getData().get("date").toString();


                        // will pass these assertions the added document exists
                        assertTrue((dbID == mockExp.getUID()));
                        assertTrue((dbOwnerID == ownerID));
                        assertTrue((dbDescription == mockExp.getDescription()));
                        assertTrue((dbRegion == mockExp.getRegion()));
                        assertTrue((dbDate == mockExp.getDate()));
                    } else {
                        Log.d("unit test failed", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });

        // now prove the specific user in the User's collection has this experiment owned in their ownedExperiments field
        DocumentReference userRef = db.collection("Users").document(ownerID);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("unit test success", "DocumentSnapshot data: " + document.getData());
                        ownedExp = (ArrayList<String>) document.getData().get("ownedExperiments");

                        // will pass if the experiment owned by the user the is id of the experiment we sent to be added
                        assertTrue((ownedExp.get(0) == mockExp.getUID()));
                    } else {
                        Log.d("unit test failed", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });

    }
    /**
     * tests deleting Experiment from DB (which also deletes from the user's owned experiments in the Users collection)
     * we make use of the empty constructor (for testing only) since the program will give an error about being unable to pass it a
     * valid context (the context passed in is not necessary for testing these methods
     *
     */
    @Test
    public void testDeleteExperimentFromDB() {
        // make mock variables
        ownerID = "Gnt580viu6ErzEnqTFiS";
        db = FirebaseFirestore.getInstance();

        testController = new ExperimentController();

        // add the mock experiment to the DB
        testController.deleteExperimentFromDB(mockExp, db);

        // now prove it worked by querying the db
        DocumentReference docRef = db.collection("Experiments").document(mockExp.getUID());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("unit test fail", "DocumentSnapshot data: " + document.getData());
                    } else {
                        // false when the document is empty (which means experiment doesn't exist)
                        assertFalse(document.exists());
                        Log.d("unit test succeeded", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });

        // now the owner should not have this document as well
        docRef = db.collection("Users").document(ownerID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // user should exist but the document field is empty
                        emptyExp = (ArrayList<String>) document.getData().get("ownedExperiments");
                        assertTrue(emptyExp.size() == 0);

                        Log.d("test", "DocumentSnapshot data: " + document.getData());
                    } else {
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });
    }
}
