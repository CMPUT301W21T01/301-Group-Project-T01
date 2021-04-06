package com.example.experimentify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    private TextView quartilesTV;
    private TextView medianTV;
    private TextView meanTV;
    private TextView stdDevTV;

    private double calculateMean() {
        return 0.0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();

        quartilesTV = findViewById(R.id.quartiles);
        medianTV = findViewById(R.id.median);
        meanTV = findViewById(R.id.mean);
        stdDevTV = findViewById(R.id.stdDev);









    }
}