package com.example.experimentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ExperimentController {
    private ArrayList<Experiment> experiments;
    private ArrayAdapter<Experiment> listAdapter;

    private Experiment testExperiment;



    public ExperimentController(Context context) {
        experiments = new ArrayList<Experiment>();
        listAdapter = new ExperimentListAdapter(context, experiments);

        //This block of code is for testing
        testExperiment = new Experiment("Test description", "Test name", "Test date");
        experiments.add(testExperiment);
    }

    public ArrayAdapter<Experiment> getAdapter() {
        return listAdapter;
    }

    public int getSize() {
        return experiments.size();
    }

    //Creates new experiment and appends it to the ArrayList
    public void addExperiment(String desc, String name, String date) {
        experiments.add(new Experiment(desc, name, date));
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
}
