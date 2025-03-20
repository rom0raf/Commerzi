package com.commerzi.app.route.actualRoute.maps;

import java.util.List;

public class Intersection {

    private List<Boolean> entry;
    private List<Integer> bearings;
    private List<Double> location;
    private int in;
    private int out;

    public Intersection() {
    }

    public List<Boolean> getEntry() {
        return entry;
    }

    public void setEntry(List<Boolean> entry) {
        this.entry = entry;
    }

    public List<Integer> getBearings() {
        return bearings;
    }

    public void setBearings(List<Integer> bearings) {
        this.bearings = bearings;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "entry=" + entry +
                ", bearings=" + bearings +
                ", location=" + location +
                ", in=" + in +
                ", out=" + out +
                '}';
    }
}
