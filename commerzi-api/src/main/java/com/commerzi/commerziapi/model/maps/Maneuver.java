package com.commerzi.commerziapi.model.maps;

import java.util.List;

public class Maneuver {

    private int bearing_after;
    private int bearing_before;
    private List<Double> location;
    private String type;

    public Maneuver() {
    }

    public int getBearing_after() {
        return bearing_after;
    }

    public void setBearing_after(int bearing_after) {
        this.bearing_after = bearing_after;
    }

    public int getBearing_before() {
        return bearing_before;
    }

    public void setBearing_before(int bearing_before) {
        this.bearing_before = bearing_before;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Maneuver{" +
                "bearing_after=" + bearing_after +
                ", bearing_before=" + bearing_before +
                ", location=" + location +
                ", type='" + type + '\'' +
                '}';
    }
}
