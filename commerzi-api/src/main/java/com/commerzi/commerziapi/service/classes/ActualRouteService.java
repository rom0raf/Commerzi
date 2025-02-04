package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.dao.ActualRouteRepository;
import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.model.Visit;
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
        checkPlannedRoute(plannedRoute);

        ActualRoute actualRoute = new ActualRoute();

        actualRoute.setDate(LocalDate.now().toString());
        actualRoute.setUserId(plannedRoute.getUserId());
        actualRoute.setRouteId(plannedRoute.getId());

        actualRoute.setVisits(
                getVisitFromPlannedRoute(plannedRoute)
        );

        return actualRoute;
    }

    public static List<Visit> getVisitFromPlannedRoute(PlannedRoute plannedRoute) {
        List<Visit> visits = new ArrayList<>();
        for (Customer customer : plannedRoute.getCustomersAndProspects()) {
            visits.add(new Visit(customer));
        }
        return visits;
    }

    private static void checkPlannedRoute(PlannedRoute plannedRoute) throws IllegalArgumentException {
        if (plannedRoute == null) {
            throw new IllegalArgumentException("Planned route cannot be null");
        }
        if (plannedRoute.getCustomersAndProspects() == null || plannedRoute.getCustomersAndProspects().isEmpty()) {
            throw new IllegalArgumentException("Customers and prospects cannot be null or empty");
        }

        for (Customer customer : plannedRoute.getCustomersAndProspects()) {
            if (customer == null) {
                throw new IllegalArgumentException("Customer cannot be null");
            }
        }

        if (plannedRoute.getUserId() == null || plannedRoute.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }
}
