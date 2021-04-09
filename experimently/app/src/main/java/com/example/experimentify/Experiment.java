package com.example.experimentify;


import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is a class that models an experiment in which trials can be conducted on.
 */
public class Experiment implements Parcelable {
    private boolean viewable;
    private String ownerID;
    private Image graph;
    private String experimentId;
    private ArrayList<Trial> trials;
    private String description;
    private String date;
    private long minTrials; //db takes in a long not an int
    private boolean locationRequired;
    private String region;
    private boolean ended;
    private String uid;
    private boolean editable;
    private String expType;
    private long trialCount;
    private long questionCount;
    static final String TAG = Experiment.class.getName();

    public Experiment(String description, String region, long minTrials, String date, boolean locationRequired, String expType) {
        this.description = description;
        this.date = date;
        this.region = region;
        this.minTrials = minTrials;
        this.locationRequired = locationRequired;
        this.expType = expType;
        editable = true;
        ended = false;
        viewable = true;
        trialCount = 0;
        questionCount = 0;



    }

    protected Experiment(Parcel in) {
        /*Make sure these variable are assigned in the same order as the normal constructor
        and writeToParcel() or else the attributes will be set to null for god knows what reason.
         */
        description = in.readString();
        date = in.readString();
        region = in.readString();
        minTrials = in.readLong();
        locationRequired = in.readByte() != 0;
        expType = in.readString();
        editable = in.readByte() != 0;
        ended = in.readByte() != 0;
        viewable = in.readByte() != 0;
        uid = in.readString();
        trialCount = in.readLong();
        questionCount = in.readLong();
        ownerID = in.readString();


    }

    public static final Creator<Experiment> CREATOR = new Creator<Experiment>() {
        @Override
        public Experiment createFromParcel(Parcel in) {
            return new Experiment(in);
        }

        @Override
        public Experiment[] newArray(int size) {
            return new Experiment[size];
        }
    };
    /**
     * This method gets the date
     */
    public String getDate() {
        return date;
    }

    /**
     * This method gets the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method gets the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * This method gets the minimum of trials
     */
    public long getMinTrials() {
        return minTrials;
    }

    public String getExperimentId(){return experimentId;}
    /**
     * This method gets the experiment id
     */

    public String getOwnerID() {
        return ownerID;
    }

    /**
     * This method sets the experiment to viewable
     * @param viewable
     */
    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

    /**
     * This method sets a boolean if an experimtn has ended or not
     * @param ended
     */
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    /**
     * This method gets if the location is required or not
     */
    public boolean isLocationRequired() {
        return locationRequired;
    }

    /**
     * This method gets if the experiment is viewable
     */
    public boolean isViewable() {
        return viewable;
    }
    /**
     * This method gets if the experiment is ended
     */

    public boolean isEnded() {
        return ended;
    }

    /**
     * This method gets user id of the experiment
     */
    public String getUID() {
        return uid;
    }

    /**
     * This method sets the experiment description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method sets the experiment region
     * @param region
     */
    public void setRegion(String region){this.region = region; }

    /**
     * This method sets the experiment min trials
     * @param min
     */
    public void setMinTrials(long min){this.minTrials = min;}

    /**
     * This method sets the experiment locaiton is required
     * @param b
     */
    public void setLocationRequired(boolean b){this.locationRequired = b; }

    /**
     * This method sets the experiment date
     * @param date
     */
    public void setDate(String date){this.date = date;}

    /**
     * This method sets the experiment user id
     * @param id
     */
    public void setUID(String id) {
        this.uid = id;
    }

    /**
     * This method gets the experiment and says if its deible or not
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * This method sets the experiment owners id
     * @param ownerID
     */
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    /**
     * This method gets the experiment type
     */
    public String getExpType(){return expType;}
    /**
     * This method sets the experiment type
     * @param expType
     */
    public void setExpType(String expType) {
        this.expType = expType;
    }

    /**
     * This method sets the experiment is editible
     * @param editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * This method sets the experiment questions count
     */
    public long getQuestionCount() {
        return questionCount;
    }

