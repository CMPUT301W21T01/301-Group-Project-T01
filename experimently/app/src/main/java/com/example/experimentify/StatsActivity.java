package com.example.experimentify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * This class is an activity that displays stats for an experiment
 * The implementation imports DescriptiveStatistics found on:
 * http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/stat/descriptive/DescriptiveStatistics.html
 * which accurately calculates the stats for us, and keeps the design of this activity straightforward(initialize the object, add the data, then simply call the methods for the statistics we want to show)
 * which is simple and keeps the code relatively shorter
 */
public class StatsActivity extends AppCompatActivity {

    private TextView quartilesTV;
    private TextView medianTV;
    private TextView meanTV;
    private TextView stdDevTV;
    private CollectionReference collectionReference;
    private Experiment exp;
    private ArrayList<Trial> trials;
    private FirebaseFirestore db;
    final String TAG = StatsActivity.class.getName();
    private String expType;
    private String expID;
    private DescriptiveStatistics stats;


    public void setUI() {

        double mean = stats.getMean();
        meanTV.setText("Mean: " + Double.toString(mean));
        double median = stats.getPercentile(50);
        medianTV.setText("Median: " + Double.toString(median));
        double stdDev = stats.getStandardDeviation();
        stdDevTV.setText("Standard Deviation: " + Double.toString(stdDev));
        //double[] quartiles = calculateQuartiles();
        String quartileString = "";
        quartileString += Double.toString(stats.getPercentile(25));
        quartileString += ", ";
        quartileString += Double.toString(stats.getPercentile(50));
        quartileString += ", ";
        quartileString += Double.toString(stats.getPercentile(75));


        quartilesTV.setText(this.getResources().getString(R.string.quartiles_header) + quartileString);

    }

    private void updateList(QuerySnapshot value, FirebaseFirestoreException error) {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                trials.clear();
                expType = exp.getExpType();
                expID = exp.getUID();
                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String TID = doc.getString("TID");
                    String UID = doc.getString("UID");
                    String date = doc.getString("date");
                    Number result = (Number) doc.getData().get("result");


                    //TODO add filter for owner's ignored experiments


                    //TODO design pattern?
                    if (expType.equals("Measurement")) {
                        stats.addValue((double) result);


                        MeasurementTrial newTrial = new MeasurementTrial(UID, expID, (double) result);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);
                    } else if (expType.equals("Integer")) {
                        stats.addValue((int) result);


                        IntegerTrial newTrial = new IntegerTrial(UID, expID, (int) result);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);
                    } else if (expType.equals("Count")) {
                        CountTrial newTrial = new CountTrial(UID, expID);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);
                    } else if (expType.equals("Binomial")) {
                        stats.addValue((int) result);

                        BinomialTrial newTrial = new BinomialTrial(UID, expID, (int) result);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);
                    }
                }
                setUI();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        db = FirebaseFirestore.getInstance();

        stats = new DescriptiveStatistics();


        Intent intent = getIntent();

        exp = intent.getParcelableExtra("experiment");
        collectionReference = db.collection("Experiments").document(exp.getUID()).collection("Trials");
        trials = new ArrayList<Trial>();

        quartilesTV = findViewById(R.id.quartiles);
        medianTV = findViewById(R.id.median);
        meanTV = findViewById(R.id.mean);
        stdDevTV = findViewById(R.id.stdDev);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                updateList(value, error);
            }
        });

    }
}