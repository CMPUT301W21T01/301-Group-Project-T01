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
public class chatAnswerAdaptor extends ArrayAdapter<chatAnswer> {
    private ArrayList<chatAnswer> answers;
    private Context context;

    private TextView descBox;
    private TextView dateBox;
    private TextView userID;
    private TextView repliesCount;

    public chatAnswerAdaptor(Context context, ArrayList<chatAnswer> answers) {
        super(context, 0, answers);
        this.answers = answers;
        this.context = context;
    }

    /**
     * This method sets the TextViews of the question adapter.
     * @param answer Question whose data is displayed by the question adapter
     */
    public void setAnswersUi(chatAnswer answer) {
        descBox.setText(answer.getDescription());
        dateBox.setText(answer.getDate());
        userID.setText(answer.getUID());
    }

    //TODO add javadocs comment
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_question_content, parent, false);
        }

        chatAnswer answer = answers.get(position);

        descBox = view.findViewById(R.id.userAnswer);
        System.out.println("descbox..." + descBox);
        dateBox = view.findViewById(R.id.userAnswerDate);
        userID = view.findViewById(R.id.userAnswerID);

        setAnswersUi(answer);

        return view;
    }
}
