
package com.example.experimentify;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Date;

/**
 * This is the stats activity where we should hard stats (quartiles, median, mean, std. dev, min, max,
 * and the show graphs (histogram (frequency - value), datapoint(frequency - value) , and data point over time(value - date))
 * our implementation uses DescriptiveStatistics found on: http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/stat/descriptive/DescriptiveStatistics.html
 * and we make use of jjoe64's GraphView library for plotting and drawing our graphs (found on: https://github.com/jjoe64/GraphView)
 */
public class graphsActivity extends AppCompatActivity {
    // Database Elements
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    // UI Elements
    private TextView quartilesTV;
    private TextView medianTV;
    private TextView meanTV;
    private TextView stdDevTV;
    private TextView Min;
    private TextView Max;
    private GraphView graph;
    private GraphView graph1;
    private GraphView graph2;

    // Logic Elements
    private DescriptiveStatistics stats;
    private ArrayList<Double> rawResultsDouble;
    private ArrayList<Integer> rawResultsInt;
    private ArrayList<String> rawResultsDate;
    private Experiment exp;
    private int maxOccurrences;
    private int dateOccurrences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphs_activity);
        Intent intent = getIntent();
        // get our intent and set up database connection
        exp = intent.getParcelableExtra("experiment");

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Experiments").document(exp.getUID()).collection("Trials");

        // set up UI elements
        quartilesTV = findViewById(R.id.quartiles);
        medianTV = findViewById(R.id.median);
        meanTV = findViewById(R.id.mean);
        stdDevTV = findViewById(R.id.stdDev3);
        Max = findViewById(R.id.Max);
        Min = findViewById(R.id.Min);

        // set up logic elements
        rawResultsDouble = new ArrayList<Double>();
        rawResultsInt = new ArrayList<Integer>();
        rawResultsDate = new ArrayList<String>();

        stats = new DescriptiveStatistics();

        // We will query ONCE for the data and display it (the user can go back and click the stats activity again for updated results if any were added during the time frame)

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {  // this iterates over the entirety of all the trials in our FireStore

                        String date       = (String)  doc.getData().get("date");

                        if (exp.getExpType().equals("Measurement")){  // our measurement is in doubles

                            Double result  = (Double)  doc.getData().get("result");

                            stats.addValue(result);
                            rawResultsDouble.add(result);
                            rawResultsDate.add(date);
                        }
                        else if(exp.getExpType().equals("Integer")){ // integer and counts are obviously ints

                            Long result = (Long) doc.getData().get("result");

                            stats.addValue(result);
                            rawResultsInt.add(result.intValue());
                            rawResultsDate.add(date);
                        }
                        else if (exp.getExpType().equals("Count")){
                            Long result = (Long) doc.getData().get("result");
                            stats.addValue(result);
                            rawResultsInt.add(result.intValue());
                            rawResultsDate.add(date);
                        }
                        else if (exp.getExpType().equals("Binomial")){
                            Long result = (Long) doc.getData().get("result");
                            stats.addValue(result);
                            rawResultsInt.add(result.intValue());
                            rawResultsDate.add(date);
                        }
                    }
                    // after we have all our data, now display it
                    setUI();
                } else {
                    Log.d("firebase couldn't get documents", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public void setUI(){

        // set the stats that are required in the specification
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

        String minText = "";
        String maxText = "";
        minText += Double.toString(stats.getMin());
        maxText += Double.toString(stats.getMax());

        Min.setText("Min: " + minText);
        Max.setText("Max: " + maxText);


        // now create our graphs
        graph = (GraphView) findViewById(R.id.results);
        graph1 = (GraphView) findViewById(R.id.resultspoint);
        graph2 = (GraphView) findViewById(R.id.resultsdate);


        //all graphs need dates, (# of submitted trials on dates)
        // we need to count the duplicates of dates, this method used is created by  Deva44 (edited by Haboryme) on StackOverflow at https://stackoverflow.com/questions/8098601/java-count-occurrence-of-each-item-in-an-array
        Map<Date, Integer> date_dupes = new HashMap();
        dateOccurrences = 0;
        for(String s:rawResultsDate){

            // now convert the strings into dates, and put them into a tree map later (which will the sort keys which are the dates in ascending order)

            Date d1 = null;

            try {
                d1 = new SimpleDateFormat("yyyy/MM/dd").parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(!date_dupes.containsKey(d1)){
                date_dupes.put(d1,1);
                if(dateOccurrences == 0) {
                    dateOccurrences += 1;
                }
            }else{
                date_dupes.put(d1, date_dupes.get(d1)+1);
                if(date_dupes.get(d1)>dateOccurrences) {
                    dateOccurrences = date_dupes.get(d1);
                }
            }
        }

        // now convert the strings into dates, and put them into a tree map (which will the sort keys which are the dates in ascending order)
        Map<Date, Integer> dateMap = new TreeMap<>(date_dupes);
        Iterator dateit = dateMap.entrySet().iterator();



        //depending on the type of experiment, we will graph differently
        if (exp.getExpType().equals("Measurement")){
            Map<Double, Integer> result_dupes = new HashMap();

            // for the first graph for measurements we will show the value measured(x-axis) and frequency(y-axis)
            // we need to count the duplicates, this method used is created by  Deva44 (edited by Haboryme) on StackOverflow at https://stackoverflow.com/questions/8098601/java-count-occurrence-of-each-item-in-an-array
            maxOccurrences = 0;
            for (Double x : rawResultsDouble) {

                if (!result_dupes.containsKey(x)) {
                    result_dupes.put(x, 1);
                    if(maxOccurrences == 0) {
                        maxOccurrences += 1;
                    }
                } else {
                    result_dupes.put(x, result_dupes.get(x) + 1);
                    if(result_dupes.get(x)>maxOccurrences) {
                        maxOccurrences = result_dupes.get(x);
                    }
                    Log.d("measurement, occurences: ", Double.toString(x)+ " : " + maxOccurrences);
                }
            }
            // tree map sorts the key-value pairs by the keys in ascending order (plotting the data points needs the x-values in ascending order)
            Map<Double, Integer> treeMap = new TreeMap<>(result_dupes);
            Iterator it = treeMap.entrySet().iterator();

            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();
            PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                series.appendData(new DataPoint((Double) pair.getKey(),(Integer) pair.getValue()), true, 100);
                series1.appendData(new DataPoint((Double) pair.getKey(),(Integer) pair.getValue()), true, 100);
                it.remove();
            }

            graph.setTitle(exp.getDescription() + ": Histogram for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Measurement");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);


            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(stats.getMax() + 2); //padding to the right

            series.setSpacing(0);

            graph.addSeries(series);

            // SECOND GRAPH SETTINGS

            graph1.setTitle(exp.getDescription() + ": DataPoints for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("Measurement");
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph1.getViewport().setScalable(true);
            graph1.getViewport().setScrollable(true);


            graph1.getViewport().setYAxisBoundsManual(true);
            graph1.getViewport().setMinY(0);
            graph1.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(0);
            graph1.getViewport().setMaxX(stats.getMax() + 2); //padding to the right


            graph1.addSeries(series1);

            //THIRD GRAPH
            PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>();

            Date firstDate = null;

            Date lastDate = null;

            int i = 0;

            while (dateit.hasNext()) {
                Map.Entry pair = (Map.Entry) dateit.next();


                series2.appendData(new DataPoint((Date) pair.getKey(), (Integer) pair.getValue()), true, 100);

                if(i == 0){
                    firstDate = (Date) pair.getKey();
                }

                if (!dateit.hasNext())
                {
                    lastDate = (Date) pair.getKey();
                }
                i += 1;
                dateit.remove();
            }

            graph2.setTitle(exp.getDescription() + ": Date Points for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph2.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph2.getViewport().setScalable(true);
            graph2.getViewport().setScrollable(true);


            graph2.getViewport().setYAxisBoundsManual(true);
            graph2.getViewport().setMinY(0);
            graph2.getViewport().setMaxY(dateOccurrences + 2); //padding on top

            //
            graph2.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphsActivity.this));
            graph2.getGridLabelRenderer().setNumHorizontalLabels(4);

            graph2.getGridLabelRenderer().setHorizontalLabelsAngle(150);
            graph2.getGridLabelRenderer().setLabelHorizontalHeight(100);


            // use the "first date" and "last date" to set our x-axis
            graph2.getViewport().setMinX(firstDate.getTime());
            graph2.getViewport().setMaxX(lastDate.getTime() + 8 * 24 * 60 * 60 * 1000);
            graph2.getViewport().setXAxisBoundsManual(true);

            graph2.addSeries(series2);

        }
        else if(exp.getExpType().equals("Integer")){
            Map<Integer, Integer> result_dupes = new HashMap();
            // we need to shown the integers and the frequency, (same as measurement)
            maxOccurrences = 0;
            for (Integer x : rawResultsInt) {

                if (!result_dupes.containsKey(x)) {
                    result_dupes.put(x, 1);
                    if (maxOccurrences == 0) {
                        maxOccurrences += 1;
                    }
                } else {
                    result_dupes.put(x, result_dupes.get(x) + 1);
                    if (result_dupes.get(x) > maxOccurrences) {
                        maxOccurrences = result_dupes.get(x);
                    }
                    Log.d("measurement, occurences: ", x + " : " + maxOccurrences);
                }
            }

            Map<Integer, Integer> treeMap = new TreeMap<>(result_dupes);
            Iterator it = treeMap.entrySet().iterator();

            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();
            PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                series.appendData(new DataPoint((Integer) pair.getKey(),(Integer) pair.getValue()), true, 100);
                series1.appendData(new DataPoint((Integer) pair.getKey(),(Integer) pair.getValue()), true, 100);
                it.remove();
            }

            graph.setTitle(exp.getDescription() + ": Histogram for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Integers");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);


            graph.getViewport().setYAxisBoundsManual(true);  // y-axis is frequency, x is the integer
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(stats.getMin());
            graph.getViewport().setMaxX(stats.getMax() + 2); //padding to the right

            series.setSpacing(0);

            graph.addSeries(series);


            // SECOND GRAPH(DATA POINT) SETTINGS

            graph1.setTitle(exp.getDescription() + ": DataPoints for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("Integers");
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph1.getViewport().setScalable(true);
            graph1.getViewport().setScrollable(true);


            graph1.getViewport().setYAxisBoundsManual(true);
            graph1.getViewport().setMinY(0);
            graph1.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(stats.getMin());
            graph1.getViewport().setMaxX(stats.getMax() + 2); //padding to the right


            graph1.addSeries(series1);

            //THIRD GRAPH (DATE AND FREQUENCY)
            PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>();

            Date firstDate = null;

            Date lastDate = null;

            int i = 0;

            while (dateit.hasNext()) {
                Map.Entry pair = (Map.Entry) dateit.next();

                series2.appendData(new DataPoint((Date) pair.getKey(), (Integer) pair.getValue()), true, 100);

                if(i == 0){
                    firstDate = (Date) pair.getKey();
                }

                if (!dateit.hasNext())
                {
                    lastDate = (Date) pair.getKey();
                }
                i += 1;
                dateit.remove();
            }

            graph2.setTitle(exp.getDescription() + ": Date Points for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph2.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph2.getViewport().setScalable(true);
            graph2.getViewport().setScrollable(true);


            graph2.getViewport().setYAxisBoundsManual(true);
            graph2.getViewport().setMinY(0);
            graph2.getViewport().setMaxY(dateOccurrences + 2); //padding on top

            //
            graph2.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphsActivity.this));
            graph2.getGridLabelRenderer().setNumHorizontalLabels(4);

            graph2.getGridLabelRenderer().setHorizontalLabelsAngle(150);
            graph2.getGridLabelRenderer().setLabelHorizontalHeight(100);

            // use the "first date" and "last date" to set our x-axis
            graph2.getViewport().setMinX(firstDate.getTime());
            graph2.getViewport().setMaxX(lastDate.getTime() + 8 * 24 * 60 * 60 * 1000);
            graph2.getViewport().setXAxisBoundsManual(true);

            graph2.addSeries(series2);


        }
        else if(exp.getExpType().equals("Count")){
            Map<Integer, Integer> result_dupes = new HashMap();
            // we need to shown the integers and the frequency, (same as measurement)
            maxOccurrences = 0;
            for (Integer x : rawResultsInt) {

                if (!result_dupes.containsKey(x)) {
                    result_dupes.put(x, 1);
                    if (maxOccurrences == 0) {
                        maxOccurrences += 1;
                    }
                } else {
                    result_dupes.put(x, result_dupes.get(x) + 1);
                    if (result_dupes.get(x) > maxOccurrences) {
                        maxOccurrences = result_dupes.get(x);
                    }
                    Log.d("measurement, occurences: ", x + " : " + maxOccurrences);
                }
            }

            Map<Integer, Integer> treeMap = new TreeMap<>(result_dupes);
            Iterator it = treeMap.entrySet().iterator();

            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();
            PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                series.appendData(new DataPoint((Integer) pair.getKey(),(Integer) pair.getValue()), true, 100);
                series1.appendData(new DataPoint((Integer) pair.getKey(),(Integer) pair.getValue()), true, 100);
                it.remove();
            }

            graph.setTitle(exp.getDescription() + ": Histogram for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph.getGridLabelRenderer().setHorizontalAxisTitle("One-Trial(1 Count)");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Count");

            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);

            graph.getViewport().setYAxisBoundsManual(true);  // y-axis is frequency, x is the count
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0); // padding to the left
            graph.getViewport().setMaxX(3); //padding to the right

            series.setSpacing(0);

            graph.addSeries(series);


            // SECOND GRAPH(DATA POINT) SETTINGS

            graph1.setTitle(exp.getDescription() + ": DataPoints for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("One-Trial(1 Count)");
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Count");

            graph1.getViewport().setScalable(true);
            graph1.getViewport().setScrollable(true);


            graph1.getViewport().setYAxisBoundsManual(true);
            graph1.getViewport().setMinY(0);
            graph1.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(0);
            graph1.getViewport().setMaxX(2); //padding to the right


            graph1.addSeries(series1);

            //THIRD GRAPH (DATE AND FREQUENCY)
            PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>();

            Date firstDate = null;

            Date lastDate = null;

            int i = 0;

            while (dateit.hasNext()) {
                Map.Entry pair = (Map.Entry) dateit.next();

                series2.appendData(new DataPoint((Date) pair.getKey(), (Integer) pair.getValue()), true, 100);

                if(i == 0){
                    firstDate = (Date) pair.getKey();
                }

                if (!dateit.hasNext())
                {
                    lastDate = (Date) pair.getKey();
                }
                i += 1;
                dateit.remove();
            }

            graph2.setTitle(exp.getDescription() + ": Date Points for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph2.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph2.getViewport().setScalable(true);
            graph2.getViewport().setScrollable(true);


            graph2.getViewport().setYAxisBoundsManual(true);
            graph2.getViewport().setMinY(0);
            graph2.getViewport().setMaxY(dateOccurrences + 2); //padding on top

            //
            graph2.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphsActivity.this));
            graph2.getGridLabelRenderer().setNumHorizontalLabels(4);

            graph2.getGridLabelRenderer().setHorizontalLabelsAngle(150);
            graph2.getGridLabelRenderer().setLabelHorizontalHeight(100);

            // use the "first date" and "last date" to set our x-axis
            graph2.getViewport().setMinX(firstDate.getTime());
            graph2.getViewport().setMaxX(lastDate.getTime() + 8 * 24 * 60 * 60 * 1000);
            graph2.getViewport().setXAxisBoundsManual(true);

            graph2.addSeries(series2);

        }
        else if(exp.getExpType().equals("Binomial")){
            Map<Integer, Integer> result_dupes = new HashMap();
            // we need to shown the pass fail
            maxOccurrences = 0;
            for (Integer x : rawResultsInt) {

                if (!result_dupes.containsKey(x)) {
                    result_dupes.put(x, 1);
                    if (maxOccurrences == 0) {
                        maxOccurrences += 1;
                    }
                } else {
                    result_dupes.put(x, result_dupes.get(x) + 1);
                    if (result_dupes.get(x) > maxOccurrences) {
                        maxOccurrences = result_dupes.get(x);
                    }
                    Log.d("measurement, occurences: ", x + " : " + maxOccurrences);
                }
            }

            Map<Integer, Integer> treeMap = new TreeMap<>(result_dupes);
            Iterator it = treeMap.entrySet().iterator();

            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();
            PointsGraphSeries<DataPoint> series1 = new PointsGraphSeries<DataPoint>();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                series.appendData(new DataPoint((Integer) pair.getKey(),(Integer) pair.getValue()), true, 100);
                series1.appendData(new DataPoint((Integer) pair.getKey(),(Integer) pair.getValue()), true, 100);
                it.remove();
            }

            graph.setTitle(exp.getDescription() + ": Histogram for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Fail/Pass (0 for Fail, 1 for Pass)");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);


            graph.getViewport().setYAxisBoundsManual(true);  // y-axis is frequency, x is the true / false (1 or 0)
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0); // padding to the left
            graph.getViewport().setMaxX(2); //padding to the right

            series.setSpacing(5);

            graph.getGridLabelRenderer().setNumHorizontalLabels(2);
            graph.getGridLabelRenderer().setHumanRounding(false,true);

            graph.addSeries(series);


            // SECOND GRAPH(DATA POINT) SETTINGS

            graph1.setTitle(exp.getDescription() + ": DataPoints for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("Fail/Pass (0 for Fail, 1 for Pass)");
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Frequency");


            graph1.getViewport().setScalable(true);
            graph1.getViewport().setScrollable(true);


            graph1.getViewport().setYAxisBoundsManual(true);
            graph1.getViewport().setMinY(0);
            graph1.getViewport().setMaxY(maxOccurrences + 2); //padding on top

            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(0);
            graph1.getViewport().setMaxX(2); //padding to the right

            graph1.getGridLabelRenderer().setNumHorizontalLabels(2);
            graph1.getGridLabelRenderer().setHumanRounding(false,true);




            graph1.addSeries(series1);

            //THIRD GRAPH (DATE AND FREQUENCY)
            PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<DataPoint>();

            Date firstDate = null;

            Date lastDate = null;

            int i = 0;

            while (dateit.hasNext()) {
                Map.Entry pair = (Map.Entry) dateit.next();

                series2.appendData(new DataPoint((Date) pair.getKey(), (Integer) pair.getValue()), true, 100);

                if(i == 0){
                    firstDate = (Date) pair.getKey();
                }

                if (!dateit.hasNext())
                {
                    lastDate = (Date) pair.getKey();
                }
                i += 1;
                dateit.remove();
            }

            graph2.setTitle(exp.getDescription() + ": Date Points for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph2.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

            graph2.getViewport().setScalable(true);
            graph2.getViewport().setScrollable(true);


            graph2.getViewport().setYAxisBoundsManual(true);
            graph2.getViewport().setMinY(0);
            graph2.getViewport().setMaxY(dateOccurrences + 2); //padding on top

            //
            graph2.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graphsActivity.this));
            graph2.getGridLabelRenderer().setNumHorizontalLabels(4);

            graph2.getGridLabelRenderer().setHorizontalLabelsAngle(150);
            graph2.getGridLabelRenderer().setLabelHorizontalHeight(100);

            // use the "first date" and "last date" to set our x-axis
            graph2.getViewport().setMinX(firstDate.getTime());
            graph2.getViewport().setMaxX(lastDate.getTime() + 8 * 24 * 60 * 60 * 1000);
            graph2.getViewport().setXAxisBoundsManual(true);


            graph2.addSeries(series2);



        }


    }

}