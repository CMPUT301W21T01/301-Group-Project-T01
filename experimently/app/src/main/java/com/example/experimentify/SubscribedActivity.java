package com.example.experimentify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
public class SubscribedActivity extends AppCompatActivity implements ExpOptionsFragment.OnFragmentInteractionListener {
    private ExperimentController experimentController;
    private ArrayList<Experiment> experimentList;
    private SharedPreferences settings;
    private User user;
    public static final String PREFS_NAME = "PrefsFile";

    private ListView experimentListView;

    FirebaseFirestore db;
    final String TAG = SubscribedActivity.class.getName();


    //TODO javadoc
    //TODO subcribe button in experiment activity
    

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
     * This method deletes an experiment from the database
     * @param expToDel experiment to delete
     */
    private void delExperiment(Experiment expToDel) {
        experimentController.deleteExperimentFromDB(expToDel, db);
    }

    /**
     * This method edits existing experiments in the database
     * @param expToEdit experiment to edit
     */
    private void editExperiment(Experiment expToEdit) {
        experimentController.editExperimentToDB(expToEdit, db);
    }

    /**
     * This method brings the user to the experiment screen for the experiment they clicked on.
     * @param pos position of experiment in ListView
     */
    private void handleExpClick(int pos) {
        Experiment clickedExperiment = experimentController.getClickedExperiment(pos);
        experimentController.viewExperiment(SubscribedActivity.this, clickedExperiment);
    }


    private void updateList(@Nullable QuerySnapshot value,  @Nullable FirebaseFirestoreException error) {
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

                newExperiment.userIsSubscribed(localUID, new Experiment.GetDataListener() {
                    @Override
                    public void onSuccess(boolean result) {
                        Log.d("bleh", "1");
                        if (result) {
                            Log.d("bleh", "4");
                            experimentList.add(newExperiment);
                            experimentController.getAdapter().notifyDataSetChanged();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed);

        Intent intent = getIntent();
        //intent.getBundleExtra()
        user = (User) intent.getSerializableExtra("user");

        db = FirebaseFirestore.getInstance();
        final CollectionReference expReference = db.collection("Experiments");
        //String userID = settings.getString("uid","0");
        //final DocumentReference userReference = db.collection("Users").document(userID);

        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);

        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();


        experimentListView = findViewById(R.id.subscribedLV);
        experimentListView.setAdapter(experimentController.getAdapter());


        expReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                updateList(value, error);
            }
        });

        experimentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                Experiment experiment = experimentController.getAdapter().getItem(pos);
                showExpOptionsUI(experiment, user);
                return true;
            }
        });

        experimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                handleExpClick(pos);
            }
        });

    }

    @Override
    public void onConfirmEdits(Experiment exp) {
        editExperiment(exp);
        String localUID = settings.getString("uid","0");
        exp.userIsSubscribed(localUID, new Experiment.GetDataListener() {
            @Override
            public void onSuccess(boolean result) {
                if (!result) {
                    experimentList.remove(exp);
                    experimentController.getAdapter().notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onDeletePressed(Experiment exp) {
        delExperiment(exp);
    }
}

