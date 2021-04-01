package com.example.experimentify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapView map;
    private TextView date;
    private GoogleMap gMap;
    private Button ok;
    private Button cancelButton;
    private Button search;

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

        Intent intent = getIntent();
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Trial Marker"));
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

    public void onMapReady(View view) {
        EditText searchLoc = findViewById(R.id.searchMap);
        String location = searchLoc.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            Location loc = new Location(address);
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLong());
            MarkerOptions mark = new MarkerOptions().position(latLng).title("Trial Marker:" + location);
            gMap.addMarker(mark);
            gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        }


    }
}