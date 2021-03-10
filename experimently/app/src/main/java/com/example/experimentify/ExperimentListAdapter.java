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

//Class code based on lab 3 code, add citation
public class ExperimentListAdapter extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;
    private String exName;
    //private String exDescription;

    private TextView nameBox;
    private TextView regionBox;
    private TextView dateBox;

    public ExperimentListAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.experiment_list_content, parent, false);
        }

        Experiment experiment = experiments.get(position);

        nameBox = view.findViewById(R.id.generalExName);
        regionBox = view.findViewById(R.id.generalExLocation);
        dateBox = view.findViewById(R.id.generalExDate);

        nameBox.setText(experiment.getName());
        regionBox.setText(experiment.getRegion());
        dateBox.setText(experiment.getDate());

        return view;
    }
}
