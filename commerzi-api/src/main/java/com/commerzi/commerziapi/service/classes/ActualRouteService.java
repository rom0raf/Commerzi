package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.dao.ActualRouteRepository;
import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.interfaces.IActualRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing actual routes in the Commerzi application.
 */
@Service
public class ActualRouteService implements IActualRouteService {

    @Autowired
    private ActualRouteRepository actualRouteRepository;

    /**
     * Saves an actual route to the repository.
     *
     * @param plannedRouteId the actual route to save
     * @return the ID of the saved actual route
     */
    public String saveActualRoute(ActualRoute plannedRouteId) {
        actualRouteRepository.save(plannedRouteId);
        return plannedRouteId.getId();
    }

    /**
     * Retrieves an actual route by its ID.
     *
     * @param plannedRouteId the ID of the actual route to retrieve
     * @return the actual route with the specified ID, or null if not found
     */
    public ActualRoute getActualRouteById(String plannedRouteId) {
        return actualRouteRepository.findById(plannedRouteId).orElse(null);
    }

    /**
     * Creates an actual route from a planned route.
     *
     * @param plannedRoute the planned route to convert
     * @return the created actual route
     */
    public ActualRoute createActualRouteFromPlannedRoute(PlannedRoute plannedRoute) {
        return null;
    }
}
