package com.example.experimentify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class chatQuestionActivity extends AppCompatActivity {

    private EditText userQuestionInput;
    private ArrayList<chatQuestion> questionsList;
    private ListView questionsListView;
    private Button questionEnter;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "PrefsFile";
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

        questionController = new chatQuestionController(this);
        questionsList = questionController.getQuestions();
        questionsListView.setAdapter(questionController.getAdapter());


        questionEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add question to DB
                String description = userQuestionInput.getText().toString();
//                System.out.println("userinput..." + description);
                chatQuestion question = new chatQuestion(description, userID, experimentID);
                questionController.addQuestionToDB(question, db);

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



    }
}