package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.ActualRoute;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.interfaces.IActualRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public ResponseEntity<ActualRoute> getActualRoutes(@PathVariable String id) {
        ActualRoute actualRoute = actualRouteService.getActualRouteById(id);
        return ResponseEntity.ok(actualRoute);
    }

    /**
     * Creates an actual route from a planned route.
     *
     * @param plannedRoute the planned route to convert
     * @return the created actual route
     */
    @PostMapping()
    public ResponseEntity<ActualRoute> getActualRoutes(@RequestBody PlannedRoute plannedRoute) {
        ActualRoute actualRoute = actualRouteService.createActualRouteFromPlannedRoute(plannedRoute);
        return ResponseEntity.ok(actualRoute);
    }

    /**
     * Saves an actual route to the repository.
     *
     * @param actualRoute the actual route to save
     * @return the ID of the saved actual route
     */
    @PostMapping("/save")
    public ResponseEntity<String> createActualRoute(@RequestBody ActualRoute actualRoute) {
        String id = actualRouteService.saveActualRoute(actualRoute);
        return ResponseEntity.ok(id);
    }
}