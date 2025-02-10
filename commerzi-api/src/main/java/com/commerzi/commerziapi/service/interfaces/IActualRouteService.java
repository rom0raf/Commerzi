package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.PlannedRoute;

public interface IActualRouteService {

    String saveActualRoute(ActualRoute actualRoute);

    ActualRoute getActualRouteById(String plannedRouteId);

    ActualRoute createActualRouteFromPlannedRoute(PlannedRoute plannedRoute);

    ActualRoute skipVisit(int visit, ActualRoute route);
}
