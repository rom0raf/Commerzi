package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.model.PlannedRoute;

import java.util.List;

public interface IPlannedRouteService {

    String createRoute(List<String> customersId, String name, CommerziUser user) throws Exception;

    List<PlannedRoute> getAll(String userId);

    PlannedRoute getPlannedRouteById(String id);

    PlannedRoute updateRoute(String name, PlannedRoute route) throws Exception;

    void deleteRouteById(String id);

}
