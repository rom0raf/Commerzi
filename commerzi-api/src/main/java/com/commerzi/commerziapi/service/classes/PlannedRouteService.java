package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.dao.PlannedRouteRepository;
import com.commerzi.commerziapi.maps.MapsUtils;
import com.commerzi.commerziapi.maps.algorithms.ATravelerAlgorithm;
import com.commerzi.commerziapi.maps.algorithms.AlgorithmType;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.interfaces.IPlannedRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String createRoute(PlannedRoute route) throws IllegalArgumentException {
        checkPlannedRoute(route);

        MapsUtils.buildFullRoute(
                route,
                ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE_OPTIMIZED_THREADED)
        );

        plannedRouteRepository.save(route);

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
     * Retrieves all planned routes for a user.
     * @param userId the ID of the user to retrieve planned routes for
     * @return a list of all planned routes for the specified user
     */
    public List<PlannedRoute> getAll(String userId) {
        return plannedRouteRepository.findByUserId(userId);
    }

    /**
     * Updates an existing planned route.
     *
     * @param route the planned route to update
     */
    public void updateRoute(PlannedRoute route) throws IllegalArgumentException {

        checkPlannedRoute(route);

        plannedRouteRepository.save(route);
    }

    /**
     * Deletes a planned route.
     *
     * @param id the ID of the planned route to delete
     */
    public void deleteRouteById(String id) {
        plannedRouteRepository.deleteById(id);
    }

    public static void checkPlannedRoute(PlannedRoute route) throws IllegalArgumentException {
        if (route.getCustomersAndProspects().size() > 8) {
            throw new IllegalArgumentException("Too many customers");
        }

        if (route.getCustomersAndProspects().size() < 2) {
            throw new IllegalArgumentException("Too few customers");
        }

        if (route.getCustomersAndProspects().stream().anyMatch(c -> c.getGpsCoordinates() == null)) {
            throw new IllegalArgumentException("Customer coordinates missing");
        }

        if (route.getStartingPoint() == null) {
            throw new IllegalArgumentException("Starting point missing");
        }
    }
}