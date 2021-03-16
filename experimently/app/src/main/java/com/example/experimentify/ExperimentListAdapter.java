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
 * This class is an ArrayAdapter for Experiments.
 */
public class ExperimentListAdapter extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;
    //private String exDescription;

    private TextView descBox;
    private TextView regionBox;
    private TextView dateBox;

    public ExperimentListAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    //TODO add javadocs comment
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.experiment_list_content, parent, false);
        }

        Experiment experiment = experiments.get(position);

        //TODO show status and owner of experiment in list
        descBox = view.findViewById(R.id.generalExDescription);
        regionBox = view.findViewById(R.id.generalExLocation);
        dateBox = view.findViewById(R.id.generalExDate);

        descBox.setText(experiment.getDescription());
        regionBox.setText(experiment.getRegion());
        dateBox.setText(experiment.getDate());

        return view;
    }
}
