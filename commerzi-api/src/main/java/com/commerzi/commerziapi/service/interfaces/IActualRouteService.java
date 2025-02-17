package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.model.maps.GPSRoute;

import java.util.List;

public interface IActualRouteService {

    String saveActualRoute(ActualRoute actualRoute) throws Exception;

    ActualRoute getActualRouteById(String actualRouteId);

    ActualRoute createActualRouteFromPlannedRoute(PlannedRoute plannedRoute);

    ActualRoute updateVisit(int visitId, String status, ActualRoute route);

    List<GPSRoute> getGPSRoutes(ActualRoute route);
}
