package com.example.experimentify;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

public class MainActivityTest {
    private Solo solo;
    private String descriptionTest = "Intent test desc";
    private String regionTest = "Intent test region";
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
    }

    //Need to look into importance of this
    /*
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    */

    //TODO: not working, cannot find fragment.
    @Test
    public void addExpFragmentTest() {

        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);
        Activity activity = solo.getCurrentActivity();
        //showAddExpUiButton is the FloatActionButton
        View button = activity.findViewById(R.id.showAddExpUiButton);
//        Log.d(TAG, "addExpFragmentTest: " + button.getX() + button.getY());
        //Clicks on View since FloatActionButton is not considered a button.

        DisplayMetrics displayMetrics = new DisplayMetrics();
        int x = Resources.getSystem().getDisplayMetrics().widthPixels;
        int y = Resources.getSystem().getDisplayMetrics().heightPixels;
        int dpi = (int) Resources.getSystem().getDisplayMetrics().density;
//        System.out.println(x + "/" + y + "/" + dpi);
        int distButton = 30 * dpi;

//        solo.waitForView(button);
//        solo.clickOnScreen(x - distButton, y - distButton);
        solo.clickOnScreen(button.getX() + distButton, button.getY() + distButton);
        //Waits for Fragment
//        solo.waitForFragmentById(R.id.addExpCL);
        EditText description = (EditText) solo.getView(R.id.expDescription);
        TextView date = (TextView) solo.getView(R.id.date);
        EditText region = (EditText) solo.getView(R.id.region);
        EditText minTrials = (EditText) solo.getView(R.id.minTrials);
        View locationRequired = solo.getView(R.id.locationRequiredBox);
        Spinner expType = (Spinner) solo.getView(R.id.expType);
        //solo.pressSpinnerItem(0, -5)

        solo.enterText(description, descriptionTest);
        solo.clickOnView(date);
        solo.clickOnText("OK");
        solo.enterText(region, regionTest);
        solo.enterText(minTrials, Integer.toString(minTrialsTest));
//        solo.clickOnCheckBox(R.id.locationRequiredBox);
        solo.clickOnText("OK");

//        FragmentManager showFrag = activity.getFragmentManager().findFragmentById(R.id.mainActivityCL).getChildFragmentManager();



//        Fragment mainActivity = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.addExpCL);
//        FragmentManager = solo.getCurrentActivity().getFragmentManager().
//        boolean fragShow = solo.waitForFragmentById(R.id.addExpCL);
//        boolean viewShow = solo.waitForView(solo.waitForFragmentById(R.id.addExpCL));
//        Log.d(TAG, "fragShow: " + fragShow + " viewShow: " + viewShow + " mainActivity: " + mainActivity);
//
//        addExpFragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.addExpCL);


    }


    //TODO add testing to check if experiment is hidden when under the min number of trials
    //Allow owner to publish an experiment with description, a region, and minimun number of trials.
//    @Test
//    public void listTest() {
//        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);
//
//        addExpButton = rule.getActivity().findViewById(R.id.showAddExpUiButton);
//        solo.clickOnView(addExpButton);
//
//
//        //Fills fields
//        solo.enterText((EditText) solo.getView(R.id.expDescription), description);
//        solo.enterText((EditText) solo.getView(R.id.date), date);
//        solo.enterText((EditText) solo.getView(R.id.region), region);
//
//        //Confirm entries
//        solo.clickOnButton("OK");
//
//        //Check that experiment was created
//        assertTrue(solo.searchText(description));
//        assertTrue(solo.searchText(region));
//        assertTrue(solo.searchText(date));
//        assertTrue(solo.searchText(name));
//
//        //ehhhh
//
//    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
        db.clearPersistence();
        db.terminate();
        db.clearPersistence();
    }
}


