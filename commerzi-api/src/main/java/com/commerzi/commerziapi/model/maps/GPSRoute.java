package com.commerzi.commerziapi.model.maps;

import java.util.List;

public class GPSRoute {

    private double distance;

    private double duration;

    private List<Leg> legs;

    public GPSRoute() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    @Override
    public String toString() {
        return "GPSRoute{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", legs=" + legs +
                '}';
    }
}
