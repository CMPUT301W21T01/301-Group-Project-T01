package com.example.experimentify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
    /*
  Author: Abdul Ali Bangash
  Date published: Feb. 2, 2021
  License: Public domain
  Link: https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
  Code for this class is based on examples from lab 3.
  */
/**
 * This class is an ArrayAdapter for Questions.
 */
public class chatQuestionAdaptor extends ArrayAdapter<chatQuestion> {
    private ArrayList<chatQuestion> questions;
    private Context context;

    private TextView descBox;
    private TextView dateBox;
    private TextView userID;
    private TextView repliesCount;

    public chatQuestionAdaptor(Context context, ArrayList<chatQuestion> questions) {
        super(context, 0, questions);
        this.questions = questions;
        this.context = context;
    }

    /**
     * This method sets the TextViews of the question adapter.
     * @param question Question whose data is displayed by the question adapter
     */
    public void setQuestionUi(chatQuestion question) {
        descBox.setText(question.getDescription());
        dateBox.setText(question.getDate());
        userID.setText(question.getUID());
        //int Temp = question.getNumReplies();
        repliesCount.setText(String.valueOf(question.getNumReplies()));
        System.out.println("replies..." + String.valueOf(question.getNumReplies()));
        //repliesCount.setText("1");
    }

    /**
     * This method sets the overall view of the question adapter.
     * @param position the location of the object in list
     * @param convertView is the view converter can be null
     * @param convertView is the view groups parent can be null
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_question_content, parent, false);
        }

        chatQuestion question = questions.get(position);

        descBox = view.findViewById(R.id.userQuestion);
        //System.out.println("descbox..." + descBox);
        dateBox = view.findViewById(R.id.userQuestionDate);
        userID = view.findViewById(R.id.userQuestionID);
        repliesCount = view.findViewById(R.id.userQuestionReplies);

        setQuestionUi(question);

        return view;
    }
}
