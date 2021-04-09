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
    private Location looc = null;


    public void setUI() throws ParseException {

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
                    if(location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        LatLng latlng = new LatLng(lat, lon);
                        Address loc = new Address(Locale.getDefault());
                        loc.setLatitude(lat);
                        loc.setLongitude(lon);
                        Location looc = new Location(loc);

                    }
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
                        newTrial.setValue((result.doubleValue()));
                        newTrial.setTrialLocation(looc);
                        trials.add(newTrial);

                    } else if (expType.equals("Integer")) {
                        stats.addValue(Integer.parseInt(String.valueOf(result)));
                        rawResultsInt.add(Integer.parseInt(String.valueOf(result)));


                        IntegerTrial newTrial = new IntegerTrial(UID, expID, result.intValue());
                        newTrial.setDate(date);
                        newTrial.setTID(TID);
                        newTrial.setValue((result.intValue()));
                        newTrial.setTrialLocation(looc);
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
                        newTrial.setTrialLocation(looc);
                        trials.add(newTrial);

                    }
                }
                System.out.println("geopoint2:" + trials.size() );
                for (int i = 0; i < trials.size(); i++) {
                    Location trial = trials.get(i).getTrialLocation();
                    if(trial != null) {
                        double lat = trial.getLatitude();
                        double lon = trial.getLong();
                        LatLng latlng = new LatLng(lat, lon);

                        Log.d("123123", latlng.toString());
                        MarkerOptions mark = new MarkerOptions().position(latlng).title("Trial #" + (i + 1));
                        gMap.addMarker(mark);
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                        gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    }
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


        map = findViewById(R.id.mapView2);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);


        rawResultsDouble = new ArrayList<Double>();

        rawResultsInt = new ArrayList<Integer>();

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                updateList(value, error);
                System.out.println("geopoint:" + trials.size());

            }
        });
        if (exp.isLocationRequired() == false) { map.setVisibility(View.GONE);}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

    }

}