    /**
     * This method sets the experiment trialcount to increase +1
     */
    public void incrementTrialCount(){
        trialCount++;
    }
    /**
     * This method sets the experiment questions to increase +1
     */
    public void incrementQuestionCount(){
        questionCount++;
    }
    /**
     * This method gets the experiment trialcount
     */
    public long getTrialCount() {
        return trialCount;
    }
    /**
     * This method sets the experiment trialcount
     * @param trialCount
     */
    public void setTrialCount(long trialCount) {
        this.trialCount = trialCount;
    }

    /**
     * This method gets the experiment question count
     */
    public void setQuestionCount(long questionCount) {
        this.questionCount = questionCount;
    }

    /**
     * This interface gives access to the result of userIsSubscribed
     */
    interface GetDataListener {
        void onSuccess(boolean result);
    }


    /**
     * This method checks if the current user is subscribed to the experiment it is called on
     * @param userID The ID of the current user
     * @param callback Interface for listener that returns the result once the database is done
     *                 with its task.
     */
    public void userIsSubscribed(String userID, GetDataListener callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
            Author: Joseph Varghese
            Date published: Sep 29 '14 at 10:20
            License: Attribution-ShareAlike 3.0 Unported
            Link: https://stackoverflow.com/a/46997517

            I used this post to help with returning a value after the database is done
            retrieving data.
        */
        db.collection("Users")
                .whereArrayContains("participatingExperiments", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                /* There are no users with this experiment in their
                                   subscription list
                                 */
                                callback.onSuccess(false);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.getId().equals(userID)) {
                                    /* The local user has the experiment in their
                                       subscription list.
                                     */
                                    callback.onSuccess(true);
                                    break; // Prevents result from being changed
                                }
                                else {
                                    /* The local user was not one of the users who had the
                                       experiment in their subscription list
                                     */
                                    callback.onSuccess(false);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    /**
     * This method checks if the experiment is ignoring results from the given user
     * @param userID The ID of the user to check for if the current user is ignoring
     * @param callback Interface for listener that returns the result once the database is done
     *                 with its task.
     */
    public void isUserIgnored(String userID, GetDataListener callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
            Author: Joseph Varghese
            Date published: Sep 29 '14 at 10:20
            License: Attribution-ShareAlike 3.0 Unported
            Link: https://stackoverflow.com/a/46997517

            I used this post to help with returning a value after the database is done
            retrieving data.
        */
        db.collection("Experiments")
                .whereArrayContains("ignoredUsers", userID)//gets all users that are ignoring the specified user
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                callback.onSuccess(false);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.getId().equals(uid)) {
                                    callback.onSuccess(true);
                                    break; // Prevents result from being changed
                                }
                                else {
                                    callback.onSuccess(false);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * This method checks if the user given is a participant of the experiment
     * @param userID The ID of the user to check for participation
     * @param callback Interface for listener that returns the result once the database is done
     *                 with its task.
     */
    public void isUserParticipant(String userID, GetDataListener callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
            Author: Joseph Varghese
            Date published: Sep 29 '14 at 10:20
            License: Attribution-ShareAlike 3.0 Unported
            Link: https://stackoverflow.com/a/46997517

            I used this post to help with returning a value after the database is done
            retrieving data.
        */
        db.collection("Experiments")
                .whereArrayContains("participants", userID)//gets all users that are ignoring the specified user
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                callback.onSuccess(false);
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.getId().equals(uid)) {
                                    callback.onSuccess(true);
                                    break; // Prevents result from being changed
                                }
                                else {
                                    callback.onSuccess(false);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    /**
     * This method adds a user to the experiments ignoredUsers list in the database
     * @param userID id of user to add
     * @param db database where user's subscription list will be updated
     */
    public void addIgnore(String userID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Experiments").document(uid);
        ref.update("ignoredUsers", FieldValue.arrayUnion(userID));
    }

    /**
     * This method removes a user from the experiments ignoredUsers list in the database
     * @param userID id of user to remove
     * @param db database where user's subscription list will be updated
     */
    public void removeIgnore(String userID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Experiments").document(uid);
        ref.update("ignoredUsers", FieldValue.arrayRemove(userID));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(region);
        dest.writeLong(minTrials);
        dest.writeBoolean(locationRequired);
        dest.writeString(expType);
        dest.writeBoolean(editable);
        dest.writeBoolean(ended);
        dest.writeBoolean(viewable);
        dest.writeString(uid);
        dest.writeLong(trialCount);
        dest.writeLong(questionCount);
        dest.writeString(ownerID);
    }

}
