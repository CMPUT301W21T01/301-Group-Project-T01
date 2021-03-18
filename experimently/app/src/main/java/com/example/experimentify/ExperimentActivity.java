package com.example.experimentify;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExperimentActivity extends AppCompatActivity {

    private TextView description;
    private TextView date;
    private TextView expType;
    private TextView location;
    private Button statsButton;
    private Button chatButton;
    private Experiment exp;

    private CardView count;
    private Button countButton;

    private CardView binomial;
    private Button passButton;
    private Button failButton;

    private CardView integer;
    private EditText intInput;

    private  CardView measure;
    private EditText measureInput;
    private TextView endedMessageBox;
    private Button submitButton;


    /**
     * This method sets text in the UI.
     */
    private void initUi() {
        description.setText(this.getResources().getString(R.string.description_header) + exp.getDescription());
        date.setText(this.getResources().getString(R.string.date_header) + exp.getDate());
        expType.setText(this.getResources().getString(R.string.exp_type_header) + exp.getExperimentId());
        location.setText(this.getResources().getString(R.string.region_header) + exp.getRegion());
    }

    /**
     * This method displays a message when an experiment has ended.
     */
    private void showExpEndedMessage() {
        endedMessageBox.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
    }

    /**
     * This method displays a button for submitting trials when the experiment is ongoing.
     */
    private void showSubmitButton() {
        endedMessageBox.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);
        description = findViewById(R.id.activ_descrip);
        date = findViewById(R.id.actiiv_date);
        expType = findViewById(R.id.activ_expType);
        location = findViewById(R.id.activ_location);
        statsButton = findViewById(R.id.stats);
        chatButton = findViewById(R.id.chat);
        count = findViewById(R.id.Count);
        countButton = findViewById(R.id.increaseCount);
        binomial = findViewById(R.id.Binomial);
        passButton = findViewById(R.id.pass);
        failButton = findViewById(R.id.fail);
        integer = findViewById(R.id.Integer);
        intInput = findViewById(R.id.integerInput);
        measure = findViewById(R.id.measurement);
        measureInput = findViewById(R.id.meaasurementInput);
        endedMessageBox = findViewById(R.id.trialEndedMessage);
        submitButton = findViewById(R.id.submitTrials);



        Intent intent = getIntent();
        if (intent.hasExtra("clickedExp")) {
            exp = intent.getParcelableExtra("clickedExp");
            initUi();

            statsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // If editable then display ui for conducting trials, else show message
            if (exp.isEditable()) {
                if (exp.getExpType() == "count") {
                    count.setVisibility(View.VISIBLE);
                    countButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                if (exp.getExpType() == "binomial") {
                    binomial.setVisibility(View.VISIBLE);
                    passButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    failButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                if (exp.getExpType() == "integer") {
                    integer.setVisibility(View.VISIBLE);

                }

                if (exp.getExpType() == "measurement") {
                    measure.setVisibility(View.VISIBLE);

                }

                showSubmitButton();
            }
            else {
                showExpEndedMessage();
            }
        }
    }
}