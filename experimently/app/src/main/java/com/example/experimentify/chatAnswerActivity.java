package com.example.experimentify;

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
    private ListView answersListView;
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


        answersEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = userAnswerInput.getText().toString();
//                System.out.println("userinput..." + description);
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