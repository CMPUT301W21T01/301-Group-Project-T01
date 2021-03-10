package com.example.experimentify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements AddExpFragment.OnFragmentInteractionListener {
    private ExperimentController experimentController;
    private ListView exListView;
    private FloatingActionButton showAddExpUiButton;


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

        exListView = findViewById(R.id.exListView);
        showAddExpUiButton = findViewById(R.id.showAddExpUiButton);

        experimentController = new ExperimentController(this);
        exListView.setAdapter(experimentController.getAdapter());


        showAddExpUiButton.setOnClickListener((v) -> {
            showAddExpUi();
        });


    }

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