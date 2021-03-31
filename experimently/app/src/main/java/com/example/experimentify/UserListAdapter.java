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
  Author: riley and kenny


  Modified code from Abdul Ali Bangash  for the purposes of handling our "user class" in our app
  License: Public domain
  Link: https://eclass.srv.ualberta.ca/pluginfile.php/6713985/mod_resource/content/1/Lab%203%20instructions%20-%20CustomList.pdf
  Code for this class is based on examples from lab 3.
  */
/**
 * This class is an ArrayAdapter for Experiments.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    private ArrayList<User> Users;
    private Context context;
    //private String exDescription;

    private TextView descBox;
    private TextView regionBox;
    private TextView dateBox;

    public UserListAdapter(Context context, ArrayList<User> Users) {
        super(context, 0, Users);
        this.Users = Users;
        this.context = context;
    }

    /**
     * This method sets the TextViews of the experiment adapter.
     * @param user Experiment whose data is displayed by the experiment adapter
     */
    public void setUi(User user) {
        descBox.setText(context.getResources().getString(R.string.name_header) + user.getName());
        regionBox.setText(context.getResources().getString(R.string.email_header) + user.getEmail());
        dateBox.setText(context.getResources().getString(R.string.uid_header) + user.getUid());
    }

    //TODO add javadocs comment
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.experiment_list_content, parent, false);
        }

        User user = Users.get(position);

        //TODO show status and owner of experiment in list
        descBox = view.findViewById(R.id.generalExDescription);
        regionBox = view.findViewById(R.id.generalExLocation);
        dateBox = view.findViewById(R.id.generalExDate);

        setUi(user);

        return view;
    }
}
