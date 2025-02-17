package com.commerzi.app.route.actualRoute.maps;

import java.util.List;

public class Leg {

    private String summary;
    private double distance;
    private double duration;
    private double weight;
    private List<Step> steps;

    public Leg() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "summary='" + summary + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                ", weight=" + weight +
                ", steps=" + steps +
                '}';
    }
}
