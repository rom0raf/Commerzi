package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.service.interfaces.IActualRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling actual routes in the Commerzi application.
 */
@RestController
@RequestMapping("/api/route/actual")
public class ActualRouteController {

    @Autowired
    private IActualRouteService actualRouteService;

    /**
     * Retrieves an actual route by its ID.
     *
     * @param id the ID of the actual route to retrieve
     * @return the actual route with the specified ID
     */
    @CommerziAuthenticated
    @GetMapping("/{id}")
    public ResponseEntity getActualRoutes(@PathVariable String id) {
        ActualRoute actualRoute = actualRouteService.getActualRouteById(id);

        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualRoute);
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
        ActualRoute actualRoute = actualRouteService.createActualRouteFromPlannedRoute(plannedRoute);
        try {
            actualRouteService.saveActualRoute(actualRoute);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(actualRoute);
    }

    /**
     * Saves an actual route to the repository.
     *
     * @param actualRoute the actual route to save
     * @return the ID of the saved actual route
     */
    @CommerziAuthenticated
    @PostMapping("/save")
    public ResponseEntity<String> createActualRoute(@RequestBody ActualRoute actualRoute) {
        String id;
        try {
            id = actualRouteService.saveActualRoute(actualRoute);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(id);
    }

    @CommerziAuthenticated
    @PostMapping("/{id}")
    public ResponseEntity updateRouteCoordinates(@RequestBody List<Coordinates> newCoordinates, @PathVariable String id) {
        ActualRoute actualRoute = actualRouteService.getActualRouteById(id);
        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        actualRoute.getCoordinates().addAll(newCoordinates);

        actualRouteService.saveActualRoute(actualRoute);
        return ResponseEntity.ok(actualRoute);
    }

    @CommerziAuthenticated
    @PutMapping("/{visitIndex}?status={status}")
    public ResponseEntity skipVisit(@PathVariable int visitIndex, @PathVariable String status, @RequestBody ActualRoute actualRoute) {
        if (actualRoute == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            actualRoute = actualRouteService.updateVisit(visitIndex, status, actualRoute);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(actualRoute);
    }
}