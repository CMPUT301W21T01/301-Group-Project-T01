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
    private String experimentid;
    private String userid;
    private Intent intent;
    private User currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_question);
        userQuestionInput = findViewById(R.id.questionInput);
        questionEnter = findViewById(R.id.questionInputButton);
        questionsList = findViewById(R.id.questionList);

        //SharedPreferences sp = currentUser.getSettings(getApplicationContext());
        //userid = sp.getString("uid", "0");
        //System.out.println("userID..." + userid);

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        String userid = settings.getString("uid", "0");
        System.out.println("userID..." + userid);


        intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            experimentid = extras.getString("experiment");
            System.out.println("experimentID..." + experimentid);
        }

        questionEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add question to DB
                String temp = userQuestionInput.getText().toString();
                System.out.println("userinput..." + temp);

                //chatQuestionController.addQuestionToDB(temp,db,experimentid);

            }
        });


        questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intent = new Intent(chatQuestionActivity.this, chatAnswerActivity.class);
                //intent.putExtra("qid", questionID);
                startActivity(intent);
            }
        });



    }
}