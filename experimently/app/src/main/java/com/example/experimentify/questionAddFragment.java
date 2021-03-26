package com.example.experimentify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link questionAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class questionAddFragment extends DialogFragment {

    private questions questions;
    private EditText questionInput;

    private questionAddFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onOkPressed(Experiment newExperiment);
    }


    static questionAddFragment newInstance(questions question) {
        Bundle args = new Bundle();
        args.putSerializable("Question", question);
        questionAddFragment fragment = new questionAddFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_question_add, null);
        // Inflate the layout for this fragment
        questionInput =view.findViewById(R.id.questionEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("New/Edit Trial")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }



    }
}