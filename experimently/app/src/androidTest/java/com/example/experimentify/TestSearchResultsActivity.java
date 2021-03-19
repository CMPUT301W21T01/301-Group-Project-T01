package com.example.experimentify;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests SearchResults activity, (checks to see if we switch to activity)
 * (passes assertion for a valid occurence of a search),
 * tests opening the ExperimentActivty and that we switched to it
 */
public class TestSearchResultsActivity {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    @Test
    public void start(){
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkSearch(){
        ImageButton searchButton = (ImageButton) solo.getView(R.id.searchButton);
        // we are starting in the main activity
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);


        solo.enterText((EditText) solo.getView(R.id.searchBar),"Nice");
        solo.sleep(2000); // 2s

        solo.clickOnView(searchButton);
        solo.sleep(2000); // 2s

        assertTrue(solo.searchText("Nice"));

        // now click on the item in the list to open the activity
        // solo.clickInList(1);
        solo.sleep(2000); // 2s


        // first must be inside SearchResults activity

        solo.assertCurrentActivity("Wrong activity",SearchResults.class);


        // now should open ExperimentActivity
        solo.clickInList(1);
        solo.sleep(2000); // 2s
        solo.assertCurrentActivity("Wrong activity",ExperimentActivity.class);
    }
    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
