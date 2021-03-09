package com.example.experimentify;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// package com.example.experimentify.ui.main;

/**
 * a search manager that accepts the entered keyword as a string, does some cleaning, queries the database and returns the experiments fitting the search result inside a list
 *
 * input: string keyword
 * return: list containing experiments that the query returned by search (case insensitive search)
 */

public class SearchKeyword {   // UML at https://raw.githubusercontent.com/CMPUT301W21T01/301-Group-Project-T01/main/doc/CMPUT%20301%20Project%20UML.png
    // has no "search" class or method.. will probably discuss how to change uml later
    private ArrayList<Experiment> experiments;       // our "experiments" are documents in the firebase...
    // so when we grab the documents from the firebase each time, we must initialize them with our Experiment class if we want to use ArrayList<Experiment>  (I will assume we will discuss this later)
    private String cleanedKeyword;

    //private DatabaseReference db;

    private FirebaseFirestore db;

    private ArrayList<String> experimentId;


    // database = FirebaseDatabase.getInstance().getReference();

    private HashMap<String, Map> unique_holder;
    public void Main(String keyword){

        cleanedKeyword = keyword.trim().toLowerCase(); // we should allow user to search case insensitive

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Experiments");

        //Query query = collectionReference.whereEqualTo("name", cleanedKeyword);
        //Query query1 = collectionReference.whereEqualTo("description", cleanedKeyword);

        //https://firebase.google.com/docs/firestore/query-data/queries#execute_a_query

        // init map to hold data and we can decide what to do with it later
        unique_holder = new HashMap<String, Map>();

        collectionReference
                .whereArrayContainsAny("name", Arrays.asList(cleanedKeyword))   //query line, can be combined and turned into complex queries (this one queries for name)
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
                                experimentId.add(document.getId()); // every doc from first query results will be unique
                                unique_holder.put(document.getId(),document.getData());


                                // in this case, we can deal with "duplicate" instances (because we have seperate queries for both name and description)
                                // the implementation of dealing with dupes can be changed later, what is shown is a basic one that works
                                // where we grab the documentID and add it to a list that we can keep track of

                            }
                        } else {
                            Log.d("Test holder", "Error getting documents: ", task.getException());
                        }
                    }
                });

        collectionReference
                .whereArrayContainsAny("description", Arrays.asList(cleanedKeyword))      //this one queries for description
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {       // same as above

                                String temp_id = document.getId();

                                Log.d("Test holder", temp_id + " => " + document.getData());
                                // work gets done here on how we want to filter, for now, an example will be shown as described above


                                if (experiments.indexOf(temp_id) == -1){
                                    experimentId.add(temp_id);
                                    unique_holder.put(temp_id,document.getData());

                                }  // now we will have a list of all the unique ids of experiments and a holder that holds all the unique experiments data,
                                // i am guessing we would now iterate over all the unique experiments from the query and intialize them into experiment objects
                            }
                        } else {
                            Log.d("Test holder", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

}
