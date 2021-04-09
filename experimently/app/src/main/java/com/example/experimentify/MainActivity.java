package com.example.experimentify;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * This activity is a UI in which the user can see a list of published experiments
 * and add new experiments to the list.
 */
public class MainActivity extends AppCompatActivity implements AddExpFragment.OnFragmentInteractionListener, ExpOptionsFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener {
    private ExperimentController experimentController;
    private ExperimentListAdapter experimentAdapter;
    private ListView exListView;
    private FloatingActionButton showAddExpUiButton;
    private FloatingActionButton userProfileButton;
    private Button subButton;
    private EditText searchBar;
    private ImageButton searchButton;
    private FloatingActionButton qrScanner;
    private ArrayList<Experiment> experimentList;
    private User currentUser;
    final String TAG = MainActivity.class.getName();
    public static final String PREFS_NAME = "PrefsFile";
    private FirebaseFirestore db;
    private Spinner searchSpinner;
    private TrialController trialController;
    private String localUID;
    private FusedLocationProviderClient fusedLocationProviderClient;


    /**
     * This method shows the fragment that allows a user to create a new experiment.
     */
    private void showAddExpUi() {
        new AddExpFragment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
    }

    /**
     * This method shows the fragment that gives users options for the experiment they long clicked on.
     *
     * @param experiment experiment whose options will be edited
     */
    private void showExpOptionsUI(Experiment experiment) {
        String localUID = getLocalUID();
        ExpOptionsFragment fragment = ExpOptionsFragment.newInstance(experiment, localUID, currentUser);
        fragment.show(getSupportFragmentManager(), "EXP_OPTIONS");
    }

    /**
     * This method shows the ui for user settings.
     */
    private void showInfoUi() {
        UserProfileFragment fragment = UserProfileFragment.newInstance(currentUser);
        fragment.show(getSupportFragmentManager(), "SHOW_PROFILE");
    }

    /**
     * This method adds an experiment to the database and automatically subscribed the creator.
     * It also adds the experiment to the user's list of owned experiments in the DB
     *
     * @param experiment experiment to be added
     */
    private void addExperiment(Experiment experiment) {
        String localUID = getLocalUID();
        experimentController.addExperimentToDB(experiment, db, localUID);
        currentUser.addSub(localUID, experiment.getUID(), db);
    }

    /**
     * This method brings the user to the experiment screen for the experiment they clicked on.
     *
     * @param pos position of experiment in ListView
     */
    private void handleExpClick(int pos) {
        Experiment clickedExperiment = experimentController.getClickedExperiment(pos);
        experimentController.viewExperiment(MainActivity.this, clickedExperiment);
    }

    /**
     * This method brings the user from the home screen to the opening scanner then to the next screen
     * its a successful scan to the experiment activity
     */
    private void handleScanClick() {
        experimentController.getQrScan(MainActivity.this);
    }

    /**
     * This method deletes an experiment from the database
     *
     * @param expToDel experiment to delete
     */
    private void delExperiment(Experiment expToDel) {
        experimentController.deleteExperimentFromDB(expToDel, db);
    }

    /**
     * This method edits existing experiments in the database
     *
     * @param expToEdit experiment to edit
     */
    private void editExperiment(Experiment expToEdit) {
        experimentController.editExperimentToDB(expToEdit, db);
    }

    /**
     * This method gets the user id saved locally on the user's device.
     *
     * @return Returns string of locally stored user id.
     */
    private String getLocalUID() {
        //TODO maybe change to passing in a SharedPreference so that method could be used in more places
        SharedPreferences sp = currentUser.getSettings(getApplicationContext());
        return sp.getString("uid", "0");
    }

