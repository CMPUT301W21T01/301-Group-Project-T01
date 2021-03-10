package com.example.experimentify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AddExpFragment.OnFragmentInteractionListener {
    private ExperimentController experimentController;
    private ExperimentListAdapter experimentAdapter;
    private ListView exListView;
    private FloatingActionButton showAddExpUiButton;
    private ArrayList<Experiment> experimentList;
    final String TAG = MainActivity.class.getName();
    FirebaseFirestore db;


    //Shows fragment for creating new experiment
    private void showAddExpUi() {
        new AddExpFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
    }

    //Adds new experiment to experiment list
    private void addExperiment(Experiment experiment) {
        experimentController.addExperiment(experiment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        exListView = findViewById(R.id.exListView);
        showAddExpUiButton = findViewById(R.id.showAddExpUiButton);

//        ExperimentListAdapter experimentAdapter = new ExperimentListAdapter(this, )
        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();
        exListView.setAdapter(experimentController.getAdapter());

        showAddExpUiButton.setOnClickListener((v) -> {
            showAddExpUi();
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experimentController.getExperiments().clear();
                for (QueryDocumentSnapshot doc : value){
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    //Experiment(String description, String name, String region, int minTrials, String date)
                    String description = (String) doc.getData().get("displayDescription");
                    String displayName = (String) doc.getData().get("displayName");
                    String region      = (String) doc.getData().get("region");
                    Long minTrials      = (Long) doc.getData().get("minTrials");
                    String date        = (String) doc.getData().get("date");
                    experimentList.add(new Experiment(description, displayName, region, minTrials, date));
                }
                experimentController.getAdapter().notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onOkPressed(Experiment newExp) {
        experimentController.addExperimentToDB(newExp, db);
    }

    @Override
    public void onDeletePressed(Experiment current) {

    }

    @Override
    public void editItem(Experiment ogItem, Experiment editedItem) {

    }

    //Work in progress for deleting an experiment.
    /*
    exListView.setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position;

            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setMessage("Do you want to delete this item?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            experimentController.deleteExperiment(pos);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }

     */

}