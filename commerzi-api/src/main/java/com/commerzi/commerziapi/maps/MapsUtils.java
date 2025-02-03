package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.maps.algorithms.ATravelerAlgorithm;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

public class MapsUtils {

    private static final double EARTH_RADIUS = 6371.0;

    /**
     * Cached calculated flying distances
     * ConcurrentMap for thread safe access
     */
    private static final ConcurrentMap<String, Double> distanceCache = new ConcurrentHashMap<>();

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
     * Generates a unique cache key for a pair of geographic points (latitudes and longitudes).
     * The key ensures that the distance calculation between two points is cached regardless of the order
     * in which the points are passed (i.e., the cache key will be the same for both directions, p1 -> p2 and p2 -> p1).
     *
     * @param p1 the first geographic point (latitude and longitude)
     * @param p2 the second geographic point (latitude and longitude)
     * @return a unique string that represents the cache key for the given pair of points
     */
    private static String generateCacheKey(JOpenCageLatLng p1, JOpenCageLatLng p2) {
        // To ensure uniqueness, we use both directions (p1 -> p2 and p2 -> p1)
        String key1 = p1.getLat() + "," + p1.getLng() + "-" + p2.getLat() + "," + p2.getLng();
        String key2 = p2.getLat() + "," + p2.getLng() + "-" + p1.getLat() + "," + p1.getLng();
        return key1.hashCode() < key2.hashCode() ? key1 : key2;
    }

    /**
     * This method calculates the flying distance between two points.
     * This method uses memory caching.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return flying distance between two points
     * @throws IllegalArgumentException if p1 or p2 is null
     */
    public static double flyingDistanceBetweenTwoPoints(JOpenCageLatLng p1, JOpenCageLatLng p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Point 1 or Point 2 can't be null");
        }

        if (p1.equals(p2)) {
            return 0.0;
        }

        return distanceCache.computeIfAbsent(generateCacheKey(p1, p2), x -> {
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
        });
    }

    /**
     * Calculates the total distance between a series of points.
     * The total distance is computed by summing the flying distances between
     * each consecutive pair of points in the array.
     *
     * @param points a list of JOpenCageLatLng objects representing the points in the route
     * @return the total distance as a Double, which is the sum of distances between consecutive points
     * @throws IllegalArgumentException if the points array is null or contains fewer than two points
     */
    public static Double fullFlyingDistanceOverPoints(List<JOpenCageLatLng> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("There must be at least two points to calculate distance.");
        }

        Double totalDistance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            totalDistance += flyingDistanceBetweenTwoPoints(points.get(i), points.get(i + 1));
        }

        return totalDistance;
    }

    /**
     * Builds a full route by ordering customers based on the shortest flying distance, starting and ending at the commercial home.
     * The method uses any custom sorting class (see {@link ATravelerAlgorithm} class) to arrange the customers in the optimal order,
     * and then calculates the total flying travel distance of the route.
     *
     * @param initialRoute the original PlannedRoute that contains all the data about the Route
     * @param algorithm allows to sort the points via optimised algorithms of the developer choice
     * @return a PlannedRoute object containing the ordered customers and the total flying travel distance
     * @throws IllegalArgumentException if the customers list is null or empty, or if the commercialHome is null
     */
    public static void buildFullRoute(PlannedRoute initialRoute, ATravelerAlgorithm algorithm) {
        if (initialRoute.getCustomersAndProspects() == null || initialRoute.getCustomersAndProspects().isEmpty()) {
            throw new IllegalArgumentException("Customer list cannot be null or empty.");
        }

        if (initialRoute.getStartingPoint() == null) {
            throw new IllegalArgumentException("Commercial home can't be null.");
        }

        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm can't be null.");
        }

        List<JOpenCageLatLng> points = initialRoute.getCustomersAndProspects().stream()
            .map(Customer::getGpsCoordinates)
            .toList();

        points = algorithm.apply(initialRoute.getStartingPoint(), points);

        List<Customer> orderedCustomers = new ArrayList<>();
        for (JOpenCageLatLng point : points) {
            initialRoute.getCustomersAndProspects().stream()
                .filter(c -> c.getGpsCoordinates().equals(point))
                .findFirst()
                .ifPresent(orderedCustomers::add);
        }

        initialRoute.setCustomersAndProspects(orderedCustomers);

        points.add(initialRoute.getEndingPoint());
        points.add(0, initialRoute.getStartingPoint());
        initialRoute.setTotalDistance(algorithm.getFullDistanceOverPointsFunc().apply(points));
    }

}
