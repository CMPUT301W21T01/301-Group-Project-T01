package com.example.experimentify;

import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseSingleton {

    private static FirebaseFirestore instance = null;

    public static FirebaseFirestore getDB(){
        if (instance == null){
            instance = FirebaseFirestore.getInstance();
        }
        return instance;
    }
}
