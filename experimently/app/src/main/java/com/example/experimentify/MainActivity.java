package com.example.experimentify;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This activity is a UI in which the user can see a list of published experiments
 * and add new experiments to the list.
 */
public class MainActivity extends AppCompatActivity implements AddExpFragment.OnFragmentInteractionListener, ExpOptionsFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener {
    private ExperimentController experimentController;
    private ExperimentListAdapter experimentAdapter;
    private ListView exListView;
    private FloatingActionButton showAddExpUiButton;
    private FloatingActionButton userProfileButton;
    private EditText searchBar;
    private ImageButton searchButton;
    private FloatingActionButton qrScanner;
    private ArrayList<Experiment> experimentList;
    final String TAG = MainActivity.class.getName();
    public static final String PREFS_NAME = "PrefsFile";
    FirebaseFirestore db;

    /**
     * This method shows the fragment that allows a user to create a new experiment.
     */
    private void showAddExpUi() {
        new AddExpFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
    }

    /**
     * This method shows the fragment that gives users options for the experiment they long clicked on.
     */
    private void showExpOptionsUI(Experiment experiment) {
        ExpOptionsFragment fragment = ExpOptionsFragment.newInstance(experiment);
        fragment.show(getSupportFragmentManager(), "EXP_OPTIONS");
    }

    private void showInfoUi(User user) {
        UserProfileFragment fragment = UserProfileFragment.newInstance(user);
        fragment.show(getSupportFragmentManager(), "SHOW_PROFILE");
    }

    /**
     * This method adds an experiment to the database.
     * @param experiment experiment to be added
     */
    private void addExperiment(Experiment experiment) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        String localUID = settings.getString("uid", "0");
        experimentController.addExperimentToDB(experiment, db, localUID);
    }

    /**
     * This method brings the user to the experiment screen for the experiment they clicked on.
     * @param pos position of experiment in ListView
     */
    private void handleExpClick(int pos) {
        Experiment clickedExperiment = experimentController.getClickedExperiment(pos);
        experimentController.viewExperiment(MainActivity.this, clickedExperiment);
    }

    /**
     * This method deletes an experiment from the database
     * @param expToDel experiment to delete
     */
    private void delExperiment(Experiment expToDel) {
        experimentController.deleteExperimentFromDB(expToDel, db);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        User user = initializeUser(db);


        //get ui resources
        exListView = findViewById(R.id.exListView);
        showAddExpUiButton = findViewById(R.id.showAddExpUiButton);
        userProfileButton = findViewById(R.id.userProfileButton);
        qrScanner = findViewById(R.id.qrScanner);

        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        // search button on click listener, pass query with intent
        searchButton.setOnClickListener((v) -> {
            if(searchBar.getText().toString().trim().length() > 0) { // search if the edit text is not empty
                openSearchResults(searchBar.getText().toString());
            }
        });

        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();
        exListView.setAdapter(experimentController.getAdapter());

        userProfileButton.setOnClickListener((v) -> {
            showInfoUi(user);
        });

        showAddExpUiButton.setOnClickListener((v) -> {
            showAddExpUi();
        });

        qrScanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, qrScanActivity.class);
                startActivity(intent);

            }
        });

        exListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                Experiment experiment = experimentController.getAdapter().getItem(pos);
                showExpOptionsUI(experiment);
                return true;
            }
        });

        exListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                handleExpClick(pos);
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experimentController.getExperiments().clear();
                for (QueryDocumentSnapshot doc : value){
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String description  = (String)  doc.getData().get("description");
                    String region       = (String)  doc.getData().get("region");
                    Long minTrials      = (Long)    doc.getData().get("minTrials");
                    String date         = (String)  doc.getData().get("date");
                    boolean locationReq = (boolean) doc.getData().get("locationRequired");
                    String ownerID      = (String)  doc.getData().get("ownerID");
                    String uId          = (String)  doc.getData().get("uid");

                    Experiment newExperiment = new Experiment(description, region, minTrials, date, locationReq);
                    newExperiment.setOwnerID(ownerID);
                    newExperiment.setUID(uId);
                    experimentList.add(newExperiment);
                }
                experimentController.getAdapter().notifyDataSetChanged();
            }
        });

    }

    //AddExpFragment
    @Override
    public void onOkPressed(Experiment newExp) {
        addExperiment(newExp);
    }

    @Override
    public void onDeletePressed(Experiment exp) {
        delExperiment(exp);
    }

    @Override
    public void editItem(Experiment ogItem, Experiment editedItem) {

    }

    //ExpOptionsFragment
    @Override
    public void onOkPressed(Experiment newExp, Boolean edit) {

    }
    /**
     * open the SearchResults activity, which will query the database and show relevant experiments to the keyword the user input
     */
    public void openSearchResults(String keyword){
        Intent intent = new Intent(this, SearchResults.class);
        intent.putExtra("keyword", keyword);
        startActivity(intent);
    }


    /**
     * initialize a new user to the firestore, we do not force them to provide info like name, email, username, e.t.c until later, we are just make a document with a unique ID that can identify that specific user
     * @param db is the firestore db
     */
    public User initializeUser(FirebaseFirestore db){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        User user = new User();
        if(settings.contains("uid")){
            String localUID = settings.getString("uid", "0");

            DocumentReference docRef = db.collection("Users").document(localUID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            user.setUid(localUID); // if the controlling if block is reached, then the document exists, which means localuid = uid stored in firestore
                            user.setEmail(document.getData().get("email").toString());
                            user.setName(document.getData().get("name").toString());
                            user.setParticipatingExperiments((ArrayList<String>) document.getData().get("participatingExperiment"));
                            user.setOwnedExperiments((ArrayList<String>) document.getData().get("ownedExperiments"));
                            user.setUsername(document.getData().get("username").toString());

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


        } // if close
        else{
            final CollectionReference collectionReference = db.collection("Users");  // connect to users collection

            Map<String,String> data = new HashMap<>();

            ArrayList<String> experiments = new ArrayList();

            Map<String, ArrayList<String>> uExps = new HashMap<>();



            /*collectionReference
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });*/



            DocumentReference documentReference = db.collection("Users").document();
            data.put("email","");    // put empty values for now
            data.put("name","");
            data.put("uid","");
            data.put("username","");

            uExps.put("ownedExperiments", experiments);
            uExps.put("participatingExperiments", experiments);

            documentReference.set(data);

            String uid = documentReference.getId().toString();
            user.setUid(uid);  // every user should always have a uid which is the uid generated by firestore (unique)

            DocumentReference addID = db.collection("Users").document(uid);

            addID.update("uid", uid);
            addID.update("participatingExperiments", experiments);
            addID.update("ownedExperiments", experiments);


            SharedPreferences prefSettings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("uid", uid);

            // Apply the edits!
            editor.apply();



            // Get from the SharedPreferences
            String localUID = settings.getString("uid", "0");



        }
        return user;
    }

}