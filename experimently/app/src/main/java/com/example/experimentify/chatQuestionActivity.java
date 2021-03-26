package com.example.experimentify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class chatQuestionActivity extends AppCompatActivity {

    private EditText userQuestionInput;
    private Button questionEnter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_question);
        userQuestionInput = findViewById(R.id.questionInput);
        questionEnter = findViewById(R.id.questionInputButton);

        questionEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add question to DB 
            }
        });



    }
}