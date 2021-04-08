package com.example.experimentify;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

// AppCompatActivity
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

    private CardView measure;
    private EditText measureInput;
    private TextView endedMessageBox;
    private Button submitButton;

    private Button qrCodeGene;
    private ImageView qrCodeShow;

    private int intInfo;
    private float measurementInfo;

    private static final String TAG = ExperimentActivity.class.getName();

    public static final String PREFS_NAME = "PrefsFile";

    private FirebaseFirestore db;

    private Activity activity;

    private Trial newTrial;
    private String dateThatHasBeenSet;
    TrialController trialController;

    private MenuItem qrGenExp;
    private MenuItem qrPassMenu;
    private MenuItem qrFailMenu;
    private MenuItem qrIncreMenu;

    private MenuItem selfGenMenu;

    private MenuItem selfGenExp;
    private MenuItem selfGenPass;
    private MenuItem selfGenFail;
    private MenuItem selfGenIncre;

    private String encodedValue;

    private final int REQUESTQR = 301;

    private Location locationInfo = null;
    private String dateInfo;

    /**
     * This method sets text in the UI.
     */
    private void initUi() {
        description.setText(this.getResources().getString(R.string.description_header) + exp.getDescription());
        date.setText(this.getResources().getString(R.string.date_header) + exp.getDate());
        expType.setText(this.getResources().getString(R.string.exp_type_header) + exp.getExpType());
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

    /**
     * This method brings the user to the stats activity
     */
    private void launchStatsActivity() {
        Log.d("ughhh", "dfsfsdf");
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }


    private Trial trial;

//    public void enterTrialDetails(Activity activity) {
//        Intent intent = new Intent(activity, MapActivity.class);
//        activity.startActivityForResult(intent, 1);
//        Log.d(TAG, "enterTrialDetails: 2");
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 1) {
            if (resultCode == 0) {
                return;
            } else {
                dateInfo = data.getStringExtra("date");
                trial.setDate(dateInfo);
            }


            if (resultCode == 2) {
                locationInfo = data.getParcelableExtra("location");
                Log.d(TAG, "onActivityResult: " + locationInfo);
                trial.setTrialLocation(locationInfo);
            }
            trialController.addTrialToDB(trial, trial.getValue(), trial.getTrialLocation());
        } else if (requestCode == REQUESTQR) {
//            String encodeValue = data.getStringExtra("encode");
            IntentResult experimentValue = IntentIntegrator.parseActivityResult(resultCode, data);
            if (experimentValue != null) {
                if (experimentValue.getContents() != null) {
                    Log.d(TAG, "does this get here?");
                    Map<String, Object> enterData = new HashMap<>();
                    String barcodeValue = experimentValue.getContents();
                    enterData.put("contributingTrial", encodedValue);
                    db.collection("Barcodes").document(barcodeValue).set(enterData);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        description = findViewById(R.id.activ_descrip);
        date = findViewById(R.id.activ_date);
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


        //qrCodeGene = findViewById(R.id.qrCode);
        qrCodeShow = findViewById(R.id.qrCodeView);

        db = FirebaseFirestore.getInstance();
        activity = ExperimentActivity.this;

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        String localUID = settings.getString("uid", "0");

        ExperimentController experimentController = new ExperimentController(this);
        trialController = new TrialController();


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
                    Intent intent = new Intent(ExperimentActivity.this, chatQuestionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("experiment", exp.getUID());
                    System.out.println("experiment before..." + exp.getUID());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });

            statsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchStatsActivity();
                }
            });


//            qrCodeGene.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bitmap temp = null;
//                    String genID = exp.getUID();
//                    try {
//                        temp = qrCodeGen.textToImage(genID, 500, 500, 0, exp.getExpType());
//                    } catch (WriterException e) {
//                        e.printStackTrace();
//                    }
//                    qrCodeShow.setImageBitmap(temp);
//                    //System.out.println("testtest"+temp);
//                    qrCodeShow.setVisibility(View.VISIBLE);
//                }
//            });

            // If editable then display ui for conducting trials, else show message
            if (exp.isEditable()) {
                String expUID = exp.getUID();
                System.out.println("Before if " + exp.getExpType());
                //(exp.getExpType().equals("Count"))
                if (("Count").equals(exp.getExpType())) {
                    System.out.println("after if " + exp.getExpType());
                    count.setVisibility(View.VISIBLE);
                    trial = new CountTrial(localUID, expUID);
                    countButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: 1");
                            //Did not put next two lines in a method to ensure the correct sequence.
                            Intent intent = new Intent(ExperimentActivity.this, MapActivity.class);
                            intent.putExtra("experiment", exp);
                            activity.startActivityForResult(intent, 1);
                            trial.setDate(dateInfo);
                            if (locationInfo != null) {
                                trial.setTrialLocation(locationInfo);
                            }
                        }
                    });

                }

