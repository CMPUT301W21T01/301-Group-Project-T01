package com.example.experimentify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SubscribedActivity extends AppCompatActivity {
    private ExperimentController experimentController;
    private ArrayList<Experiment> experimentList;

    private ListView experimentListView;

    FirebaseFirestore db;
    final String TAG = SubscribedActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed);

        experimentListView = findViewById(R.id.subscribedLV);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiments");

        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();
        experimentListView.setAdapter(experimentController.getAdapter());

        /*
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
                    String expType      = (String) doc.getData().get("experimentType");
                    String ownerID      = (String)  doc.getData().get("ownerID");
                    //String uId          = (String)  doc.getData().get("uid");
                    boolean viewable    = (boolean) doc.getData().get("viewable");
                    boolean editable    = (boolean) doc.getData().get("editable");


                    //String localUID = getLocalUID();

                    // Experiments are only displayed in ListView if they are viewable or current user is the owner.
                    if (viewable || ownerID.equals(localUID)) {
                        Experiment newExperiment = new Experiment(description, region, minTrials, date, locationReq, expType);

                        //TODO remove the setters and use constructor
                        newExperiment.setOwnerID(ownerID);
                        //newExperiment.setUID(uId);
                        newExperiment.setViewable(viewable);
                        newExperiment.setEditable(editable);
                        newExperiment.setExpType(expType);
                        experimentList.add(newExperiment);
                    }
                }
                experimentController.getAdapter().notifyDataSetChanged();
            }
        });
        */
    }
}