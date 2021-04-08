package com.example.experimentify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable, Parcelable {
    private ExperimentController subbedExperiments;
    private String email;
    private String name;
    private ArrayList<String> ownedExperiments;
    private ArrayList<String> participatingExperiments;
    private String uid;
    private String username;
    private SharedPreferences settings;
    final String TAG = Experiment.class.getName();
    public static final String PREFS_NAME = "PrefsFile";

    /**
     * This interface gives access to the result of isIgnoringUser
     */
    interface GetDataListener {
        void onSuccess(boolean result);
    }

    /**
     * This method checks if the current user is subscribed to the experiment it is called on
     * @param userID The ID of the user to check for if the current user is ignoring
     * @param callback Interface for listener that returns the result once the database is done
     *                 with its task.
     */
    public void isIgnoringUser(String userID, User.GetDataListener callback) {
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
                .whereArrayContains("usersIgnoring", userID)//gets all users that are ignoring the specified user
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
                                if (document.getId().equals(uid)) {
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

    /*
    public void isParticipant(String userID, User.GetDataListener callback) {

    }

     */



    public User() {
        this.uid = "";
        this.email = "";
        this.name = "";
        this.username = "";
        //this.subbedExperiments = new ExperimentController();
        this.ownedExperiments = new ArrayList<String>();
        this.participatingExperiments = new ArrayList<String>();
    }

    protected User(Parcel in) {
        email = in.readString();
        name = in.readString();
        ownedExperiments = in.createStringArrayList();
        participatingExperiments = in.createStringArrayList();
        uid = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public ExperimentController getSubbedExperiments() {
        return subbedExperiments;
    }

    public void setSubbedExperiments(ExperimentController subbedExperiments) {
        this.subbedExperiments = subbedExperiments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getOwnedExperiments() {
        return ownedExperiments;
    }

    public void setOwnedExperiments(ArrayList<String> ownedExperiments) {
        this.ownedExperiments = ownedExperiments;
    }

    public ArrayList<String> getParticipatingExperiments() {
        return participatingExperiments;
    }

    public void setParticipatingExperiments(ArrayList<String> participatingExperiments) {
        this.participatingExperiments = participatingExperiments;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void generateUserId() {
        //pass
    }

    /**
     * This method gives activities access to user settings.
     * @param context application context
     * @return Returns SharedPreferences object
     */
    public SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    //TODO maybe move addSub and deleteSub to different class, not sure if they belong here
    /**
     * This method adds an experiment to a user's list of subscribed experiments.
     * @param userID user id saved on users device
     * @param expID id of experiment
     * @param db database where user's subscription list will be updated
     */
    public void addSub(String userID, String expID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Users").document(userID);
        ref.update("participatingExperiments", FieldValue.arrayUnion(expID));
    }

    /**
     * This method deletes an experiment to a user's list of subscribed experiments.
     * @param userID user id saved on users device
     * @param expID id of experiment
     * @param db database where user's subscription list will be updated
     */
    public void deleteSub(String userID, String expID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Users").document(userID);
        ref.update("participatingExperiments", FieldValue.arrayRemove(expID));
    }

    //TODO maybe move addSub and deleteSub to different class, not sure if they belong here
    /**
     * This method adds an experiment to a user's list of subscribed experiments.
     * @param userID user id saved on users device
     * @param userToModifyID id of user to add to ignore list
     * @param db database where user's subscription list will be updated
     */
    public void addIgnore(String userID, String userToModifyID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Users").document(userID);
        ref.update("usersIgnoring", FieldValue.arrayUnion(userToModifyID));
    }

    /**
     * This method deletes an experiment to a user's list of subscribed experiments.
     * @param userID user id saved on users device
     * @param userToModifyID id of user to add to ignore list
     * @param db database where user's subscription list will be updated
     */
    public void deleteIgnore(String userID, String userToModifyID, FirebaseFirestore db) {
        DocumentReference ref = db.collection("Users").document(userID);
        ref.update("usersIgnoring", FieldValue.arrayRemove(userToModifyID));
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeStringList(ownedExperiments);
        dest.writeStringList(participatingExperiments);
        dest.writeString(uid);
        dest.writeString(username);
    }
}