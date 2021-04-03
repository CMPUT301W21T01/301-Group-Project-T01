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

public class chatQuestionActivity extends AppCompatActivity {

    private EditText userQuestionInput;
    private ListView questionsList;
    private Button questionEnter;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "PrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_question);
        userQuestionInput = findViewById(R.id.questionInput);
        questionEnter = findViewById(R.id.questionInputButton);
        questionsList = findViewById(R.id.questionList);

        questionEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add question to DB

                userQuestionInput.getText();
                //settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            }
        });


        questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intent = new Intent(chatQuestionActivity.this, chatAnswerActivity.class);
                //intent.putExtra("qid", queuestionID);
                startActivity(intent);
            }
        });



    }
}