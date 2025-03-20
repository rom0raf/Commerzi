package com.commerzi.app.dto;

import com.commerzi.app.customers.Coordinates;

import java.util.List;

public class UpdateLocationDTO {

    private String routeId;

    private List<Coordinates> coordinates;

    public UpdateLocationDTO() {
    }

    public UpdateLocationDTO(String routeId, List<Coordinates> coordinates) {
        this.routeId = routeId;
        this.coordinates = coordinates;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

}
