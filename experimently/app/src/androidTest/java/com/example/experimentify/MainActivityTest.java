package com.example.experimentify;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.rule.ActivityTestRule;
import android.app.Fragment;
import android.widget.EditText;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainActivityTest {
    private Solo solo;
    private String description = "Intent test desc";
    private String region = "Intent test region";
    private String date = "2021/01/01";
    private String name = "Intent test name";
    private Fragment addExpFragment;

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

    @Test
    public void addExpFragmentTest() {
        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);
        //https://stackoverflow.com/a/19052227
        addExpFragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.addExpFragment);

        //Tests that the addExpFragment is initially not visible.
        assertFalse(addExpFragment.isVisible());

        //Tests that addExpFragment is shown after the button to create an experiment is clicked.
        solo.clickOnButton(R.id.showAddExpUiButton);
        assertTrue(addExpFragment.isVisible());

    }


    //TODO add testing to check if experiment is hidden when under the min number of trials
    //Allow owner to publish an experiment with description, a region, and minimun number of trials.
    @Test
    public void listTest() {
        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);

        solo.clickOnButton(R.id.showAddExpUiButton);


        //Fills fields
        solo.enterText((EditText) solo.getView(R.id.expName), name);
        solo.enterText((EditText) solo.getView(R.id.expDescription), description);
        solo.enterText((EditText) solo.getView(R.id.date), date);
        solo.enterText((EditText) solo.getView(R.id.region), region);

        //
        solo.clickOnButton(R.id.showAddExpUiButton);


    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}


