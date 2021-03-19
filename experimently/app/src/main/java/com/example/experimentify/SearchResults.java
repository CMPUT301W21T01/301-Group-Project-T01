package com.example.experimentify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * the best practice that ensures the program will continue execution immediately after getting data from the query is to pass the list
 */
public class SearchResults extends AppCompatActivity {

    private ExperimentController expController;
    private ExperimentListAdapter experimentAdapter;
    private ListView exListView;
    private ArrayList<Experiment> experimentList;
    FirebaseFirestore db;
    private Intent intent;
    private String keyword;
    private String cleanedKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //get db and reference to Experiments collection
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");
        //find listview ui element
        exListView = findViewById(R.id.exListView);



        // get the keyword stored inside the intent
        intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            keyword = extras.getString("keyword");
        }

        experimentList = new ArrayList<Experiment>();
        expController = new ExperimentController(this);


        cleanedKeyword = keyword.trim().toLowerCase(); // we should allow user to search case insensitive

        //set up adapter
        experimentAdapter = new ExperimentListAdapter(this, experimentList);
        exListView.setAdapter(experimentAdapter);

        // onclick listener that lets the user view the experiment when clicked
        exListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                expController.viewExperiment(SearchResults.this,experimentList.get(position));
            }
        });

        collectionReference
                .whereArrayContainsAny("searchable", Arrays.asList(cleanedKeyword))//query line, can be combined and turned into complex queries (this one queries for name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // when you finish getting the data from the firebase
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {     // querysnapshot contains 0..* document snapshots
                        if (task.isSuccessful()) {

                            // https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/QueryDocumentSnapshot

                            for (QueryDocumentSnapshot document : task.getResult()) {                      // iterate over query results of the document
                                Log.d("Test holder", document.getId() + " => " + document.getData());                // this for each document from our query result where we can filter and extract results
                                // the idea is here we use document snapshot methods such as get(String field) or getData()(returns fields of doc as a map) and we can
                                // filter, then grab and use all the data we could need from the firebase ()
                                // experimentId.add(document.getId());

                                // every document from first query results will be unique
                                Log.d("test", String.valueOf(document.getData().get("EID")));
                                String description  = (String)  document.getData().get("description");
                                String region       = (String)  document.getData().get("region");
                                Long minTrials      = (Long)    document.getData().get("minTrials");
                                String date         = (String)  document.getData().get("date");
                                boolean locationReq = (boolean) document.getData().get("locationRequired");
                                Experiment temp_experiment = new Experiment(description, region, minTrials, date, locationReq);
                                temp_experiment.setUID(String.valueOf(document.getData().get("EID")));
                                experimentList.add(temp_experiment);
                            }
                        } else {
                            Log.d("Test holder", "Error getting documents: ", task.getException());
                        }
                        experimentAdapter.notifyDataSetChanged();
                    }
                });

        getSupportActionBar().setTitle("Search Results for: " + keyword); // title the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button to main activity




    }




}