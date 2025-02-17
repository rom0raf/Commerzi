package com.commerzi.app.communication.responses;

import com.commerzi.app.route.actualRoute.RouteAndGpsDto;

public class ActualRouteResponse {

    public final RouteAndGpsDto actualRoute;
    public final String message;

    public ActualRouteResponse(RouteAndGpsDto route, String message) {
        this.actualRoute = route;
        this.message = message;
    }
}
