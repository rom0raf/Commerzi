package com.commerzi.app.customers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Coordinates implements Parcelable {
    private double latitude;
    private double longitude;

    public Coordinates() {}

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Coordinates)) {
            return false;
        }

        Coordinates other = (Coordinates) obj;

        return this.latitude == other.latitude && this.longitude == other.longitude;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
//        dest.writeParcelable(this, flags);
    }

     public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            double latitude = in.readDouble();
            double longitude = in.readDouble();
            return new Coordinates(latitude, longitude);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };
}
