package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.PlannedRoute;

public interface IPlannedRouteService {

    String createRoute(PlannedRoute route);

    PlannedRoute getPlannedRouteById(String id);

    void updateRoute(PlannedRoute route);

    void deleteRoute(PlannedRoute route);

}
