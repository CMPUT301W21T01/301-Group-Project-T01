package com.example.experimentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        List<String> searchable = new ArrayList<String>();
        //https://stackoverflow.com/a/36456911 add citation for below
        searchable.addAll(Arrays.asList(newExp.getDescription().toLowerCase().split("\\W+")));

        DocumentReference newRef = db.collection("Experiments").document();

        CollectionReference experiments = db.collection("Experiment");
        enterData.put("description", newExp.getDescription());
        enterData.put("isEnded", false);
        enterData.put("minTrials", newExp.getMinTrials());
        enterData.put("ownerID", 0);
        enterData.put("region", newExp.getRegion());
        enterData.put("editable", true);
        enterData.put("searchable", searchable);
        enterData.put("locationRequired", newExp.isLocationRequired());
        // UID?
        enterData.put("viewable", newExp.isViewable());
        enterData.put("date", newExp.getDate());
        newRef.set(enterData);
        String experimentID = newRef.getId();

        DocumentReference addID = db.collection("Experiments").document(experimentID);
        addID.update("EID", experimentID);
        addID.update("searchable", FieldValue.arrayUnion(experimentID));
        //System.out.println(experimentID);
    }
}

