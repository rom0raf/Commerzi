package com.commerzi.commerziapi.maps.coordinates;

public class Coordinates {
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
}
