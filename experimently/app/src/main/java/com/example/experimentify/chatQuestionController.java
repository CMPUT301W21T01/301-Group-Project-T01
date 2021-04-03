package com.example.experimentify;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chatQuestionController {

    private ArrayList<chatQuestion> questions;
    private ArrayAdapter<chatQuestion> listAdapter;

    public chatQuestionController(Context context) {
        questions = new ArrayList<chatQuestion>();
        listAdapter = new chatQuestionAdaptor(context, questions);
    }

    public ArrayAdapter<chatQuestion> getAdapter() {
        return listAdapter;
    }

    public int getSize() {
        return questions.size();
    }

    public ArrayList<chatQuestion> getQuestions() {
        return questions;
    }

    /**
     * This method adds an experiment to the database.
     *
     * @param newQuestion experiment to bed added
     * @param db          the database the experiment will be saved to
     */
    public void addQuestionToDB(chatQuestion newQuestion, FirebaseFirestore db, String uid) {
        String EID = newQuestion.getEID();
        DocumentReference newRef = db.collection("Experiment").document(EID).collection("Questions").document();

        Map<String, Object> enterData = new HashMap<>();
        enterData.put("description", newQuestion.getDescription());
        enterData.put("uid", uid);
        enterData.put("date", newQuestion.getDate());
        enterData.put("eid", EID);
        newRef.set(enterData);
    }

    /**
     * sets the experiments variable
     */
    public void setQuestion(ArrayList<chatQuestion> set_experiments) {
        questions = set_experiments;
    }


    /**
     * This method returns an experiment based on its position in the ListView.
     *
     * @param pos position of experiment in ListView
     * @return experiment that was clicked on
     */
    public chatQuestion getClickedQuestion(int pos) {
        return questions.get(pos);
    }


}

