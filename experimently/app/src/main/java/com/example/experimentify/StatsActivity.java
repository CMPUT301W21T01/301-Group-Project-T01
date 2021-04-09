package com.example.experimentify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * This class is an activity that displays stats for an experiment
 * The implementation imports DescriptiveStatistics found on:
 * http://commons.apache.org/proper/commons-math/javadocs/api-3.3/org/apache/commons/math3/stat/descriptive/DescriptiveStatistics.html
 * which accurately calculates the stats for us, and keeps the design of this activity straightforward(initialize the object, add the data, then simply call the methods for the statistics we want to show)
 * which is simple and keeps the code relatively shorter
 */
public class StatsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView quartilesTV;
    private TextView medianTV;
    private TextView meanTV;
    private TextView stdDevTV;
    private TextView Min;
    private TextView Max;
    private CollectionReference collectionReference;
    private Experiment exp;
    private ArrayList<Trial> trials;
    private FirebaseFirestore db;
    final String TAG = StatsActivity.class.getName();
    private String expType;
    private String expID;
    private DescriptiveStatistics stats;
    private ArrayList<Double> rawResultsDouble;
    private ArrayList<Integer> rawResultsInt;
    private ArrayList<String> rawResultsDate;

    private GraphView graph;
    private GraphView graph1;

    private int maxOccurrences;

    private MapView map;
    private GoogleMap gMap;


    public void setUI() throws ParseException {

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
        Collections.sort(rawResultsDouble);
        Collections.reverse(rawResultsDouble);

        graph = (GraphView) findViewById(R.id.results);
        graph1 = (GraphView) findViewById(R.id.resultsovertime);


        // all our database stores date in the same way
        DateFormat dateFormat = new SimpleDateFormat("YYYY/d/M");

        if (expType.equals("Measurement")) {

            // for the first graph for measurements we will show the value measured(x-axis) and frequency(y-axis)
            // we need to count the duplicates, this method used is created by  Deva44 (edited by Haboryme) on StackOverflow at https://stackoverflow.com/questions/8098601/java-count-occurrence-of-each-item-in-an-array

            Map<Double, Integer> dupes = new HashMap();

            for (Double x : rawResultsDouble) {

                if (!dupes.containsKey(x)) {
                    dupes.put(x, 1);
                    maxOccurrences = 1;
                } else {
                    dupes.put(x, dupes.get(x) + 1);
                    maxOccurrences = dupes.get(x);
                    Log.d("measurement, occurences: ", Double.toString(x)+ " : " + maxOccurrences);
                }
            }

            // iterate over our map and plot measurement on x-axis and frequency on y-axis
            Iterator it = dupes.entrySet().iterator();

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                series.appendData(new DataPoint((Integer) pair.getValue(),(Double) pair.getKey()), true, 50);
                it.remove();
            }
            graph.setTitle(exp.getDescription() + ": Results for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Frequency");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Measurement");

            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(stats.getMax()+100);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(maxOccurrences +5);
            //series.setSpacing(10);

            graph.addSeries(series);


            // do the second graph (over time)

            //LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[] { // place holder
                    new DataPoint(0, 1),
                    new DataPoint(1, 5),
                    new DataPoint(2, 3),
                    new DataPoint(3, 2),
                    new DataPoint(4, 6)
            });
/*
            for (String s : rawResultsDate){
                Date d1 = dateFormat.parse(s);
                series1.appendData(new DataPoint(d1, 15), true, 50);
            }
*/
            //new DataPoint(0, -1),
            graph1.setTitle(exp.getDescription() + ": Results for " + ((int) stats.getN()) + " Trials");
            // remember to set x and y axis
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Measurement");

            //graph1.getViewport().setScalable(true);
            //graph1.getViewport().setScrollable(true);

            /*
            graph1.getViewport().setYAxisBoundsManual(true);
            graph1.getViewport().setMinY(0);
            graph1.getViewport().setMaxY(stats.getMax()+100);

            graph1.getViewport().setXAxisBoundsManual(true);
            graph1.getViewport().setMinX(0);
            graph1.getViewport().setMaxX(maxOccurrences +5);
            */

            graph1.addSeries(series1);


        }
        else if(expType.equals("Integer")){

        }
        else if(expType.equals("Count")){

        }
        else if(expType.equals("Binomial")){

        }
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
                    GeoPoint location = doc.getGeoPoint("location");

                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng latlng = new LatLng(lat,lon);

                    Address loc = new Address(Locale.getDefault());
                    loc.setLatitude(lat);
                    loc.setLongitude(lon);
                    Location looc = new Location(loc);
                    Number result = (Number) doc.getData().get("result");


                    //TODO add filter for owner's ignored experiments


                    //TODO design pattern?
                    if (expType.equals("Measurement")) {
                        stats.addValue((double) result);
                        rawResultsDouble.add((Double) result);
                        //rawResultsDate.add(date);

                        MeasurementTrial newTrial = new MeasurementTrial(UID, expID, (double) result);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);

                    } else if (expType.equals("Integer")) {
                        stats.addValue(Integer.parseInt(String.valueOf(result)));
                        rawResultsInt.add(Integer.parseInt(String.valueOf(result)));


                        IntegerTrial newTrial = new IntegerTrial(UID, expID, (int) result);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);


                    } else if (expType.equals("Count")) {
                        stats.addValue(Integer.parseInt(String.valueOf(result)));
                        rawResultsInt.add(Integer.parseInt(String.valueOf(result)));

                        CountTrial newTrial = new CountTrial(UID, expID);
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        newTrial.setTrialLocation(looc);
                        trials.add(newTrial);

                    } else if (expType.equals("Binomial")) {
                        stats.addValue(Integer.parseInt(String.valueOf(result)));
                        rawResultsInt.add(Integer.parseInt(String.valueOf(result)));

                        BinomialTrial newTrial = new BinomialTrial(UID, expID, Integer.parseInt(String.valueOf(result)));
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        trials.add(newTrial);

                    }
                }
                System.out.println("geopoint2:" + trials.size() );
                for (int i = 0; i < trials.size(); i++) {
                    Location trial = trials.get(i).getTrialLocation();
                    double lat = trial.getLatitude();
                    double lon = trial.getLong();
                    LatLng latlng = new LatLng(lat, lon);

                    Log.d("123123", latlng.toString());
                    MarkerOptions mark = new MarkerOptions().position(latlng).title("Trial #" + i);
                    gMap.addMarker(mark);
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                }
                    try {
                    setUI();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
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
        stdDevTV = findViewById(R.id.stdDev3);
        Max = findViewById(R.id.Max);
        Min = findViewById(R.id.Min);

        map = findViewById(R.id.mapView2);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);


        rawResultsDouble = new ArrayList<Double>();

        rawResultsInt = new ArrayList<Integer>();

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                updateList(value, error);
                System.out.println("geopoint:" + trials.size() );

            }
        });

    }

//this shit dont work
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        }

    }


