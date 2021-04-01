package com.example.experimentify;

import android.location.Address;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

public class Location {
    private Address address;

    public Location(Address address) {
        this.address = address;
    }

    public GeoLocation getGeoLocation(Address address){
        return new GeoLocation(address.getLatitude(), address.getLongitude());

    }
}

