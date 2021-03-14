package com.example.experimentify;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;



/**
 * This activity is a UI in which the user can see a list of published experiments
 * and add new experiments to the list.
 */
public class MainActivity extends AppCompatActivity implements AddExpFragment.OnFragmentInteractionListener, ExpOptionsFragment.OnFragmentInteractionListener {
    private ExperimentController experimentController;
    private ExperimentListAdapter experimentAdapter;
    private ListView exListView;
    private FloatingActionButton showAddExpUiButton;
    private FloatingActionButton qrScanner;
    private ArrayList<Experiment> experimentList;
    final String TAG = MainActivity.class.getName();
    FirebaseFirestore db;

    /**
     * This method shows the fragment that allows a user to create a new experiment.
     */
    private void showAddExpUi() {
        new AddExpFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
    }

    /**
     * This method shows the fragment that gives users options for the experiment they long clicked on.
     */
    private void showExpOptionsUI(Experiment experiment) {
        ExpOptionsFragment fragment = ExpOptionsFragment.newInstance(experiment);
        fragment.show(getSupportFragmentManager(), "EXP_OPTIONS");

    }

    /**
     * This method adds an experiment to the database.
     * @param experiment
     */
    private void addExperiment(Experiment experiment) {
        experimentController.addExperimentToDB(experiment, db);
    }

    private void delExperiment() {
        //pass
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        exListView = findViewById(R.id.exListView);
        showAddExpUiButton = findViewById(R.id.showAddExpUiButton);
        qrScanner = findViewById(R.id.qrScanner);

        //ExperimentListAdapter experimentAdapter = new ExperimentListAdapter(this, )
        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();
        exListView.setAdapter(experimentController.getAdapter());

        showAddExpUiButton.setOnClickListener((v) -> {
            showAddExpUi();
        });

        qrScanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, qrScanActivity.class);
                startActivity(intent);

            }
        });

        exListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                Experiment experiment = experimentController.getAdapter().getItem(pos);
                showExpOptionsUI(experiment);
                return true;
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experimentController.getExperiments().clear();
                for (QueryDocumentSnapshot doc : value){
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String description  = (String)  doc.getData().get("description");
                    String region       = (String)  doc.getData().get("region");
                    Long minTrials      = (Long)    doc.getData().get("minTrials");
                    String date         = (String)  doc.getData().get("date");
                    boolean locationReq = (boolean) doc.getData().get("locationRequired");
                    experimentList.add(new Experiment(description, region, minTrials, date, locationReq));
                }
                experimentController.getAdapter().notifyDataSetChanged();
            }
        });

    }

    //AddExpFragment
    @Override
    public void onOkPressed(Experiment newExp) {
        addExperiment(newExp);
    }

    @Override
    public void onDeletePressed(Experiment current) {

    }

    @Override
    public void editItem(Experiment ogItem, Experiment editedItem) {

    }

    //ExpOptionsFragment
    @Override
    public void onOkPressed(Experiment newExp, Boolean edit) {

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