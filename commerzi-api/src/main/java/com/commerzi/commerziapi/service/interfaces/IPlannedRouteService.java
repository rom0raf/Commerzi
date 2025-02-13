package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.model.PlannedRoute;

import java.io.IOException;
import java.util.List;

public interface IPlannedRouteService {

    String createRoute(List<String> customersId, CommerziUser user, boolean useRealDistance) throws Exception;

    List<PlannedRoute> getAll(String userId);

    PlannedRoute getPlannedRouteById(String id);

    void updateRoute(PlannedRoute route);

    void deleteRouteById(String id);

}
