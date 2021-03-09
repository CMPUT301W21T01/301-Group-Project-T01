package com.example.experimentify;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AddExpFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText experimentName;
    private EditText region;
    private EditText date;
    private EditText expType;
    private DatePickerDialog.OnDateSetListener selectDate;
    private OnFragmentInteractionListener listener;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String txt = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExp);
        void onDeletePressed(Experiment current);
        void editItem(Experiment ogItem, Experiment editedItem );
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_exp_fragment, null);
        experimentName = view.findViewById(R.id.expName);
        date = view.findViewById(R.id.date);
        region = view.findViewById(R.id.region);
        expType = view.findViewById(R.id.expType);

        Spinner spinner = view.findViewById(R.id.expType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.experiments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //creating a date picker dialog using DatePickerDialog class
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog calendarDialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) selectDate, year, month, day);
                calendarDialog.show();
            }
        });
        selectDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dat = year + "  " + (month + 1) + "  " + dayOfMonth;
                date.setText(dat);
            }

        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = experimentName.getText().toString();
                        String date1 = date.getText().toString();
                       // String region1 = region
                        listener.onOkPressed(new Experiment(name, "North America", date1));
                    }}).create();
    }


}