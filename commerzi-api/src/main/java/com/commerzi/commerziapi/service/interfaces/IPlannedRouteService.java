package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.PlannedRoute;

import java.util.List;

public interface IPlannedRouteService {

    String createRoute(PlannedRoute route);

    List<PlannedRoute> getAll(String userId);

    PlannedRoute getPlannedRouteById(String id);

    void updateRoute(PlannedRoute route);

    void deleteRoute(PlannedRoute route);

}
