package com.example.experimentify;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapView map;
    private TextView date;
    private GoogleMap gMap;
    private Button ok;
    private Button cancelButton;
    private Button search;
    private DatePickerDialog.OnDateSetListener selectDate;
    private MarkerOptions last = null;
    private Experiment exp = null;
    private Location loc;
    private TrialController trialController;
    private String dateValue;
    private int trialResult;
    private EditText searchLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
        date = findViewById(R.id.trialDate);
        search = findViewById(R.id.searchLoc);
        ok = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancel);
        searchLoc = findViewById(R.id.searchMap);
        trialController = new TrialController();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            exp = intent.getParcelableExtra("experiment");
        }
        if(exp.isLocationRequired() == false){
            map.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
            searchLoc.setVisibility(View.GONE);

        }
        Log.d("exp123", "onCreate: ");
        
        selectDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateValue = year + "/" + (month + 1) + "/" + dayOfMonth;
                date.setText(dateValue);
            }

        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(0, i);
                finish();
                Toast.makeText(MapActivity.this, "Cancelled Trial Creation", Toast.LENGTH_SHORT).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateValue == null) {
                    Intent i = new Intent();
                    setResult(0, i);
                    Toast.makeText(MapActivity.this, "Cancelled Trial Creation: trial did not have date.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                if (loc == null && exp.isLocationRequired()) {
                    Intent i = new Intent();
                    setResult(0, i);
                    Toast.makeText(MapActivity.this, "Cancelled Trial Creation: trial did not have location", Toast.LENGTH_SHORT).show();
                    finish();
                }

                Intent i = new Intent();
                i.putExtra("date", dateValue);
                if (loc != null){
                    i.putExtra("location", loc);
                    setResult(2, i);
                    finish();
                }
                setResult(1, i);
                finish();
//                Toast.makeText(MapActivity.this, "Trial Successfully Created!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog calendarDialog = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener) selectDate, year, month, day);
        calendarDialog.show();
    }


    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

    /*
                implemented a map api to record trial location
                CITATION for map:
                CSE Labs, "Mapview with search option in android studio (Ex-13)",
                Feb-13-2020, Public Domain, https://www.youtube.com/watch?v=9dC1uvl-Qig&ab_channel=CSELAB
.
   */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gMap.setMyLocationEnabled(true);
    }

    public Location onMapReady(View view) {
        String location = searchLoc.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            loc = new Location(address);
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLong());
            MarkerOptions mark = new MarkerOptions().position(latLng).title("Trial Marker:" + location);
            gMap.clear();
            gMap.addMarker(mark);
            gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        }



        return loc;
    }
}
