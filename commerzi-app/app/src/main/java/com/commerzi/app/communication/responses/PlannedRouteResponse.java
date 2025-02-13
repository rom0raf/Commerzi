package com.commerzi.app.communication.responses;

import com.commerzi.app.route.plannedRoute.PlannedRoute;

import java.util.ArrayList;

public class PlannedRouteResponse {
    public final ArrayList<PlannedRoute> routes;
    public final String message;

    public PlannedRouteResponse(ArrayList<PlannedRoute> routes, String message) {
        this.routes = routes;
        this.message = message;
    }
}
