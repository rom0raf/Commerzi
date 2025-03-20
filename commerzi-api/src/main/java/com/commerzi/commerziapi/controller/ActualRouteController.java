package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.dto.NearbyCustomersDTO;
import com.commerzi.commerziapi.dto.RouteAndGpsDto;
import com.commerzi.commerziapi.dto.UpdateLocationDTO;
import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.model.maps.GPSRoute;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.security.Security;
import com.commerzi.commerziapi.service.classes.ActualRouteService;
import com.commerzi.commerziapi.service.interfaces.IActualRouteService;
import com.commerzi.commerziapi.service.interfaces.IAuthentificationService;
import com.commerzi.commerziapi.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for handling actual routes in the Commerzi application.
 */
@RestController
@RequestMapping("/api/route/actual")
public class ActualRouteController {

    @Autowired
    private IActualRouteService actualRouteService;

    @Autowired
    private IAuthentificationService authentificationService;

    @Autowired
    private ICustomerService customerService;

    /**
     * Retrieves an actual route by its ID.
     *
     * @param id the ID of the actual route to retrieve
     * @return the actual route with the specified ID
     */
    @CommerziAuthenticated
    @GetMapping("/{id}")
    public ResponseEntity getActualRoute(@PathVariable String id) {
        ActualRoute actualRoute = actualRouteService.getActualRouteById(id);

        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualRoute);
    }

    @CommerziAuthenticated
    @GetMapping("/")
    public ResponseEntity getActualRoutesForUser() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ActualRoute> actualRoutes = actualRouteService.getActualRoutesForUser("" + user.getUserId());

        return ResponseEntity.ok(actualRoutes);
    }

    /**
     * Creates an actual route from a planned route.
     *
     * @param plannedRoute the planned route to convert
     * @return the created actual route
     */
    @CommerziAuthenticated
    @PostMapping("/")
    public ResponseEntity getActualRoutes(@RequestBody PlannedRoute plannedRoute) {

        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        ActualRoute actualRoute = actualRouteService.createActualRouteFromPlannedRoute(plannedRoute);
        actualRoute.setUserId("" + user.getUserId());

//        List<GPSRoute> gpsRoutes = actualRouteService.getGPSRoutes(actualRoute);
        List<GPSRoute> gpsRoutes = new ArrayList<>();
        try {
            String id = actualRouteService.saveActualRoute(actualRoute);
            actualRoute.setId(id);
            actualRoute.setPlannedRouteId(plannedRoute.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred while creating the actual route.");
        }

        RouteAndGpsDto routeAndGpsDto = new RouteAndGpsDto(
                actualRoute, gpsRoutes
        );

        return ResponseEntity.ok(routeAndGpsDto);
    }

    /**
     * Saves an actual route to the repository.
     *
     * @param actualRoute the actual route to save
     * @return the ID of the saved actual route
     */
    @CommerziAuthenticated
    @PostMapping("/save")
    public ResponseEntity<String> saveActualRoute(@RequestBody ActualRoute actualRoute) {
        String id;
        try {
            id = actualRouteService.saveActualRoute(actualRoute);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred while saving the actual route.");
        }

        return ResponseEntity.ok(id);
    }

    @CommerziAuthenticated
    @PostMapping("/{id}")
    public ResponseEntity updateRouteCoordinates(@RequestBody UpdateLocationDTO newCoordinates, @PathVariable String id) {
        ActualRoute actualRoute = actualRouteService.getActualRouteById(id);
        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        actualRoute.getCoordinates().addAll(newCoordinates.getCoordinates());
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());
        List<Customer> customers = new ArrayList<>();
        try {
            actualRouteService.saveActualRoute(actualRoute);

            Coordinates coordinates = newCoordinates.getCoordinates().get(newCoordinates.getCoordinates().size() - 1);
            customers = customerService.getNearbyClients(coordinates, user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred while updating the route coordinates.");
        }
        NearbyCustomersDTO nearbyCustomersDTO = new NearbyCustomersDTO();
        nearbyCustomersDTO.setCustomers(customers);
        return ResponseEntity.ok(nearbyCustomersDTO);
    }

    @CommerziAuthenticated
    @PutMapping("/skip")
    public ResponseEntity skipVisit(@RequestBody ActualRoute actualRoute) {
        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            actualRoute = actualRouteService.skipVisit(actualRoute);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(actualRoute);
    }

    @CommerziAuthenticated
    @PutMapping("/confirm")
    public ResponseEntity confirmVisit(@RequestBody ActualRoute actualRoute) {
        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            actualRoute = actualRouteService.confirmVisit(actualRoute);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(actualRoute);
    }

    @CommerziAuthenticated
    @DeleteMapping("/{id}")
    public ResponseEntity deleteActualRoute(@PathVariable String id) {
        ActualRoute actualRoute = actualRouteService.getActualRouteById(id);

        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        actualRouteService.delete(actualRoute);

        return ResponseEntity.ok(actualRoute);
    }
}