package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.dao.PlannedRouteRepository;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.interfaces.IPlannedRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing planned routes in the Commerzi application.
 */
@Service
public class PlannedRouteService implements IPlannedRouteService {

    @Autowired
    private PlannedRouteRepository plannedRouteRepository;

    /**
     * Creates a new planned route.
     *
     * @param route the planned route to create
     * @return the ID of the created planned route
     */
    public String createRoute(PlannedRoute route) {
        route = plannedRouteRepository.save(route);
        return route.getId();
    }

    /**
     * Retrieves a planned route by its ID.
     *
     * @param id the ID of the planned route to retrieve
     * @return the planned route with the specified ID, or null if not found
     */
    public PlannedRoute getPlannedRouteById(String id) {
        return plannedRouteRepository.findById(String.valueOf(id)).orElse(null);
    }

    /**
     * Updates an existing planned route.
     *
     * @param route the planned route to update
     */
    public void updateRoute(PlannedRoute route) {
        plannedRouteRepository.save(route);
    }

    /**
     * Deletes a planned route.
     *
     * @param route the planned route to delete
     */
    public void deleteRoute(PlannedRoute route) {
        plannedRouteRepository.delete(route);
    }
}