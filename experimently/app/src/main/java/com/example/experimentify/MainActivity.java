package com.example.experimentify;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private ExperimentController experimentController;
    private ListView exListView;
    private FloatingActionButton showAddExpUiButton;


    //Shows fragment for creating new experiment
    private void showAddExpUi() {
        new AddExpFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
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
}