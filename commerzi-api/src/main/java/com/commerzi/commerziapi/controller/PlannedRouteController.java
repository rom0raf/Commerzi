package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.security.Security;
import com.commerzi.commerziapi.service.interfaces.IAuthentificationService;
import com.commerzi.commerziapi.service.interfaces.IPlannedRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * REST controller for handling routes in the Commerzi application.
 */
@RestController
@RequestMapping("/api/route/planned")
public class PlannedRouteController {
    @Autowired
    private IPlannedRouteService plannedRouteService;

    @Autowired
    IAuthentificationService authentificationService;

    /**
     * Retrieves all planned routes for a user.
     * @return a list of all planned routes for the current user
     */
    @CommerziAuthenticated
    @GetMapping("/")
    public ResponseEntity getAllPlannedRoutes() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<PlannedRoute> plannedRoutes;

        try {
            plannedRoutes = plannedRouteService.getAll(String.valueOf(user.getUserId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(plannedRoutes);
    }

    /**
     * Retrieves a planned route by its ID.
     *
     * @param id the ID of the planned route to retrieve
     * @return the planned route with the specified ID
     */
    @CommerziAuthenticated
    @GetMapping("/{id}")
    public ResponseEntity<PlannedRoute> getPlannedRoutes(@PathVariable String id) {
        PlannedRoute plannedRoute = plannedRouteService.getPlannedRouteById(id);

        if (plannedRoute == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(plannedRoute);
    }

    /**
     * Creates a new planned route.
     *
     * @return the ID of the created planned route
     */
    @CommerziAuthenticated
    @PostMapping("/{useRealDistance}")
    public ResponseEntity<String> createPlannedRoute(@RequestBody List<String> customersId) {

        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        try {
            String id = plannedRouteService.createRoute(customersId, user);
            return ResponseEntity.ok(id);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error while fetching real distances");
        } catch (Exception e) {
            String message = "An unexpected error occurred while creating the route\n";
            message += e.getMessage() != null ? ": " + e.getMessage() : "";
            return ResponseEntity.internalServerError().body(message);
        }
    }

    /**
     * Updates an existing planned route.
     *
     * @param plannedRoute the planned route to update
     * @return a response indicating the update status
     */
    @CommerziAuthenticated
    @PutMapping("/")
    public ResponseEntity<String> updatePlannedRoute(@RequestBody PlannedRoute plannedRoute) {

        try {
            plannedRouteService.updateRoute(plannedRoute);
            return ResponseEntity.ok("Route updated successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * Deletes a planned route.
     *
     * @param id the ID of the planned route to delete
     * @return a response indicating the deletion status
     */
    @CommerziAuthenticated
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlannedRoute(@PathVariable String id) {
        plannedRouteService.deleteRouteById(id);
        return ResponseEntity.ok("Route deleted successfully");
    }

}