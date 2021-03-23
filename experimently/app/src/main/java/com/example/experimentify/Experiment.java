package com.example.experimentify;


import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
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
    final String TAG = Experiment.class.getName();



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
        editable = in.readByte() != 0;
        ended = in.readByte() != 0;
        viewable = in.readByte() != 0;
        uid = in.readString();
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

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public long getMinTrials() {
        return minTrials;
    }

    public String getExperimentId(){return experimentId;}

    public String getOwnerID() {
        return ownerID;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isLocationRequired() {
        return locationRequired;
    }

    public boolean isViewable() {
        return viewable;
    }

    public boolean isEnded() {
        return ended;
    }

    public String getUID() {
        return uid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUID(String id) {
        this.uid = id;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getExpType(){return expType;}

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    interface IsSubbedCallback {
       void onBool(boolean containsExp);
    }

    public void userIsSubscribed(String userID, IsSubbedCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //https://stackoverflow.com/a/52421135
        //https://firebase.google.com/docs/firestore/query-data/queries#execute_a_query
        //https://firebase.google.com/docs/firestore/query-data/queries#array_membership
        //Returns every user doc where array contains uid (experiment id)
        db.collection("Users")
                .whereArrayContains("participatingExperiments", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("qqq","yurrr");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.getData().get("uid").equals(userID)) {
                                    Log.d("qqq","yurrr2");
                                    callback.onBool(true);
                                }
                                else {
                                    //TODO this is never reached, fix
                                    Log.d("qqq","yurrr3");
                                    callback.onBool(false);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
        dest.writeBoolean(editable);
        dest.writeBoolean(ended);
        dest.writeBoolean(viewable);
        dest.writeString(uid);

    }


    //TODO Ask about the variables below
    //private Location region //Region? - from requirements
    //private int minTrials //ex wont be shown until this amount of trials are submitted

}
