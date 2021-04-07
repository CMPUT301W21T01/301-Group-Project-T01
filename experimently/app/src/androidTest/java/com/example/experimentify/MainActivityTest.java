package com.example.experimentify;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MainActivityTest {
    private Solo solo;
    private String description = "Intent test desc";
    private String region = "Intent test region";
    private String date = "2999/01/01";
    private String name = "Intent test name";
//    private Fragment addExpFragment;
    private FloatingActionButton addExpButton;
    private String TAG = "sample";

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class,
                    true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
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
        //
        View button = activity.findViewById(R.id.showAddExpUiButton);
        solo.clickOnView(button);
        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.addExpCL);
        assertTrue(fragment.isVisible());
        EditText description = (EditText) solo.getView(R.id.expDescription);
        solo.enterText(description, "Edmonton");

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
    @Test
    public void listTest() {
        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);

        addExpButton = rule.getActivity().findViewById(R.id.showAddExpUiButton);
        solo.clickOnView(addExpButton);


        //Fills fields
        solo.enterText((EditText) solo.getView(R.id.expDescription), description);
        solo.enterText((EditText) solo.getView(R.id.date), date);
        solo.enterText((EditText) solo.getView(R.id.region), region);

        //Confirm entries
        solo.clickOnButton("OK");

        //Check that experiment was created
        assertTrue(solo.searchText(description));
        assertTrue(solo.searchText(region));
        assertTrue(solo.searchText(date));
        assertTrue(solo.searchText(name));

        //ehhhh

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}


