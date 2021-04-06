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

/**
 * This class is an activity that displays stats for an experiment
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

    /**
     * This method calculates the mean of the trials for the experiment.
     * @return returns a double that represents mean of the trials
     */
    private double calculateMean() {
        double sum = 0.0;
        if (!trials.isEmpty()) {
            for (Trial trial : trials) {
                sum += (double) trial.getValue(); //TODO fix error with casting int to double
            }
            return sum/trials.size();
        }
        else {
            return 0.0;
        }
    }

    //TODO check that this method calculates median correctly
    /**
     * This method calculates the median of the trials for the experiment.
     * @return returns a double that represents median of the trials
     */
    public double calculateMedian() {

        if (!trials.isEmpty()) {
            int size = trials.size();
            Collections.sort(trials, new Comparator<Trial>() {
                @Override
                public int compare(Trial o1, Trial o2) {
                    //https://stackoverflow.com/a/15111857
                    BigDecimal b1 = new BigDecimal((double) o1.getValue());
                    BigDecimal b2 = new BigDecimal((double) o2.getValue());
                    return b1.compareTo(b2);
                }
            });


            if (size % 2 == 0) {
                // When size is even
                return (double) trials.get(size / 2).getValue();
            } else {
                // When size is odd
                return ((double) trials.get((size - 1) / 2).getValue() + (double) trials.get((size + 1) / 2).getValue()) / 2;

            }
        }
        else {
            return 0.0;
        }


    }

    //TODO calculate standard deviation
    public double calculateStdDev() {
        return 0.0; //placeholder
    }

    //TODO calculate the list of quartiles
    public double[] calculateQuartiles() {
        //quartiles should be empty at start, need values below as placeholder to avoid crash
        double [] quartiles = {1.0, 2.0, 3.0};
        return quartiles;
    }

    public void setUI() {
        double mean = calculateMean();
        double median = calculateMedian();
        double stdDev = calculateStdDev();
        double[] quartiles = calculateQuartiles();
        String quartileString = "";

        meanTV.setText(this.getResources().getString(R.string.mean_header) + Double.toString(mean));
        medianTV.setText(this.getResources().getString(R.string.median_header) + Double.toString(median));
        stdDevTV.setText(this.getResources().getString(R.string.std_dev_header) + Double.toString(stdDev));


        // Generates string to display for quartiles
        //TODO throw exception if array size is not == 4
        for (int i = 0; i < 3; i++) {
            quartileString += quartiles[i];
            if (i != 2) {
                quartileString += ", ";
            }
        }

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
                        MeasurementTrial newTrial = new MeasurementTrial(UID, expID, (double) result);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);
                    } else if (expType.equals("Integer")) {
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