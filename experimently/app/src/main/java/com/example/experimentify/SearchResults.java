package com.example.experimentify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * SearchResults Activity that displays the results of searching a specific keyword of matching experiments
 * that will display a listview of Experiments that match with the search (searchable field in db)
 */
public class SearchResults extends AppCompatActivity implements ExpOptionsFragment.OnFragmentInteractionListener, UserOptionsFragment.OnFragmentInteractionListener {

    private ExperimentController expController;
    private ExperimentListAdapter experimentAdapter;

    private ListView exListView;
    private ArrayList<Experiment> experimentList;

    private UserListAdapter userAdapter;
    private ArrayList<User> userList;

    FirebaseFirestore db;
    public static final String PREFS_NAME = "PrefsFile";
    private Intent intent;
    private String keyword;
    private String cleanedKeyword;
    private String searchType;
    private SharedPreferences settings;
    private User user;

    /**
     * This method shows the fragment that gives users options for the experiment they long clicked on.
     * @param experiment experiment whose options will be edited
     */
    private void showExpOptionsUI(Experiment experiment, User currentUser) {
        String localUID = settings.getString("uid","0");
        ExpOptionsFragment fragment = ExpOptionsFragment.newInstance(experiment, localUID, currentUser);
        fragment.show(getSupportFragmentManager(), "EXP_OPTIONS");
    }

    /**
     * This method shows the fragment that gives users options for the experiment they long clicked on.
     * @param newUser experiment whose options will be edited
     */
    private void showUserOptionsUI(User newUser, User currentUser) {
        String localUID = settings.getString("uid","0");
        UserOptionsFragment fragment = UserOptionsFragment.newInstance(newUser, localUID, currentUser);
        fragment.show(getSupportFragmentManager(), "EXP_OPTIONS");
    }

    /**
     * This method deletes an experiment from the database
     * @param expToDel experiment to delete
     */
    private void delExperiment(Experiment expToDel) {
        expController.deleteExperimentFromDB(expToDel, db);
    }

    /**
     * This method edits existing experiments in the database
     * @param expToEdit experiment to edit
     */
    private void editExperiment(Experiment expToEdit) {
        expController.editExperimentToDB(expToEdit, db);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //get db and reference to Experiments collection
        db = FirebaseFirestore.getInstance();
        //find listview ui element
        exListView = findViewById(R.id.exListView);

        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);



