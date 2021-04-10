package com.example.experimentify;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class Location implements Parcelable {
    private Address address;
    private double longitude;
    private double latitude;
    private GeoPoint geoPoint;

    public Location(Address address) {
        this.address = address;
        longitude = address.getLongitude();
        latitude = address.getLatitude();
    }


//    public GeoLocation getGeoLocation(Address address){
//        return new GeoLocation(address.getLatitude(), address.getLongitude());
//
//    }

    protected Location(Parcel in) {
        address = in.readParcelable(Address.class.getClassLoader());
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, flags);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    /**
     * The method gets the latitudue of a location
     * @return latitude
     */
    public double getLatitude(){
        double latitude = address.getLatitude();
        return latitude;
    }
    /**
     * The method gets the longitude of a location
     * @return longitude
     */
    public double getLong(){
        double longitude = address.getLongitude();
        return longitude;
    }
    /**
     * The method gets the geo point which is a long and lat in a single object
     * @return geoPoint
     */
    public GeoPoint getGeoPoint(){
        geoPoint = new GeoPoint(latitude, longitude);
        return geoPoint;
    }
}

