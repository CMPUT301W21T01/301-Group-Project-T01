package com.example.experimentify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class models is an activity that allows a user to view a list of their subscribed experiments.
 */
public class SubscribedActivity extends AppCompatActivity {
    private ExperimentController experimentController;
    private ArrayList<Experiment> experimentList;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "PrefsFile";

    private ListView experimentListView;

    FirebaseFirestore db;
    final String TAG = SubscribedActivity.class.getName();

    //TODO add listeners
    //TODO only display subscribed experiments
    //TODO allow users to subscribe using fragment
    //TODO send user in intent, move method for adding subs to User


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed);

        Intent intent = getIntent();

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);

        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();


        experimentListView = findViewById(R.id.subscribedLV);
        experimentListView.setAdapter(experimentController.getAdapter());


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experimentController.getExperiments().clear();
                String localUID = settings.getString("uid","0");
                for (QueryDocumentSnapshot doc : value){
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String description  = (String)  doc.getData().get("description");
                    String region       = (String)  doc.getData().get("region");
                    Long minTrials      = (Long)    doc.getData().get("minTrials");
                    String date         = (String)  doc.getData().get("date");
                    boolean locationReq = (boolean) doc.getData().get("locationRequired");
                    String expType      = (String)  doc.getData().get("experimentType");
                    String ownerID      = (String)  doc.getData().get("ownerID");
                    String uId          = (String)  doc.getData().get("uid");
                    boolean viewable    = (boolean) doc.getData().get("viewable");
                    boolean editable    = (boolean) doc.getData().get("editable");

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
                experimentController.getAdapter().notifyDataSetChanged();
            }
        });

    }
}