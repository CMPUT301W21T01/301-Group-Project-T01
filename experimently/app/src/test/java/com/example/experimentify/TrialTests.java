package com.example.experimentify;
import android.location.Address;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.*;

import static org.junit.Assert.*;
//Experiment newExp, FirebaseFirestore db, String ownerID

/**
 * test for experiment controller methods (addtodb, edit in db, delete from db)
 * currently not working yet because cant initialize experimentControllerObject
 */
public class TrialTests {

    private Trial mockCountTrial() {
        return new CountTrial("UID", "EXPID");
    }

    private Trial mockBinomialTrialPass(){
        return new BinomialTrial("UID1", "EXPID1", 1);
    }
    private Trial mockBinomialTrialFail(){
        return new BinomialTrial("UID1.5", "EXPID1.5", -1);
    }

    private Trial mockIntegerTrial(){
        return new IntegerTrial("UID2", "EXPID2", 25);
    }

    private Trial mockMeasurementTrial(){
        return new MeasurementTrial("UID3", "EXPID3", (float) 12.5);
    }

    /**
     * tests get and set Date
     */
    @Test
    public void testDate(){
        String date = "01/22/2000";
        mockIntegerTrial().setDate(date);
       assertEquals(date, mockIntegerTrial().getDate());
    }

    /**
     * tests set and get UID
     */
    @Test
    public void testUID(){
        String UID = "User12";
        mockCountTrial().setUID(UID);
        assertEquals(UID, mockCountTrial().getUID());
    }

    /**
     * tests get and set trial location
     */
    @Test
    public void testLocation(){
        //Location loc = new Address(53.5461245,-113.49382290000001);
       // mockBinomialTrialFail().setTrialLocation(loc);
        //assertEquals(loc, mockBinomialTrialPass().getTrialLocation());
    }

    /**
     * tests TID
     */
    @Test
    public void tesTID(){
        String TID = "Trial Count";
       mockCountTrial().setTID(TID);
       assertEquals(TID, mockCountTrial().getTID());

    }

    /**
     * tests EID
     */
    @Test
    public void tesEID(){
        String EID = "Experiment 24";
        mockCountTrial().setEID(EID);
        assertEquals(EID, mockCountTrial().getEID());
    }
}