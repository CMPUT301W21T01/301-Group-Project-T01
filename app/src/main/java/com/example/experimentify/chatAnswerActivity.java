package com.example.experimentify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class chatAnswerActivity extends AppCompatActivity {

    private EditText userAnswerInput;
    private ArrayList<chatAnswer> answersList;
    public ListView answersListView;
    private Button answersEnter;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "PrefsFile";
    public static final String TAG = chatAnswerActivity.class.getName();
    private String experimentID;
    private String questionID;
    private String userID;
    private Intent intent;
    private chatAnswerController answerController;
    private FirebaseFirestore db;

    /**
     * This interface gives access to the result of isUserOwner
     */
    interface GetDataListener {
        void onSuccess(boolean result);
    }

    /**
     * This method checks if the current user is the owner of the experiment
     * @param callback Interface for listener that returns the result once the database is done
     *                 with its task.
     */
    public void isUserOwner(chatAnswerActivity.GetDataListener callback) {
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
                .whereArrayContains("ownedExperiments", experimentID)
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
                                if (document.getId().equals(userID)) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_answer);
        userAnswerInput = findViewById(R.id.answerInput);
        answersEnter = findViewById(R.id.answerInputButton);
        answersListView = findViewById(R.id.answerList);

        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        userID = settings.getString("uid", "0");
        System.out.println("userID..." + userID);

        db = DatabaseSingleton.getDB();


        intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            experimentID = extras.getString("eid");
            questionID = extras.getString("qid");
            System.out.println("experimentID after..." + experimentID);
            System.out.println("questionID after..." + questionID);
        }
        CollectionReference questionRef = db.collection("Experiments").document(experimentID).collection("Questions").document(questionID).collection("Answers");

        System.out.println("answersSize..." + questionRef.get());
        answerController = new chatAnswerController(this);
        answersList = answerController.getAnswers();
        answersListView.setAdapter(answerController.getAdapter());
        isUserOwner(new GetDataListener() {
            @Override
            public void onSuccess(boolean result) {
                if (result) {
                    answersEnter.setVisibility(View.VISIBLE);
                    userAnswerInput.setVisibility(View.VISIBLE);

                }
                else {
                    answersEnter.setVisibility(View.GONE);
                    userAnswerInput.setVisibility(View.GONE);
                }
            }


        });



        answersEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = userAnswerInput.getText().toString();
//              System.out.println("userinput..." + description);
                chatAnswer answer = new chatAnswer(description, userID, experimentID, questionID);

                answerController.addAnswerToDB(answer, db);
                userAnswerInput.getText().clear();

            }
        });

        questionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                answerController.getAnswers().clear();
                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String description = (String) doc.getData().get("description");
                    String date = (String) doc.getData().get("date");
                    String eid = (String) doc.getData().get("eid");
                    String uId = (String) doc.getData().get("uid");
                    answersList.add(new chatAnswer(description, uId, eid, date));
                }
                answerController.getAdapter().notifyDataSetChanged();

            }
        });




    }
}