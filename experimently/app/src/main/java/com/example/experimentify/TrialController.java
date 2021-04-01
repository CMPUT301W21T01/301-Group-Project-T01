package com.example.experimentify;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TrialController {
    FirebaseFirestore db;

    public TrialController(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        db = databaseSingleton.getDB();
    }

    //Integer variation of addTrialToDB
    public void addTrialToDB(Trial newTrial, int result, @Nullable Location location){

        Map<String, Object> enterData = new HashMap<>();

        DocumentReference newRef = db.collection("Experiments").document(newTrial.getEID()).collection("Trials").document();
//        enterData.put("TID", newTrial.getTID());
        enterData.put("UID", newTrial.getUID());
        enterData.put("result", result);
        enterData.put("date", newTrial.getDate());

        if (location != null){
            enterData.put("location", location);
        }

//        TODO: add dependencies, understand geohashes, implement to Location class?
//        requires geohashes https://firebase.google.com/docs/firestore/solutions/geoqueries#java_1
//        enterData.put("Location")

        String TID = newRef.getId();

        newTrial.setTID(TID);
        newRef.set(enterData);

        DocumentReference trialRef = db.collection("Experiments").document(newTrial.getEID()).collection("Trials").document(TID);
        trialRef.update("TID", TID);
    };

    //Float variation of addTrialToDB
    public void addTrialToDB(Trial newTrial, float result, @Nullable Location location){

        Map<String, Object> enterData = new HashMap<>();

        DocumentReference newRef = db.collection("Experiments").document(newTrial.getEID()).collection("Trials").document();
//        enterData.put("TID", newTrial.getTID());
        enterData.put("UID", newTrial.getUID());
        enterData.put("result", result);

        if (location != null){
            enterData.put("location", location);
        }

//        TODO: add dependencies, understand geohashes, implement to Location class?
//        requires geohashes https://firebase.google.com/docs/firestore/solutions/geoqueries#java_1
//        enterData.put("Location")

        String TID = newRef.getId();

        newTrial.setTID(TID);
        newRef.set(enterData);

        DocumentReference trialRef = db.collection("Experiments").document(newTrial.getEID()).collection("Trials").document(TID);
        trialRef.update("TID", TID);
    };
}
