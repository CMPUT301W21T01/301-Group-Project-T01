package com.example.experimentify;

public class BinomialTrial extends Trial{
    private int fail ;
    private int pass;
    public BinomialTrial(String userId, String eID, Location trialLocation) {
        super(userId, eID);
    }

    public void addFail(){
        fail = 1;
    }

    public void addPass(){
        pass = 1;
    }

    public int getPass(){
        return pass;
    }

    public int getFail(){
        return fail;
    }
}