//exp.getExpType().equals("Binomial")
                if (("Binomial").equals(exp.getExpType())) {
                    binomial.setVisibility(View.VISIBLE);
                    passButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trial = new BinomialTrial(localUID, expUID, 1);
                            Intent intent = new Intent(ExperimentActivity.this, MapActivity.class);
                            intent.putExtra("experiment", exp);
                            activity.startActivityForResult(intent, 1);
                            trial.setDate(dateInfo);
                            if (locationInfo != null) {
                                trial.setTrialLocation(locationInfo);
                            }
                        }
                    });
                    failButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trial = new BinomialTrial(localUID, expUID, 0);
                            Intent intent = new Intent(ExperimentActivity.this, MapActivity.class);
                            activity.startActivityForResult(intent, 1);
                            intent.putExtra("experiment", exp);
                            trial.setDate(dateInfo);
                            if (locationInfo != null) {
                                trial.setTrialLocation(locationInfo);
                            }
                        }
                    });
                }

                if (("Integer").equals(exp.getExpType())) {
                    integer.setVisibility(View.VISIBLE);
                    showSubmitButton();
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                intInfo = Integer.parseInt(intInput.getText().toString());
                            } catch (NumberFormatException e) {
                                Log.d(TAG, "Integer/onClick/NumberFormatException: " + intInput + e);
                                return;
                            }
                            trial = new IntegerTrial(localUID, expUID, intInfo);
                            Intent intent = new Intent(ExperimentActivity.this, MapActivity.class);
                            intent.putExtra("experiment", exp);
                            activity.startActivityForResult(intent, 1);
                            trial.setDate(dateInfo);
                            if (locationInfo != null) {
                                trial.setTrialLocation(locationInfo);
                            }
                        }
                    });
                }

                if (("Measurement").equals(exp.getExpType())) {
                    measure.setVisibility(View.VISIBLE);
                    showSubmitButton();
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                measurementInfo = Float.parseFloat(measureInput.getText().toString());
                            } catch (NumberFormatException e) {
                                Log.d(TAG, "Measurement/onClick/NumberFormatException: " + measureInput + e);
                                return;
                            }
                            Log.d(TAG, "onClick: measurementInfo = " + measurementInfo);
                            trial = new MeasurementTrial(localUID, expUID, measurementInfo);
                            Intent intent = new Intent(ExperimentActivity.this, MapActivity.class);
                            intent.putExtra("experiment", exp);
                            activity.startActivityForResult(intent, 1);
                            trial.setDate(dateInfo);
                            if (locationInfo != null) {
                                trial.setTrialLocation(locationInfo);
                            }
                        }
                    });
                }


            } else {
                showExpEndedMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.experiment_qr_options_menu, menu);
        qrPassMenu = menu.findItem(R.id.qrPassMenu);
        qrFailMenu = menu.findItem(R.id.qrFailMenu);
        qrIncreMenu = menu.findItem(R.id.qrIncreMenu);
        qrGenExp = menu.findItem(R.id.qrGenExp);

        selfGenMenu = menu.findItem(R.id.setOwnMenu);

        selfGenPass = menu.findItem(R.id.selfSetPass);
        selfGenFail = menu.findItem(R.id.selfSetFail);
        selfGenIncre = menu.findItem(R.id.selfSetIncre);

        if (exp.getExpType().equals("Count")){
            System.out.println("exp type" + exp.getExpType());
            qrGenExp.setVisible(true);
            qrPassMenu.setVisible(false);
            qrFailMenu.setVisible(false);
            qrIncreMenu.setVisible(true);

            selfGenPass.setVisible(false);
            selfGenFail.setVisible(false);
            selfGenIncre.setVisible(true);
        }
        else if (exp.getExpType().equals("Binomial")){
            System.out.println("exp type" + exp.getExpType());
            qrGenExp.setVisible(true);
            qrPassMenu.setVisible(true);
            qrFailMenu.setVisible(true);
            qrIncreMenu.setVisible(false);

            selfGenIncre.setVisible(false);
            selfGenPass.setVisible(true);
            selfGenFail.setVisible(true);
        }

        else if(exp.getExpType().equals("Integer") || exp.getExpType().equals("Measurement")){
            qrGenExp.setVisible(true);
            qrPassMenu.setVisible(false);
            qrFailMenu.setVisible(false);
            qrIncreMenu.setVisible(false);

            selfGenMenu.setVisible(false);
        }

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String expUID = exp.getUID();
        Bitmap codeQR = null;
        String expType = exp.getExpType();
        switch (item.getItemId()) {
            case R.id.qrFailMenu:
                try{
                    codeQR = qrCodeGen.textToImage(expUID, 500, 500, 0, expType);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                qrCodeShow.setImageBitmap(codeQR);
                qrCodeShow.setVisibility(View.VISIBLE);
                return true;

            case R.id.qrIncreMenu:
            case R.id.qrPassMenu:
                try{
                    codeQR = qrCodeGen.textToImage(expUID, 500, 500, 1, expType);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                qrCodeShow.setImageBitmap(codeQR);
                System.out.println("qrPassMenu: " + codeQR);
                qrCodeShow.setVisibility(View.VISIBLE);
                return true;

            case R.id.qrGenExp:
                try {
                    codeQR = qrCodeGen.textToImage(expUID, 500, 500, 2, expType);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                qrCodeShow.setImageBitmap(codeQR);
                System.out.println("testtest" + codeQR);
                qrCodeShow.setVisibility(View.VISIBLE);
                return true;

            //Need to differentiate between pass fail from barcode.
            //No control over ResultCode from OnActivityResult
            //Set global variable?
            case R.id.selfSetPass: //self gen QR / barcode Pass
            case R.id.selfSetIncre: //self gen QR / barcode Increment
                Log.d(TAG, "onOptionsItemSelected: 1");
                getQrScan(ExperimentActivity.this, expUID + "/" + expType + "/" + "1");

                return true;
            case R.id.selfSetFail: //self gen QR / barcode fail
                getQrScan(ExperimentActivity.this, expUID + "/" + expType + "/" + "0");

                return true;

        }

        return true;
    }
    /**
     * This method initiates the QR scanning by using the Zxing library and then uses our scanning interface layout
     */
    public void getQrScan(Activity activity, String encode) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(qrScanActivity.class);
        encodedValue = encode;
        integrator.setRequestCode(REQUESTQR).initiateScan();
    }
}