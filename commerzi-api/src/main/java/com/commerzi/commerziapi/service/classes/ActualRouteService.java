package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.dao.ActualRouteRepository;
import com.commerzi.commerziapi.model.*;
import com.commerzi.commerziapi.service.interfaces.IActualRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
     * @param actualRoute the actual route to save
     * @return the ID of the saved actual route
     */
    public String saveActualRoute(ActualRoute actualRoute) throws IllegalArgumentException {
        checkActualRoute(actualRoute);
        actualRouteRepository.save(actualRoute);
        return actualRoute.getId();
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
        PlannedRouteService.checkPlannedRoute(plannedRoute);

        ActualRoute actualRoute = new ActualRoute();

        actualRoute.setDate(LocalDate.now().toString());
        actualRoute.setUserId(plannedRoute.getUserId());
        actualRoute.setRouteId(plannedRoute.getId());

        actualRoute.setVisits(
                getVisitFromPlannedRoute(plannedRoute)
        );

        actualRoute.setCoordinates(
                new ArrayList<>()
        );

        actualRoute.setStatus(ERouteStatus.IN_PROGRESS);

        return actualRoute;
    }

    private static List<Visit> getVisitFromPlannedRoute(PlannedRoute plannedRoute) {
        List<Visit> visits = new ArrayList<>();
        for (Customer customer : plannedRoute.getCustomersAndProspects()) {
            visits.add(new Visit(customer));
        }
        return visits;
    }

    private static void checkActualRoute(ActualRoute route) throws IllegalArgumentException {

        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }
        if (route.getDate() == null) {
            throw new IllegalArgumentException("Route date cannot be null");
        }
        if (route.getUserId() == null) {
            throw new IllegalArgumentException("Route user ID cannot be null");
        }
        if (route.getRouteId() == null) {
            throw new IllegalArgumentException("Route ID cannot be null");
        }
        if (route.getVisits() == null) {
            throw new IllegalArgumentException("Route visits cannot be null");
        }
        if (route.getVisits().isEmpty()) {
            throw new IllegalArgumentException("Route visits cannot be empty");
        }
        if (route.getStatus() == null) {
            throw new IllegalArgumentException("Route status cannot be null");
        }
        if (route.getCoordinates() == null) {
            throw new IllegalArgumentException("Route current location cannot be null");
        }
    }


    public ActualRoute skipVisit(int visitPos, ActualRoute route) throws IllegalArgumentException {
        checkActualRoute(route);

        if (visitPos < 0 || visitPos >= route.getVisits().size()) {
            throw new IllegalArgumentException("Invalid visit position");
        }

        route.getVisits().get(visitPos).setStatus(EVisitStatus.SKIPPED);

        if (route.getVisits().stream().allMatch(visit -> visit.getStatus() == EVisitStatus.SKIPPED || visit.getStatus() == EVisitStatus.VISITED)) {
            route.setStatus(ERouteStatus.COMPLETED);
        }

        actualRouteRepository.save(route);

        return route;
    }

}
