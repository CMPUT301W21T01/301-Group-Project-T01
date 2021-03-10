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

public class ExperimentController{
    private ArrayList<Experiment> experiments;
    private ArrayAdapter<Experiment> listAdapter;
    private Experiment testExperiment;


//    db = FirebaseFirestore.getInstance("")

    public ExperimentController(Context context) {
        experiments = new ArrayList<Experiment>();
        listAdapter = new ExperimentListAdapter(context, experiments);

        //This block of code is for testing
        testExperiment = new Experiment("Test description", "test name", "Test region", 0, "2021/01/01");
        experiments.add(testExperiment);
    }

    public ArrayAdapter<Experiment> getAdapter() {
        return listAdapter;
    }

    public int getSize() {
        return experiments.size();
    }

    //Creates new experiment and appends it to the ArrayList
    public void addExperiment(Experiment ex) {
        experiments.add(ex);
        listAdapter.notifyDataSetChanged();
    }

    //Inserts an existing experiment into the ArrayList
    public void insertExperiment(Experiment ex, int pos) {
        //pass
    }

    //Removes an experiment
    public void deleteExperiment(int pos) {
        experiments.remove(pos);
        listAdapter.notifyDataSetChanged();
    }

    //Brings user to ConductTrial activity associated with the experiment that was clicked
    public void viewExperiment(Activity activity, int pos) {
        //pass
    }

//    public String getUID(FirebaseFirestore db){
//        ;
//    }

    public void addExperimentToDB(Experiment newExp, FirebaseFirestore db){
        Map<String, Object> enterData = new HashMap<>();

        ArrayList<String> description = new ArrayList();
        description.add(newExp.getName().toLowerCase().trim());

        ArrayList<String> name = new ArrayList();
        name.add(newExp.getName().toLowerCase().trim());

        DocumentReference newRef = db.collection("Experiments").document();

        CollectionReference experiments = db.collection("Experiment");
//        DatabaseReference postRef = ;
        enterData.put("displayDescription", newExp.getDescription());
        enterData.put("isEnded", false);
        enterData.put("minTrials", newExp.getMinTrials());
        enterData.put("ownerID", 0);
        enterData.put("region", newExp.getRegion());
        enterData.put("displayName", newExp.getName());
        enterData.put("name", newExp.getName().toLowerCase());
        enterData.put("editable", true);
        // UID?
        enterData.put("viewable", true);

        newRef.set(enterData);
        System.out.println(newRef.getId());
    }
}

