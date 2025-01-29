package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapsUtils {

    private static final double EARTH_RADIUS = 6371.0;

    /**
     * Calculates the haversine of an angle.
     * The haversine formula is used to find the distance between two points on the surface of a sphere.
     *
     * @param theta the angle in radians
     * @return the haversine of the angle
     */
    private static double haversine(double theta) {
        return Math.pow(Math.sin(theta / 2), 2);
    }

    /**
     * This method calculates the flying distance between two points.
     * @param p1 the first point
     * @param p2 the second point
     * @return flying distance between two points
     * @throws IllegalArgumentException if p1 or p2 is null
     */
    public static double distanceBetweenTwoPoints(JOpenCageLatLng p1, JOpenCageLatLng p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Point 1 or Point 2 can't be null");
        }

        if (p1.equals(p2)) {
            return 0.0;
        }

        double lat1 = p1.getLat();
        double lon1 = p1.getLng();

        double lat2 = p2.getLat();
        double lon2 = p2.getLng();

        double latitudinalDistance = Math.toRadians(lat2 - lat1);
        double longitudinalDistance = Math.toRadians(lon2 - lon1);

        // Haversine formula https://en.wikipedia.org/wiki/Haversine_formula
        double a = haversine(latitudinalDistance) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * haversine(longitudinalDistance);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Calculates the total distance between a series of points.
     * The total distance is computed by summing the distances between each consecutive pair of points in the array.
     *
     * @param points a list of JOpenCageLatLng objects representing the points in the route
     * @return the total distance as a Double, which is the sum of distances between consecutive points
     * @throws IllegalArgumentException if the points array is null or contains fewer than two points
     */
    public static Double calcTotalDistanceBetweenPoints(List<JOpenCageLatLng> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("There must be at least two points to calculate distance.");
        }

        Double totalDistance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            totalDistance += distanceBetweenTwoPoints(points.get(i), points.get(i + 1));
        }

        return totalDistance;
    }

    /**
     * Builds a full route by ordering customers based on the shortest path starting and ending at the commercial home.
     * The method first sorts the customers by distance using the nearest-neighbor heuristic (or any custom sorting function)
     * to create the optimal route, and then calculates the total travel distance of the route.
     *
     * @param commercialHome the starting and ending point for the route, representing the commercial home GPS coordinates
     * @param customers a list of customers whose locations need to be included in the route
     * @param sortFunction a function that sorts a list of points
     * @return a PlannedRoute object containing the ordered customers and the total travel distance
     * @throws IllegalArgumentException if the customers list is null or empty or if the commercialHome is null
     */
    public static PlannedRoute buildFullRoute(
        JOpenCageLatLng commercialHome,
        List<Customer> customers,
        BiFunction<JOpenCageLatLng, List<JOpenCageLatLng>, List<JOpenCageLatLng>> sortFunction
    ) {
        if (customers == null || customers.isEmpty()) {
            throw new IllegalArgumentException("Customer list cannot be null or empty.");
        }

        if (commercialHome == null) {
            throw new IllegalArgumentException("Commercial home can't be null.");
        }

        PlannedRoute route = new PlannedRoute();

        route.setStartingPoint(commercialHome);
        route.setEndingPoint(commercialHome);

        List<JOpenCageLatLng> points = customers.stream()
            .map(Customer::getGpsCoordinates)
            .toList();

        points = sortFunction.apply(commercialHome, points);

        List<Customer> orderedCustomers = new ArrayList<>();
        for (JOpenCageLatLng point : points) {
            customers.stream()
                .filter(c -> c.getGpsCoordinates().equals(point))
                .findFirst()
                .ifPresent(orderedCustomers::add);
        }

        route.setCustomersAndProspects(orderedCustomers);

        route.setTotalDistance(calcTotalDistanceBetweenPoints(points));

        return route;
    }

}