    /**
     * This method brings the user to an activity that shows a list of experiments they are
     * subscribed  to.
     */
    private void viewSubscribedExpList() {
        Intent intent = new Intent(this, SubscribedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", currentUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        currentUser = initializeUser(db);

        final CollectionReference collectionReference = db.collection("Experiments");
        trialController = new TrialController();


        //get ui resources
        exListView = findViewById(R.id.exListView);
        showAddExpUiButton = findViewById(R.id.showAddExpUiButton);
        userProfileButton = findViewById(R.id.userProfileButton);
        qrScanner = findViewById(R.id.qrScanner);
        subButton = findViewById(R.id.subButton);


//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions();
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);


        // used documentation at https://developer.android.com/guide/topics/ui/controls/spinner
        searchSpinner = (Spinner) findViewById(R.id.search_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.search, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        searchSpinner.setAdapter(adapter);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.experiments, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //expType.setAdapter(adapter);
        //expType.setOnItemSelectedListener(this);

        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        // search button on click listener, pass query with intent
        searchButton.setOnClickListener((v) -> {
            if (searchBar.getText().toString().trim().length() > 0) { // search if the edit text is not empty
                openSearchResults(searchBar.getText().toString());
            }
        });

        experimentController = new ExperimentController(this);
        experimentList = experimentController.getExperiments();
        exListView.setAdapter(experimentController.getAdapter());

        userProfileButton.setOnClickListener((v) -> {
            showInfoUi();
        });

        showAddExpUiButton.setOnClickListener((v) -> {
            showAddExpUi();
        });

        qrScanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleScanClick();
                // Intent intent = new Intent( MainActivity.this, qrScanActivity.class);
                //startActivityForResult(new Intent(getApplicationContext(), qrScanActivity.class),1);
                //Intent intent = new Intent( MainActivity.this, qrScanActivity.class);
                //startActivity(intent);
            }

        });

        exListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id) {
                Experiment experiment = experimentController.getAdapter().getItem(pos);
                showExpOptionsUI(experiment);
                return true;
            }
        });

        exListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                handleExpClick(pos);
            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewSubscribedExpList();
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                experimentController.getExperiments().clear();
                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("EID")));
                    String description = (String) doc.getData().get("description");
                    String region = (String) doc.getData().get("region");
                    long minTrials = (long) doc.getData().get("minTrials");
                    String date = (String) doc.getData().get("date");
                    boolean locationReq = (boolean) doc.getData().get("locationRequired");
                    String expType = (String) doc.getData().get("ExperimentType");
                    String ownerID = (String) doc.getData().get("ownerID");
                    String uId = (String) doc.getData().get("uid");
                    boolean viewable = (boolean) doc.getData().get("viewable");
                    boolean editable = (boolean) doc.getData().get("editable");
                    //commented out to fix error
                    //long questionCount  = (long)    doc.getData().get("questionCount");
                    //long trialCount     = (long)    doc.getData().get("trialCount");

                    String localUID = getLocalUID();

                    // Experiments are only displayed in ListView if they are viewable or current user is the owner.
                    if (viewable || ownerID.equals(localUID)) {
                        Experiment newExperiment = new Experiment(description, region, minTrials, date, locationReq, expType);

                        //TODO remove the setters and use constructor
                        newExperiment.setOwnerID(ownerID);
                        newExperiment.setUID(uId);
                        newExperiment.setViewable(viewable);
                        newExperiment.setEditable(editable);
                        newExperiment.setExpType(expType);
                        //commented out to fix error
                        //newExperiment.setTrialCount(trialCount);
                        //newExperiment.setQuestionCount(questionCount);
                        experimentList.add(newExperiment);
                    }
                }
                experimentController.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /**
     * This method is what is used to direct the from the scan to the correct activity, it first will check
     * if we make sure that the scan its self isnt null and then we make sure the contents of the values arent null adn then direct
     * the user to the new experiment page
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        final String VIEW = "2";
        final String PASS = "1";
        final String FAIL = "0";
        String experimentID = null;
        String experimentType = null;
        String experimentMode = null;

        IntentResult experimentValue = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (experimentValue != null) {
            if (experimentValue.getContents() != null) {
                String[] temp = experimentValue.getContents().split("/");
                Log.d(TAG, "onActivityResult: tempsplit = :" + temp[0]);
                if (temp.length == 3) {
                    experimentID = temp[0];
                    experimentType = temp[1];
                    experimentMode = temp[2];
                } else if (temp.length == 1) {
                    Log.d(TAG, "onActivityResult: temp - RYAN" + temp[0]);
                    DocumentSnapshot docRef = db.collection("Barcodes").document(temp[0]).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    task.getResult();
                                }
                            });
                    String[] barcodeToTrial = docRef.getString("contributingTrial").split("/");

                    experimentID = barcodeToTrial[0];
                    experimentType = barcodeToTrial[1];
                    experimentMode = barcodeToTrial[2];
                }
                for (Experiment experiment : experimentList) {
                    if (experiment.getUID() != null && experiment.getUID().contains(experimentID)) {
                        if (experimentMode.equals(VIEW)) {
                            experimentController.viewExperiment(this, experiment);
                        } else if (experimentMode.equals(PASS)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String date = sdf.format(new Date(System.currentTimeMillis()));
                            Trial trial = createTrialFromQR(experimentID, experimentType, localUID, experimentMode);
                            trial.setDate(date);
                            //TODO: Get Location from Android Built-in Locations
                            //Initialize fusedLocationProviderClient
                            com.example.experimentify.Location location = null;
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                //Permission Granted
                                location = getLocation();
                            } else {
                                //When permission is denied
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                            }
                            trialController.addTrialToDB(trial, Integer.parseInt(experimentMode), location);
                        } else if (experimentMode.equals(FAIL)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String date = sdf.format(new Date(System.currentTimeMillis()));
                            Trial trial = createTrialFromQR(experimentID, experimentType, localUID, experimentMode);
                            trial.setDate(date);
                            //TODO: Get Location from Android Built-in Locations
                            //Initialize fusedLocationProviderClient
                            com.example.experimentify.Location location = null;
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                //Permission Granted
                                location = getLocation();
                            } else {
                                //When permission is denied
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                            }
                            trialController.addTrialToDB(trial, Integer.parseInt(experimentMode), location);
                        }
//                        else if (temp.length == 1){
//                            //Read from collection called Barcodes
//                            DocumentSnapshot docRef = db.collection("Barcodes").document(temp[0]).get().getResult();
//                            String trialString = docRef.getString("contributingTrial");
//                            //TODO: Split the string. Then call add databaseTrialToDB.
//                            }
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


    @SuppressLint("MissingPermission")
    private com.example.experimentify.Location getLocation() {
        final com.example.experimentify.Location[] dataLocation = {null};
        //This function only gets called when permissions is true therefore we suppressed the warning.
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location from Android
                Location location = task.getResult();
                Address address = null;


                if(location != null){
                    //Initialize Geocoder
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        //Returns a list, but only one result so get the 0th element.
                        address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                        dataLocation[0] = new com.example.experimentify.Location(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return dataLocation[0];
    }

    //0 is fail, 1 is success, 2 is view.
    private Trial createTrialFromQR(String EID, String expType, String UID, String result){
        Trial trial = null;
        if (expType.equals("Count")){
            trial = new CountTrial(UID, EID);
        }
        else if (expType.equals("Binomial")){
            trial = new BinomialTrial(UID, EID, Integer.parseInt(result));
        }
        return trial;
    }

    //AddExpFragment
    @Override
    public void onOkPressed(Experiment newExp) {
        addExperiment(newExp);
        //TODO subscribe creator of experiment, maybe in addExperiment method
    }

    @Override
    public void onDeletePressed(Experiment exp) {
        delExperiment(exp);
    }

    //ExpOptionsFragment
    @Override
    public void onConfirmEdits(Experiment exp) {
        editExperiment(exp);
    }

    /**
     * open the SearchResults activity, which will query the database and show relevant experiments to the keyword the user input
     */
    public void openSearchResults(String keyword){
        Intent intent = new Intent(this, SearchResults.class);
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        bundle.putSerializable("user", currentUser);
        bundle.putString("searchType",searchSpinner.getSelectedItem().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * initialize a new user to the firestore, we do not force them to provide info like name, email, username, e.t.c until later, we are just make a document with a unique ID that can identify that specific user
     * @param db is the firestore db
     */
    public User initializeUser(FirebaseFirestore db){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        User user = new User();
        if(settings.contains("uid")){

            localUID = settings.getString("uid", "0");


            DocumentReference docRef = db.collection("Users").document(localUID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            user.setUid(localUID); // if the controlling if block is reached, then the document exists, which means localuid = uid stored in firestore
                            user.setEmail(document.getData().get("email").toString());
                            user.setName(document.getData().get("name").toString());
                            user.setParticipatingExperiments((ArrayList<String>) document.getData().get("participatingExperiment"));
                            user.setOwnedExperiments((ArrayList<String>) document.getData().get("ownedExperiments"));
                            user.setUsername(document.getData().get("username").toString());

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


        } // if close
        else{
            final CollectionReference collectionReference = db.collection("Users");  // connect to users collection

            Map<String,String> data = new HashMap<>();

            ArrayList<String> experiments = new ArrayList();

            Map<String, ArrayList<String>> uExps = new HashMap<>();


            DocumentReference documentReference = db.collection("Users").document();
            data.put("email","");    // put empty values for now
            data.put("name","");
            data.put("uid","");
            data.put("username","");
            data.put("cleanedUsername","");

            uExps.put("ownedExperiments", experiments);
            uExps.put("participatingExperiments", experiments);

            documentReference.set(data);

            String uid = documentReference.getId().toString();
            user.setUid(uid);  // every user should always have a uid which is the uid generated by firestore (unique)

            DocumentReference addID = db.collection("Users").document(uid);

            addID.update("uid", uid);
            addID.update("participatingExperiments", experiments);
            addID.update("ownedExperiments", experiments);


            SharedPreferences prefSettings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("uid", uid);

            // Apply the edits!
            editor.apply();



            // Get from the SharedPreferences
            String localUID = settings.getString("uid", "0");

        }
        return user;
    }

}