package com.commerzi.commerziapi.model;

/**
 * Model class representing GPS coordinates.
 */
public class GpsCoordinates {
    private double latitude;
    private double longitude;

    /**
     * Gets the latitude of the GPS coordinates.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the GPS coordinates.
     *
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the GPS coordinates.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the GPS coordinates.
     *
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}