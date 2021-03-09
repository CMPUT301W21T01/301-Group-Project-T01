package com.example.experimentify;

import android.location.Location;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {
    private ExperimentController experimentController;
    private ListView exListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exListView = findViewById(R.id.exListView);

        experimentController = new ExperimentController(this);
        exListView.setAdapter(experimentController.getAdapter());







    }
}