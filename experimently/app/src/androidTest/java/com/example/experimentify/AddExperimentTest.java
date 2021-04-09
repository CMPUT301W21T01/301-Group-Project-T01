package com.example.experimentify;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AddExperimentTest {
    private Solo solo;
    private String descriptionTest = "Intent test desc";
    private String regionTest = "Intent test region";
    private String dateTest = "01/01/2000";
    private int minTrialsTest = 5;

    //    private Fragment addExpFragment;
    private FloatingActionButton addExpButton;
    private String TAG = "sample";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class,
                    true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        db.disableNetwork();
        db.clearPersistence();
    }

    @Test
    public void addBinomialExpFragmentTest() {
        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);
        Activity activity = solo.getCurrentActivity();
        //showAddExpUiButton is the FloatActionButton
        View button = activity.findViewById(R.id.showAddExpUiButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        int x = Resources.getSystem().getDisplayMetrics().widthPixels;
        int y = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi = (int) Resources.getSystem().getDisplayMetrics().density;

        int distButton = 30 * dpi;


        //Clicks on View since FloatActionButton is not considered a button.
        solo.clickOnScreen(button.getX() + distButton, button.getY() + distButton);
        //Waits for Fragment
//        solo.waitForFragmentById(R.id.addExpCL);
        EditText description = (EditText) solo.getView(R.id.expDescription);
        TextView date = (TextView) solo.getView(R.id.date);
        EditText region = (EditText) solo.getView(R.id.region);
        EditText minTrials = (EditText) solo.getView(R.id.minTrials);
        View locationRequired = solo.getView(R.id.locationRequiredBox);

        solo.enterText(description, descriptionTest);
        date.setText(dateTest);
        solo.enterText(region, regionTest);
        solo.enterText(minTrials, Integer.toString(minTrialsTest));
        //Checks Location Required checkbox
        solo.clickOnCheckBox(0);
        //Selects Binomial trial
        solo.pressSpinnerItem(1, 2);
        solo.clickOnText("OK");

        assertTrue(solo.searchText(descriptionTest));
        assertTrue(solo.searchText(regionTest));
        assertTrue(solo.searchText(dateTest));
    }

    @Test
    public void addCountExpFragmentTest() {
        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);
        Activity activity = solo.getCurrentActivity();
        //showAddExpUiButton is the FloatActionButton
        View button = activity.findViewById(R.id.showAddExpUiButton);

        int x = Resources.getSystem().getDisplayMetrics().widthPixels;
        int y = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi = (int) Resources.getSystem().getDisplayMetrics().density;
        int distButton = 30 * dpi;


        //Clicks on View since FloatActionButton is not considered a button.
        solo.clickOnScreen(button.getX() + distButton, button.getY() + distButton);
        EditText description = (EditText) solo.getView(R.id.expDescription);
        TextView date = (TextView) solo.getView(R.id.date);
        EditText region = (EditText) solo.getView(R.id.region);
        EditText minTrials = (EditText) solo.getView(R.id.minTrials);
        View locationRequired = solo.getView(R.id.locationRequiredBox);

        solo.enterText(description, descriptionTest);
        date.setText(dateTest);
        solo.enterText(region, regionTest);
        solo.enterText(minTrials, Integer.toString(minTrialsTest));
        //Checks Location Required checkbox
        solo.clickOnCheckBox(0);
        //Selects Binomial trial
        solo.pressSpinnerItem(1, 3);
        solo.clickOnText("OK");

        assertTrue(solo.searchText(descriptionTest));
        assertTrue(solo.searchText(regionTest));
        assertTrue(solo.searchText(dateTest));
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
        db.clearPersistence();
        db.terminate();
        db.clearPersistence();
    }
}


