package com.commerzi.app.communication.responses;

import com.commerzi.app.route.actualRoute.ActualRoute;
import com.commerzi.app.route.actualRoute.RouteAndGpsDto;

import java.util.ArrayList;
import java.util.List;

public class ActualRouteResponse {

    public final ArrayList<ActualRoute> routes;
    public final String message;

    public ActualRouteResponse(ArrayList<ActualRoute> route, String message) {
        this.routes = route;
        this.message = message;
    }
}
