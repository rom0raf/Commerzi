package com.commerzi.commerziapi.model.maps;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;

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
