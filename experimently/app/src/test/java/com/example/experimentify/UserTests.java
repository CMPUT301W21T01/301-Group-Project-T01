package com.example.experimentify;
import static org.junit.Assert.*;

import org.junit.Test;



public class UserTests {

    public User mockUser() {
      return new User();//not strictly necessary to have this function as we have a very simple user constructor, but good practice in case this changes in future
    };

    /**
     * tests getting and setting email
     */
    @Test
    public void testEmail(){
        // make mock variables
        User user = mockUser();
        //base case
        assertEquals(user.getEmail(), "");
        //test adding
        String test = "Test Email";
        user.setEmail(test);
        assertEquals(user.getEmail(), test);
    }

    /**
     * tests getting and setting name
     */
    @Test
    public void testName(){
        // make mock variables
        User user = mockUser();
        //base case
        assertEquals(user.getName(), "");
        //test adding
        String test = "Test Name";
        user.setName(test);
        assertEquals(user.getName(), test);
    }

    /**
     * tests getting and setting username
     */
    @Test
    public void testUsername(){
        // make mock variables
        User user = mockUser();
        //base case
        assertEquals(user.getUsername(), "");
        //test adding
        String test = "Test Username";
        user.setUsername(test);
        assertEquals(user.getUsername(), test);
    }

    /**
     * tests getting and setting uid
     */
    @Test
    public void testUid(){
        // make mock variables
        User user = mockUser();
        //base case
        assertEquals(user.getUid(), "");
        //test adding
        String test = "Test UID";
        user.setUid(test);
        assertEquals(user.getUid(), test);

    }
}