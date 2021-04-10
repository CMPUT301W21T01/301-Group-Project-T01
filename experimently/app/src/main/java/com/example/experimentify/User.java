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
    /**
     * This method sets the parcel for the User
     */

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

    /**
     * This method returns the snubbed experiments
     */
    public ExperimentController getSubbedExperiments() {
        return subbedExperiments;
    }

    /**
     * This method sets the snubbed experiments
     */
    public void setSubbedExperiments(ExperimentController subbedExperiments) {
        this.subbedExperiments = subbedExperiments;
    }
    /**
     * This method gets the email
     */

    public String getEmail() {
        return email;
    }

    /**
     * This method sets the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method gets the name
     *
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * This method gets the owned experiments
     *
     */
    public ArrayList<String> getOwnedExperiments() {
        return ownedExperiments;
    }

    /**
     * This method sets the owned experiments
     * @param ownedExperiments
     */
    public void setOwnedExperiments(ArrayList<String> ownedExperiments) {
        this.ownedExperiments = ownedExperiments;
    }
    /**
     * This method gets the participating experiments
     *
     */
    public ArrayList<String> getParticipatingExperiments() {
        return participatingExperiments;
    }

    /**
     * This method sets the participatinf experiments
     * @param participatingExperiments
     */
    public void setParticipatingExperiments(ArrayList<String> participatingExperiments) {
        this.participatingExperiments = participatingExperiments;
    }

    /**
     * This method gets the owned user id
     *
     */
    public String getUid() {
        return uid;
    }

    /**
     * This method set the owned user id
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * This method gets the owned username
     *
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method sers the owned username
     * @param username
     */
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