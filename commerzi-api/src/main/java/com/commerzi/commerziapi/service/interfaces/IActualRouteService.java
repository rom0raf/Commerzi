package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.ERouteStatus;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.model.maps.GPSRoute;

import java.util.List;

public interface IActualRouteService {

    String saveActualRoute(ActualRoute actualRoute) throws Exception;

    ActualRoute getActualRouteById(String actualRouteId);

    List<ActualRoute> getActualRoutesForUser(String userId);

    ActualRoute createActualRouteFromPlannedRoute(PlannedRoute plannedRoute);

    ActualRoute updateVisit(int visitId, String status, ActualRoute route);

    ActualRoute delete(ActualRoute route);

    List<GPSRoute> getGPSRoutes(ActualRoute route);

    ActualRoute skipVisit(ActualRoute route);

    ActualRoute confirmVisit(ActualRoute route);

    ActualRoute changeStatus(ActualRoute route, ERouteStatus status);
}
