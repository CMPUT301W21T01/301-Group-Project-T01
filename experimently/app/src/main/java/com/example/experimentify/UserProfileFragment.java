package com.example.experimentify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends DialogFragment {
    private Bundle bundle;
    private TextView userID;
    private EditText userEmail;
    private EditText userName;
    private EditText userUsername;
    private User user;

    private FirebaseFirestore db;

    private OnFragmentInteractionListener listener;



    public interface OnFragmentInteractionListener {
        //no methods currently necessary here but keeping it just in case this changes in future
    }

    /**
     * Handles sending updated user profile data to Firebase after changes made in fragment
     * @param userID userID to apply changes to
     * @param username new username
     * @param email new email
     */
    public void updateFirebaseUser(String userID, String username, String email, String userusername)
    {
        //Update firebase name and email
        db = FirebaseFirestore.getInstance();
        DocumentReference userReference = db.collection("Users").document(userID);

        //Update name
        userReference.update("name", username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        //Update username
        userReference.update("username", userusername)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        //Update cleaned username
        String cleanUserUsername = userusername.trim().toLowerCase();

        userReference.update("cleanedUsername", cleanUserUsername)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        //Update email
        userReference.update("email", email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


    public static UserProfileFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);

        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(args);
        return fragment;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_profile, null);
        userID = view.findViewById(R.id.userID);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userUsername = view.findViewById(R.id.userUsername);
        bundle = getArguments();

        if (bundle != null) {
            user = (User) bundle.getSerializable("user");


            userID.setText(user.getUid());
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
            userUsername.setText(user.getUsername());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("User Profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Update local name and email
                        user.setName(userName.getText().toString());
                        user.setEmail(userEmail.getText().toString());
                        user.setUsername(userUsername.getText().toString());

                        updateFirebaseUser(user.getUid(), userName.getText().toString(), userEmail.getText().toString(), userUsername.getText().toString());

                    }
                }).create();
    }
}