        // get the keyword stored inside the intent
        intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            keyword = extras.getString("keyword");
            user = (User) extras.getSerializable("user");
            searchType = extras.getString("searchType");
        }
    if(searchType.equals("Experiment")) {
        CollectionReference collectionReference = db.collection("Experiments");
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

                expController.viewExperiment(SearchResults.this, experimentList.get(position));
            }
        });

        exListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                Experiment experiment = experimentList.get(pos);
                showExpOptionsUI(experiment, user);
                return true;
            }
        });


        // the search results should be a one time thing and do not auto update
        collectionReference
                .whereArrayContainsAny("searchable", Arrays.asList(cleanedKeyword))//query line, can be combined and turned into complex queries (this one queries for name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // when you finish getting the data from the firebase
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {     // querysnapshot contains 0..* document snapshots
                        if (task.isSuccessful()) {

                            String localUID = settings.getString("uid", "0");
                            // https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/QueryDocumentSnapshot
                            for (QueryDocumentSnapshot doc : task.getResult()) {                      // iterate over query results of the document
                                Log.d("Test holder", doc.getId() + " => " + doc.getData());                // this for each document from our query result where we can filter and extract results
                                // the idea is here we use document snapshot methods such as get(String field) or getData()(returns fields of doc as a map) and we can
                                // filter, then grab and use all the data we could need from the firebase ()
                                // experimentId.add(document.getId());

                                // every document from first query results will be unique
                                String description = (String) doc.getData().get("description");
                                String region = (String) doc.getData().get("region");
                                Long minTrials = (Long) doc.getData().get("minTrials");
                                String date = (String) doc.getData().get("date");
                                boolean locationReq = (boolean) doc.getData().get("locationRequired");
                                String expType = (String) doc.getData().get("experimentType");
                                String ownerID = (String) doc.getData().get("ownerID");
                                String uId = (String) doc.getData().get("uid");
                                boolean viewable = (boolean) doc.getData().get("viewable");
                                boolean editable = (boolean) doc.getData().get("editable");

                                // Experiments are only displayed in ListView if they are viewable or current user is the owner.
                                if (viewable || ownerID.equals(localUID)) {
                                    Experiment newExperiment = new Experiment(description, region, minTrials, date, locationReq, expType);

                                    //TODO remove the setters and use constructor
                                    newExperiment.setOwnerID(ownerID);
                                    newExperiment.setUID(uId);
                                    newExperiment.setViewable(viewable);
                                    newExperiment.setEditable(editable);
                                    newExperiment.setExpType(expType);
                                    experimentList.add(newExperiment);
                                }
                            }
                        } else {
                            Log.d("Test holder", "Error getting documents: ", task.getException());
                        }
                        experimentAdapter.notifyDataSetChanged();
                    }
                });
    }

    else if (searchType.equals("User")){
        CollectionReference collectionReference = db.collection("Users");
        userList = new ArrayList<User>();
        cleanedKeyword = keyword.trim().toLowerCase(); // we should allow user to search case insensitive

        //set up adapter
        userAdapter = new UserListAdapter(this, userList);
        exListView.setAdapter(userAdapter);

        exListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                User userToModify = userList.get(pos);
                showUserOptionsUI(userToModify, user);
                return true;
            }
        });

        // the search results should be a one time thing and do not auto update
        collectionReference
                .whereEqualTo("cleanedUsername", cleanedKeyword)//query line, can be combined and turned into complex queries (this one queries for name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // when you finish getting the data from the firebase
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {     // querysnapshot contains 0..* document snapshots
                        if (task.isSuccessful()) {

                            // https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/QueryDocumentSnapshot
                            for (QueryDocumentSnapshot doc : task.getResult()) {                      // iterate over query results of the document
                                Log.d("Test holder", doc.getId() + " => " + doc.getData());                // this for each document from our query result where we can filter and extract results
                                // the idea is here we use document snapshot methods such as get(String field) or getData()(returns fields of doc as a map) and we can
                                // filter, then grab and use all the data we could need from the firebase ()
                                // experimentId.add(document.getId());

                                // every document from first query results will be unique
                                String email = (String) doc.getData().get("email");
                                String name = (String) doc.getData().get("name");
                                ArrayList<String> ownedExp = (ArrayList<String>) doc.getData().get("ownedExperiments");
                                ArrayList<String> participatingExp = (ArrayList<String>) doc.getData().get("participatingExperiments");
                                String username = (String) doc.getData().get("username");
                                String userID = (String) doc.getData().get("uid");

                                // Experiments are only displayed in ListView if they are viewable or current user is the owner.
                                User tempUser = new User();
                                tempUser.setEmail(email);
                                tempUser.setName(name);
                                tempUser.setUid(userID);
                                tempUser.setParticipatingExperiments(participatingExp);
                                tempUser.setOwnedExperiments(ownedExp);
                                tempUser.setUsername(username);
                                userList.add(tempUser);
                                }
                            }
                         else {
                            Log.d("Test holder", "Error getting documents: ", task.getException());
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });

    }
        getSupportActionBar().setTitle("Search Results for: " + keyword); // title the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button to main activity

    }


    @Override
    public void onConfirmEdits(Experiment exp) {
        editExperiment(exp);
    }

    @Override
    public void onDeletePressed(Experiment exp) {
        delExperiment(exp);
        experimentList.remove(exp);
        experimentAdapter.notifyDataSetChanged();
    }
}