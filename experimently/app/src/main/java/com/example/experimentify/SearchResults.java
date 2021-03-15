package com.example.experimentify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    private ExperimentController experimentController;
    private ExperimentListAdapter experimentAdapter;
    private ListView exListView;
    private ArrayList<Experiment> experimentList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();
        exListView.setAdapter(experimentController.getAdapter());
    }
}