package com.example.experimentify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends DialogFragment {
    private Bundle bundle;
    private CheckBox subscribeBox;
    private CheckBox endExpBox;
    private CheckBox unpublishBox;
    private Button delExpButton;
    private Experiment experiment;

    private OnFragmentInteractionListener listener;



    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExp, Boolean delete);
        void onDeletePressed(Experiment current);
        void editItem(Experiment ogItem, Experiment editedItem );
    }


    public static UserProfileFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);

        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void handleDelete() {
        //TODO warning message for deleting experiment
        //https://stackoverflow.com/a/26097588
                        /*
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Alert message to be shown");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                         */
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_exp_options, null);
        subscribeBox = view.findViewById(R.id.subscribeCheckBox);
        endExpBox = view.findViewById(R.id.endExpCheckBox);
        unpublishBox = view.findViewById(R.id.unpublishExpCheckBox);
        delExpButton = view.findViewById(R.id.delExpButton);
        bundle = getArguments();




        if (bundle != null) {
            experiment = (Experiment) bundle.getSerializable("experiment");


            subscribeBox.setChecked(false); //TODO check if experiment is in user's subscribed list
            endExpBox.setChecked(experiment.isEnded());
            unpublishBox.setChecked(!experiment.isViewable());
        }

        delExpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ///
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Experiment Options")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean subscribed = subscribeBox.isChecked();
                        boolean ended = endExpBox.isChecked();
                        boolean unpublished = unpublishBox.isChecked();


                        //alertDialog.show();
                        //TODO Edit city
                    }
                }).create();
    }
}