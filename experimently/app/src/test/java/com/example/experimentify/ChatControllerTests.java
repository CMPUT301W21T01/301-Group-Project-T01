package com.example.experimentify;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class ChatControllerTests {

    private FirebaseFirestore dbs;
    private FirebaseFirestore db;
    private chatQuestion mockQues;
    private chatAnswer mockAns;
    private String ownerID;
    private chatQuestionController testController;
    private chatAnswerController testControllerAns;



    private String dbDescription;
    private String dbUID;
    private String dbDate;
    private String dbQID;
    private String dbEID;



    /**
     * tests adding question to DB (which also adds the question ID to the associated answer
     * we make use of the empty constructor (for testing only) since the program will give an error about being unable to pass it a
     * valid context (the context passed in is not necessary for testing these methods
     */
    @Test
    public void testAddQuestionToDB(){
        // make mock variables

        ownerID = "Gnt580viu6ErzEnqTFiS";
        mockQues = new chatQuestion("unitTest for adding to db","edmonton","123","01/01/01","123");
        dbs = FirebaseFirestore.getInstance();
        testController = new chatQuestionController();
        // add the mock experiment to the DB
        testController.addQuestionToDB(mockQues, dbs);
        // now prove it worked by querying the db in the experiment collection
        DocumentReference docRef = dbs.collection("Experiments").document(mockQues.getEID()).collection("Questions").document();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("unit test success", "DocumentSnapshot data: " + document.getData());
                        dbEID = document.getData().get("eid").toString();
                        dbUID = document.getData().get("uid").toString();
                        dbDate = document.getData().get("date").toString();
                        dbDescription = document.getData().get("description").toString();
                        dbQID = document.getData().get("qid").toString();


                        // will pass these assertions the added document exists
                        assertTrue((dbEID.equals(mockQues.getEID())));
                        assertTrue((dbDescription.equals(mockQues.getDescription())));
                        assertTrue((dbDate.equals(mockQues.getDate())));
                        assertTrue((dbQID.equals(mockQues.getQID())));
                        assertTrue((dbUID.equals(mockQues.getUID())));
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
     * tests adding question to DB (which also adds the question ID to the associated answer
     * we make use of the empty constructor (for testing only) since the program will give an error about being unable to pass it a
     * valid context (the context passed in is not necessary for testing these methods
     */
    @Test
    public void testAddAnswerToDB(){
        // make mock variables

        ownerID = "Gnt580viu6ErzEnqTFiS";
        mockAns = new chatAnswer("unitTest for adding to db","edmonton","123","01/01/01","123");
        dbs = FirebaseFirestore.getInstance();
        testControllerAns = new chatAnswerController();
        // add the mock experiment to the DB
        testControllerAns.addAnswerToDB(mockAns, dbs);
        // now prove it worked by querying the db in the experiment collection
        DocumentReference docRef = dbs.collection("Experiments").document(mockAns.getEID()).collection("Answers").document();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("unit test success", "DocumentSnapshot data: " + document.getData());
                        dbEID = document.getData().get("eid").toString();
                        dbUID = document.getData().get("uid").toString();
                        dbDate = document.getData().get("date").toString();
                        dbDescription = document.getData().get("description").toString();


                        // will pass these assertions the added document exists
                        assertTrue((dbEID.equals(mockAns.getEID())));
                        assertTrue((dbDescription.equals(mockAns.getDescription())));
                        assertTrue((dbDate.equals(mockAns.getDate())));
                        assertTrue((dbUID.equals(mockAns.getUID())));
                    } else {
                        Log.d("unit test failed", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }
            }
        });

    }









}
