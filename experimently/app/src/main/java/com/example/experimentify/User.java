package com.example.experimentify;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private ExperimentController subbedExperiments;
    private String email;
    private String name;
    private ArrayList<String> ownedExperiments;
    private ArrayList<String> participatingExperiments;
    private String uid;
    private String username;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "PrefsFile";


    public User() {
        this.uid = "";
        this.email = "";
        this.name = "";
        this.username = "";
        //this.subbedExperiments = new ExperimentController();
        this.ownedExperiments = new ArrayList<String>();
        this.participatingExperiments = new ArrayList<String>();
    }

    public ExperimentController getSubbedExperiments() {
        return subbedExperiments;
    }

    public void setSubbedExperiments(ExperimentController subbedExperiments) {
        this.subbedExperiments = subbedExperiments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getOwnedExperiments() {
        return ownedExperiments;
    }

    public void setOwnedExperiments(ArrayList<String> ownedExperiments) {
        this.ownedExperiments = ownedExperiments;
    }

    public ArrayList<String> getParticipatingExperiments() {
        return participatingExperiments;
    }

    public void setParticipatingExperiments(ArrayList<String> participatingExperiments) {
        this.participatingExperiments = participatingExperiments;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void generateUserId() {
        //pass
    }

    /**
     * This method gives activities access to user settings.
     * @param context application context
     * @return Returns SharedPreferences object
     */
    public SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void addSub(String userID, String expID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Users").document(userID);
        ref.update("participatingExperiments", FieldValue.arrayUnion(expID));
    }

    public void deleteSub(String userID, String expID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Users").document(userID);
        ref.update("participatingExperiments", FieldValue.arrayRemove(expID));
    }


}
