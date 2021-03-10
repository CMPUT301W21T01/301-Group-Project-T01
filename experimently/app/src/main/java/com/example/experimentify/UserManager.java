package com.example.experimentify;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A user manager class that accepts a username as a string in its constructor that uniquely identifies a user in our database, we can then do whatever we need to with the user data, such as retrieving any fields, changing them, using it to display in the app e.t.c.
 * input: username
 * returns: see the multitude of available methods that return values
 */
public class UserManager {
    private Map<String,Object> userData = new HashMap<>(); // stores user data that we will grab from firestore

    private FirebaseFirestore db;

    private final String TAG = "Sample";

    public void Main(String userName){
        // in constructor grab all the user data at once and store into hashmap, so that all fields are easily accessible(instead of passing around document snapshots)

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Users");

        collectionReference
                .whereEqualTo("username", userName) // our querydocumentsnapshot should have ONLY 1 document (the uniquely identifying username!!) from this query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {    // when you finish getting the data from the firebase
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {     // querysnapshot contains 0..* document snapshots
                        if (task.isSuccessful()) {

                            // https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/QueryDocumentSnapshot

                            for (QueryDocumentSnapshot document : task.getResult()) {                      // iterate over query results of the document
                                Log.d("Test holder", document.getId() + " => " + document.getData());                // this for each document from our query result where we can filter and extract results

                                userData.put(document.getId(),document.getData());  // document.getId() should actually be the username if we choose to implement it like that in the firebase

                                // note: .getData() returns the fields of the document as a Map or null if the document doesn't exist. Field values will be converted to their native Java representation.

                                // now we have a map where the key is the username, and its pair is another map which we can call .get() on to get values at specific fields
                                // example: document.getData.get("email").toString()  will return the value at the email field
                            }
                        } else {
                            Log.d("Test holder", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * returns in-app username of user
     * @return username
     */
    public String getUsername(){
        return userData.get("username").toString();
    }
    /**
     * returns real name of user
     * @return actual name of user
     */
    public String getName(){

        return userData.get("name").toString();
    }
    /**
     * returns email of user
     * @return email of user
     */
    public String getEmail(){
        return userData.get("email").toString();
    }
    /**
     * returns string list EIDS of owned experiments
     * @return string list of EIDS of owned experiments
     */
    public String[] getOwnedExperiments(){
        String [] ownedExperiments = (String[]) userData.get("ownedExperiments");
        return ownedExperiments;
    }
    /**
     * returns string list EIDS of participating experiments
     * @return string list of EIDS of participating experiments
     */
    public String[] getParticipatingExperiments(){
        String [] participatingExperiments = (String[]) userData.get("participatingExperiments");
        return participatingExperiments;
    }

    /**
     * changes the email field for the user in the firestore
     * @param newEmail  new email as a string that user wants to change to
     *  doc references : https://firebase.google.com/docs/firestore/manage-data/add-data
     */
    public void setEmail(String newEmail){
        final CollectionReference collectionReference = db.collection("Users");

        collectionReference
                .document("email")
                .set(newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    /**
     * changes the name field for the user in the firestore
     * @param newName  new name as a string that user wants to change to
     *  doc references : https://firebase.google.com/docs/firestore/manage-data/add-data
     */
    public void setName(String newName){
        final CollectionReference collectionReference = db.collection("Users");
        collectionReference
                .document("name")
                .set(newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    /**
     * changes the owned experiments field in the database
     * @param ownedExperiments  list of experiments that the user now owns (i guess an updated list)
     *  doc references : https://firebase.google.com/docs/firestore/manage-data/add-data
     */
    public void setOwnedExperiments(String [] ownedExperiments){
        final CollectionReference collectionReference = db.collection("Users");
        collectionReference
                .document("ownedExperiments")
                .set(ownedExperiments)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    /**
     * changes the owned experiments field in the database
     * @param participatingExperiments  list of experiments that the user now participates in (i guess an updated list)
     *  doc references : https://firebase.google.com/docs/firestore/manage-data/add-data
     */
    public void setParticipatingExperiments(String [] participatingExperiments){
        final CollectionReference collectionReference = db.collection("Users");
        collectionReference
                .document("participatingExperiments")
                .set(participatingExperiments)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
