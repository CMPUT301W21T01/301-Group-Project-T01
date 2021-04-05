package com.example.experimentify;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chatAnswerController {

    private ArrayList<chatAnswer> answers;
    private ArrayAdapter<chatAnswer> listAdapter;

    public chatAnswerController(Context context) {
        answers = new ArrayList<chatAnswer>();
        listAdapter = new chatAnswerAdaptor(context, answers);
    }
    /**
     * THIS CONSTRUCTOR WITH EMPTY PARAMETERS IS FOR TESTING PURPOSES ONLY, DON'T USE IT IN PRODUCTION (CANT MOCK CONTEXT CURRENTLY) IN ExperimentControllerTests
     */
    public chatAnswerController() {
        answers = new ArrayList<chatAnswer>();
    }

    public ArrayAdapter<chatAnswer> getAdapter() {
        return listAdapter;
    }

    public int getSize() {
        return answers.size();
    }

    public ArrayList<chatAnswer> getAnswers() {
        return answers;
    }

    /**
     * This method adds an answer to the database.
     *
     * @param newAnswer experiment to bed added
     * @param db          the database the experiment will be saved to
     */
    public void addAnswerToDB(chatAnswer newAnswer, FirebaseFirestore db) {
        String EID = newAnswer.getEID();
        String QID = newAnswer.getQID();
        DocumentReference newRef = db.collection("Experiments").document(EID).collection("Questions").document(QID).collection("Answers").document();
        db.collection("Experiments").document(EID).collection("Questions").document(newAnswer.getQID()).update("answerTotal", FieldValue.increment(1));
        listAdapter.notifyDataSetChanged();

        Map<String, Object> enterData = new HashMap<>();
        enterData.put("description", newAnswer.getDescription());
        enterData.put("uid", newAnswer.getUID());
        enterData.put("date", newAnswer.getDate());
        enterData.put("eid", EID);
        newRef.set(enterData);


    }

    /**
     * sets the answers variable
     */
    public void setAnswer(ArrayList<chatAnswer> set_answers) {
        answers = set_answers;
    }


    /**
     * This method returns an answer based on its position in the ListView.
     *
     * @param pos position of experiment in ListView
     * @return experiment that was clicked on
     */
    public chatAnswer getClickedAnswer(int pos) {
        return answers.get(pos);
    }


}

