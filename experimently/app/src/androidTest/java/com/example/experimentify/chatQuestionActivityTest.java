package com.example.experimentify;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;


/**
 * Test class for chatQuestionActivity. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class chatQuestionActivityTest {

    private Solo solo;


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }
    /**
     * Add a question to the listview and check the userid, date, description using assert true
     */
    @Test
    public void checkList(){


        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickInList(0);
        solo.clickOnButton("Chat");

        solo.enterText((EditText) solo.getView(R.id.questionInput), "Hello World");

        solo.clickOnButton("Enter");

        solo.clearEditText((EditText)solo.getView(R.id.questionInput));

        chatQuestionActivity current = (chatQuestionActivity) solo.getCurrentActivity();

        final ListView questionsListView = current.questionsListView;

        chatQuestion question = (chatQuestion) questionsListView.getItemAtPosition(0);

        assert(question.getDescription()).equals("Hello World");
        assertTrue(solo.waitForText("Hello World", 1, 2000));



    }

}//