package com.example.experimentify;


import android.media.Image;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

}
