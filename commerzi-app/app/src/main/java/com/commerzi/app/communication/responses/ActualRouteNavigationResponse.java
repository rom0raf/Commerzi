package com.commerzi.app.communication.responses;

import com.commerzi.app.route.actualRoute.RouteAndGpsDto;

public class ActualRouteNavigationResponse {

    public final RouteAndGpsDto actualRoute;
    public final String message;

    public ActualRouteNavigationResponse(RouteAndGpsDto route, String message) {
        this.actualRoute = route;
        this.message = message;
    }
}
