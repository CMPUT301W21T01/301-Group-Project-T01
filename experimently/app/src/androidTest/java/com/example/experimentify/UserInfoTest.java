package com.example.experimentify;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserInfoTest {
    private Solo solo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = UserInfoTest.class.getName();


    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class,
                    true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        db = FirebaseFirestore.getInstance();
        db.disableNetwork();
        db.clearPersistence();
    }

    @Test
    public void addUserInfoTest(){
        solo.assertCurrentActivity("Not in MainActivity", MainActivity.class);
        Activity activity = solo.getCurrentActivity();
        View button = (View) solo.getView(R.id.userProfileButton);
        solo.clickOnView(button);

        String emailTest = "123@123.com";
        String userNameTest = "John Doe";
        String userUsernameTest = "jDoe";

        TextView userEmail = (TextView) solo.getView(R.id.userEmail);
        TextView userName = (TextView) solo.getView(R.id.userName);
        TextView userUsername = (TextView) solo.getView(R.id.userUsername);

        userEmail.setText(emailTest);
        userName.setText(userNameTest);
        userUsername.setText(userUsernameTest);

        //Cannot connect with DB.
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
        db.clearPersistence();
        db.terminate();
        db.clearPersistence();
    }
}
