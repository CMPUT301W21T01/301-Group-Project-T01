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

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * This class is a fragment that handles the UI responsible for subscribing to an experiment,
 * ending an experiment, unpublsihing and experiment, and deleting an experiment.
 * If a user does not own an experiment the only option available to them is subscription.
 */
public class ExpOptionsFragment extends DialogFragment {
    private Bundle bundle;
    private CheckBox subscribeBox;
    private CheckBox endExpBox;
    private CheckBox unpublishBox;
    private Button delExpButton;
    private Experiment experiment;
    private String localUID;
    private User user;
    private FirebaseFirestore db;

    private OnFragmentInteractionListener listener;


    public interface OnFragmentInteractionListener {
        void onConfirmEdits(Experiment exp);
        void onDeletePressed(Experiment current);
    }

    public static ExpOptionsFragment newInstance(Experiment experiment, String localUID, User user) {
        Bundle args = new Bundle();
        args.putParcelable("experiment", experiment);
        args.putString("localUID", localUID);
        args.putSerializable("user", user);

        ExpOptionsFragment fragment = new ExpOptionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method shows a confirmation method for deleting and experiment and
     * proceeds with deleting the experiment if the user clicks "YES".
     * @param exp experiment to delete
     */
    private void handleDelete(Experiment exp) {

        /*
            Author: MysticMagicϡ
            Date published: Sep 29 '14 at 10:20
            License: Attribution-ShareAlike 3.0 Unported
            Link: https://stackoverflow.com/a/26097588

            I used this post to help with creating a confirmation pop-up
        */
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Delete Experiment?");
        alertDialog.setMessage("Are you sure you want to delete this experiment?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        listener.onDeletePressed(exp);
                        dismiss(); //Closes fragment
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * This method sets the state of the checkboxes
     */
    private void setUi() {
        experiment.userIsSubscribed(localUID, new Experiment.GetDataListener() {
            @Override
            public void onSuccess(boolean result) {
                subscribeBox.setChecked(result);
            }
        });

        endExpBox.setChecked(!experiment.isEditable());
        unpublishBox.setChecked(!experiment.isViewable());

        //If user is the owner of the experiment show extra options
        if (localUID.equals(experiment.getOwnerID())){
            endExpBox.setVisibility(View.VISIBLE);
            unpublishBox.setVisibility(View.VISIBLE);
            delExpButton.setVisibility(View.VISIBLE);
        }
        else {
            endExpBox.setVisibility(View.GONE);
            unpublishBox.setVisibility(View.GONE);
            delExpButton.setVisibility(View.GONE);
        }
    }


    /**
     * This method handles the case of a user submitting their changes.
     * The edited experiment is passed to MainActivity where it is
     * uploaded to the database.
     */
    private void editExpHandler() {
        boolean editable = !endExpBox.isChecked();
        boolean viewable = !unpublishBox.isChecked();
        boolean isSubbed = subscribeBox.isChecked();

        experiment.setViewable(viewable);
        experiment.setEditable(editable);
        handleSubs(isSubbed);

        listener.onConfirmEdits(experiment);
    }

    /**
     * This method adds and deletes the current user's subscriptions from the database.
     * @param isSubbed true if subscribed box is checked, false if subscribe box is unchecked
     */
    private void handleSubs(boolean isSubbed) {
        if (isSubbed) {
            user.addSub(localUID, experiment.getUID(), db);
        }
        else {
            user.deleteSub(localUID, experiment.getUID(), db);
        }
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
        subscribeBox = view.findViewById(R.id.ignoreCheckBox);
        endExpBox = view.findViewById(R.id.endExpCheckBox);
        unpublishBox = view.findViewById(R.id.unpublishExpCheckBox);
        delExpButton = view.findViewById(R.id.okayButton);
        bundle = getArguments();
        db = FirebaseFirestore.getInstance();


        if (bundle != null) {
            experiment = bundle.getParcelable("experiment");
            localUID = bundle.getString("localUID");
            user = (User) bundle.getSerializable("user");
            setUi();
        }


        delExpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleDelete(experiment);
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
                        editExpHandler();

                    }
                }).create();
    }
}