package com.example.experimentify;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
    private Intent intent;

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
    public void checkSearchExp(){
        ImageButton searchButton = (ImageButton) solo.getView(R.id.searchButton);
        // we are starting in the main activity
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);


        solo.enterText((EditText) solo.getView(R.id.searchBar),"FiELds");
        solo.sleep(2000); // 2s

        solo.clickOnView(searchButton);
        solo.sleep(2000); // 2s

        assertTrue(solo.searchText("Fields"));

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

    @Test
    public void checkSearchUser(){
        ImageButton searchButton = (ImageButton) solo.getView(R.id.searchButton);




        // we are starting in the main activity
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);


        solo.enterText((EditText) solo.getView(R.id.searchBar),"tYsON");
        solo.sleep(2000); // 2s

        solo.pressSpinnerItem(0,1);

        Boolean selected = solo.isSpinnerTextSelected(0,"User");

        assertEquals("User item not selected on spinner",true,selected);


        solo.sleep(1000);

        solo.sleep(2000);

        solo.clickOnView(searchButton);
        solo.sleep(2000); // 2s

        assertTrue(solo.searchText("Mike"));

        // now click on the item in the list to open the activity
        // solo.clickInList(1);
        solo.sleep(2000); // 2s


        // first must be inside SearchResults activity

        solo.assertCurrentActivity("Wrong activity",SearchResults.class);


        // now should open ExperimentActivity
        solo.clickInList(1);
        solo.sleep(2000); // 2s
        solo.assertCurrentActivity("Wrong activity",SearchResults.class); // clicking on the item shouldn't open anything
    }
    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
