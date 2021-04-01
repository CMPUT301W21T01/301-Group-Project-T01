package com.example.experimentify;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private Address address;

    public Location(Address address) {
        this.address = address;
    }

    public LatLng getLatLng(Address address){
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        return  latLng;
    }

    public double getLatitude(){
        double latitude = address.getLatitude();
        return  latitude;
    }

    public double getLong(){
        double longitude = address.getLongitude();
        return longitude;
    }
}

