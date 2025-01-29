package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.interfaces.IPlannedRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling routes in the Commerzi application.
 */
@RestController
@RequestMapping("/api/route/planned")
public class PlannedRouteController {
    @Autowired
    private IPlannedRouteService plannedRouteService;

    /**
     * Retrieves a planned route by its ID.
     *
     * @param id the ID of the planned route to retrieve
     * @return the planned route with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlannedRoute> getPlannedRoutes(@PathVariable String id) {
        PlannedRoute plannedRoute = plannedRouteService.getPlannedRouteById(id);
        return ResponseEntity.ok(plannedRoute);
    }

    /**
     * Creates a new planned route.
     *
     * @param plannedRoute the planned route to create
     * @return the ID of the created planned route
     */
    @PostMapping()
    public ResponseEntity<String> createPlannedRoute(@RequestBody PlannedRoute plannedRoute) {
        String id = plannedRouteService.createRoute(plannedRoute);
        return ResponseEntity.ok(id);
    }

    /**
     * Updates an existing planned route.
     *
     * @param plannedRoute the planned route to update
     * @return a response indicating the update status
     */
    @PutMapping()
    public ResponseEntity<String> updatePlannedRoute(@RequestBody PlannedRoute plannedRoute) {
        plannedRouteService.updateRoute(plannedRoute);
        return ResponseEntity.ok("Route updated successfully");
    }

    /**
     * Deletes a planned route.
     *
     * @param plannedRoute the planned route to delete
     * @return a response indicating the deletion status
     */
    @DeleteMapping()
    public ResponseEntity<String> deletePlannedRoute(@RequestBody PlannedRoute plannedRoute) {
        plannedRouteService.deleteRoute(plannedRoute);
        return ResponseEntity.ok("Route deleted successfully");
    }

}