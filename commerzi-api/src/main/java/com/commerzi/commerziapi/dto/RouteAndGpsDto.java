package com.commerzi.commerziapi.dto;

import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.maps.GPSRoute;

import java.util.List;

public class RouteAndGpsDto {

    ActualRoute route;

    List<GPSRoute> gpsRoutes;

    public RouteAndGpsDto() {
    }

    public RouteAndGpsDto(ActualRoute route, List<GPSRoute> gpsRoutes) {
        this.route = route;
        this.gpsRoutes = gpsRoutes;
    }

    public ActualRoute getRoute() {
        return route;
    }

    public void setRoute(ActualRoute route) {
        this.route = route;
    }

    public List<GPSRoute> getGpsRoutes() {
        return gpsRoutes;
    }

    public void setGpsRoutes(List<GPSRoute> gpsRoutes) {
        this.gpsRoutes = gpsRoutes;
    }

}
