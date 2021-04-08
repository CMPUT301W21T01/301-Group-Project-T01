package com.example.experimentify;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TrialController {
    FirebaseFirestore db;
    private String localUserID;
    private static final String TAG = ExperimentActivity.class.getName();

    public TrialController(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        db = databaseSingleton.getDB();

    }

    public void addTrialToDB(Trial newTrial, Number result, @Nullable Location location, String localUserID){

        Map<String, Object> enterData = new HashMap<>();


        DocumentReference newRef = db.collection("Experiments").document(newTrial.getEID()).collection("Trials").document();
//        enterData.put("TID", newTrial.getTID());
        enterData.put("UID", newTrial.getUID());
        enterData.put("result", result);
        enterData.put("date", newTrial.getDate());

        db.collection("Experiments").document(newTrial.getEID()).update("trialCount", FieldValue.increment(1));

        if (location != null){
            enterData.put("location", location.getGeoPoint());
        }

        String TID = newRef.getId();

        newTrial.setTID(TID);
        newRef.set(enterData);

        DocumentReference trialRef = db.collection("Experiments").document(newTrial.getEID()).collection("Trials").document(TID);
        trialRef.update("TID", TID);


        SharedPreferences settings;


        DocumentReference expRef = db.collection("Experiments").document(newTrial.getEID());
        expRef.update("participants", FieldValue.arrayUnion(localUserID));
    };
}
