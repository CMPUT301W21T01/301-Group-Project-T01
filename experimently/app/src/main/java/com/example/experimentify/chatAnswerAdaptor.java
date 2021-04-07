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
 * This class is an ArrayAdapter for answers.
 */
public class chatAnswerAdaptor extends ArrayAdapter<chatAnswer> {
    private ArrayList<chatAnswer> answers;
    private Context context;

    private TextView descBox;
    private TextView dateBox;
    private TextView userID;

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
        descBox.setText("Answer: " + System.lineSeparator() + answer.getDescription());
        dateBox.setText("Date: "+answer.getDate());
        userID.setText("User: "+answer.getUID());
    }

    /**
     * This method sets the overall view of the answer adapter.
     * @param position the location of the object in list
     * @param convertView is the view converter can be null
     * @param convertView is the view groups parent can be null
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_answer_content, parent, false);
        }

        chatAnswer answer = answers.get(position);

        descBox = view.findViewById(R.id.userAnswer);
        //System.out.println("descbox..." + descBox);
        dateBox = view.findViewById(R.id.userAnswerDate);
        userID = view.findViewById(R.id.userAnswerID);

        setAnswersUi(answer);

        return view;
    }
}
