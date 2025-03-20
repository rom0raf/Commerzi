package com.commerzi.app.route.actualRoute.maps;

import com.commerzi.app.customers.Coordinates;

import java.util.List;

public class Geometry {

    private List<double[]> coordinates;

    public Geometry() {
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }

}
