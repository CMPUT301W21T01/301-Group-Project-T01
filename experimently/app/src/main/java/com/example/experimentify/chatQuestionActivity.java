package com.example.experimentify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class chatQuestionActivity extends AppCompatActivity {

    private EditText userQuestionInput;
    private ArrayList<chatQuestion> questionsList;
    private ListView questionsListView;
    private Button questionEnter;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "PrefsFile";
    public static final String TAG = chatQuestionActivity.class.getName();
    private String experimentID;
    private String userID;
    private Intent intent;
    private chatQuestionController questionController;
    private FirebaseFirestore db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_question);
        userQuestionInput = findViewById(R.id.questionInput);
        questionEnter = findViewById(R.id.questionInputButton);
        questionsListView = findViewById(R.id.questionList);


        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        userID = settings.getString("uid", "0");
        System.out.println("userID..." + userID);

        db = DatabaseSingleton.getDB();


        intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            experimentID = extras.getString("experiment");
            System.out.println("experimentID..." + experimentID);
        }
        CollectionReference questionRef = db.collection("Experiments").document(experimentID).collection("Questions");

        questionController = new chatQuestionController(this);
        questionsList = questionController.getQuestions();
        questionsListView.setAdapter(questionController.getAdapter());


        questionEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = userQuestionInput.getText().toString();
//                System.out.println("userinput..." + description);
                chatQuestion question = new chatQuestion(description, userID, experimentID);
                questionController.addQuestionToDB(question, db);
                userQuestionInput.getText().clear();

            }
        });


        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intent = new Intent(chatQuestionActivity.this, chatAnswerActivity.class);
                //intent.putExtra("qid", questionID);
                startActivity(intent);
            }
        });

        questionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                questionController.getQuestions().clear();
                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String description = (String) doc.getData().get("description");
                    String date = (String) doc.getData().get("date");
                    String eid = (String) doc.getData().get("eid");
                    String uId = (String) doc.getData().get("uid");
                    questionsList.add(new chatQuestion(description, uId, eid, date));
                }
                questionController.getAdapter().notifyDataSetChanged();


            }
        });
    }
}