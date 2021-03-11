package com.example.experimentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class models a list of experiments. It also holds an ArrayAdapter for displaying
 * experiments in a ListView.
 */
public class ExperimentController{
    private ArrayList<Experiment> experiments;
    private ArrayAdapter<Experiment> listAdapter;


    public ExperimentController(Context context) {
        experiments = new ArrayList<Experiment>();
        listAdapter = new ExperimentListAdapter(context, experiments);
    }

    public ArrayAdapter<Experiment> getAdapter() {
        return listAdapter;
    }

    public int getSize() {
        return experiments.size();
    }

    //Removes an experiment
    //TODO implement database delete
    public void deleteExperiment(int pos) {
       //pass
    }

    //TODO set up intent to switch to experiment activity
    public void viewExperiment(Activity activity, int pos) {
        //pass
    }

    public ArrayList<Experiment> getExperiments() {
        return experiments;
    }

    /**
     * This method adds an experiment to the database.
     * @param newExp
     * @param db
     */
    public void addExperimentToDB(Experiment newExp, FirebaseFirestore db){
        Map<String, Object> enterData = new HashMap<>();

        ArrayList<String> description = new ArrayList();
        description.add(newExp.getDescription().toLowerCase().trim());

        ArrayList<String> name = new ArrayList();
        name.add(newExp.getName().toLowerCase().trim());

        DocumentReference newRef = db.collection("Experiments").document();

        CollectionReference experiments = db.collection("Experiment");
        //DatabaseReference postRef = ;
        enterData.put("displayDescription", newExp.getDescription());
        enterData.put("description", description);
        enterData.put("isEnded", false);
        enterData.put("minTrials", newExp.getMinTrials());
        enterData.put("ownerID", 0);
        enterData.put("region", newExp.getRegion());
        enterData.put("displayName", newExp.getName());
        enterData.put("name", name);
        enterData.put("editable", true);
        // UID?
        enterData.put("viewable", true);
        enterData.put("date", newExp.getDate());
        newRef.set(enterData);
        String experimentID = newRef.getId();

        DocumentReference addID = db.collection("Experiments").document(experimentID);
        addID.update("EID", experimentID);
        //System.out.println(experimentID);
    }
}

