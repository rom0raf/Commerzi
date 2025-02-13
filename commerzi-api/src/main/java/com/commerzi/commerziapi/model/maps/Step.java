package com.commerzi.commerziapi.model.maps;

import java.util.List;

public class Step {

    private String mode;
    private double distance;
    private double duration;
    private String name;
    private double weight;
    private String geometry;
    private String driving_side;
    private List<Intersection> intersections;
    private Maneuver maneuver;

    public Step() {
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getDriving_side() {
        return driving_side;
    }

    public void setDriving_side(String driving_side) {
        this.driving_side = driving_side;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public Maneuver getManeuver() {
        return maneuver;
    }

    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
    }

    @Override
    public String toString() {
        return "Step{" +
                "mode='" + mode + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", geometry='" + geometry + '\'' +
                ", driving_side='" + driving_side + '\'' +
                ", intersections=" + intersections +
                ", maneuver=" + maneuver +
                '}';
    }
}
