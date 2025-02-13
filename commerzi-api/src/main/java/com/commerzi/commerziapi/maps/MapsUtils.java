package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.maps.algorithms.ATravelerAlgorithm;
import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.maps.coordinates.CoordinatesCache;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.maps.GPSRoute;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MapsUtils {

    private static final double EARTH_RADIUS = 6371.0;

    private static final String OSRM_URL = "https://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?overview=full&steps=true";

    private static final int CACHE_MAX_SIZE = 10000;

    /**
     * Cached calculated flying distances
     */
    private static final CoordinatesCache<Double> FLYING_DISTANCE_CACHE = new CoordinatesCache(CACHE_MAX_SIZE);

    /**
     * Cached calculated real distances
     */
    private static final CoordinatesCache<Double> REAL_DISTANCE_CACHE = new CoordinatesCache(CACHE_MAX_SIZE);

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
     * This method uses memory caching.
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return flying distance between two points
     * @throws IllegalArgumentException if p1 or p2 is null
     */
    public static double flyingDistanceBetweenTwoPoints(Coordinates p1, Coordinates p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Point 1 or Point 2 can't be null");
        }

        if (p1.equals(p2)) {
            return 0.0;
        }

        return FLYING_DISTANCE_CACHE.computeIfAbsent(CoordinatesCache.generateBothWaysCacheKey(p1, p2), x -> {
            double lat1 = p1.latitude();
            double lon1 = p1.longitude();

            double lat2 = p2.latitude();
            double lon2 = p2.longitude();

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
    public static double fullFlyingDistanceOverPoints(List<Coordinates> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("There must be at least two points to calculate distance.");
        }

        double totalDistance = 0.0;

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
    public static void buildFullRoute(PlannedRoute initialRoute, ATravelerAlgorithm algorithm) throws IOException {
        if (initialRoute.getCustomersAndProspects() == null || initialRoute.getCustomersAndProspects().isEmpty()) {
            throw new IllegalArgumentException("Customer list cannot be null or empty.");
        }

        if (initialRoute.getStartingPoint() == null) {
            throw new IllegalArgumentException("Commercial home can't be null.");
        }

        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm can't be null.");
        }

        List<Coordinates> points = initialRoute.getCustomersAndProspects().stream()
            .map(Customer::getGpsCoordinates)
            .toList();

        points = algorithm.apply(initialRoute.getStartingPoint(), points);

        List<Customer> orderedCustomers = new ArrayList<>();
        for (Coordinates point : points) {
            initialRoute.getCustomersAndProspects().stream()
                .filter(c -> c.getGpsCoordinates().equals(point))
                .findFirst()
                .ifPresent(orderedCustomers::add);
        }

        initialRoute.setCustomersAndProspects(orderedCustomers);

        points.add(initialRoute.getEndingPoint());
        points.add(0, initialRoute.getStartingPoint());

        double distance;

        distance = algorithm.getFullDistanceOverPointsFunc().apply(points);

        initialRoute.setTotalDistance(distance);
    }

    /**
     * Gets the GPS route between two points using the Open Source Routing Machine (OSRM) API.
     * @param start the starting point
     * @param end the ending point
     * @return GPSRoute object containing the distance, duration and steps of the route
     * @throws IOException
     */
    private static GPSRoute getGpsRoute(Coordinates start, Coordinates end) throws IOException {
        String urlString = getOSRMUrl(start, end);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JsonObject jsonResponse = new JsonParser().parse(response.toString()).getAsJsonObject();
        JsonArray routes = jsonResponse.getAsJsonArray("routes");
        JsonObject route = routes.get(0).getAsJsonObject();

        // load the route into a GPSRoute object
        GsonBuilder gsonBuilder = new GsonBuilder();
        GPSRoute gpsRoute = gsonBuilder.create().fromJson(route, GPSRoute.class);

        return gpsRoute;
    }

    /**
     * Calculates the real distance between two points using the Open Source Routing Machine (OSRM) API.
     * @param p1 the starting point
     * @param p2 the ending point
     * @return the real distance between the two points
     */
    public static double realDistanceBetweenPoints(Coordinates p1, Coordinates p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Point 1 or Point 2 can't be null");
        }

        if (p1.equals(p2)) {
            return 0.0;
        }

        return REAL_DISTANCE_CACHE.computeIfAbsent(CoordinatesCache.generateCacheKey(p1, p2), x -> {
            double distance;

            try {
                distance = getGpsRoute(p1, p2).getDistance();
            } catch(IOException e) {
                distance = -1.0;
            }

            return distance;
        });
    }

    /**
     * Calculates the total distance between a series of points.
     * The total distance is computed by summing the real distances between
     * each consecutive pair of points in the array.
     *
     * @param points a list of Coordinates objects representing the points in the route
     * @return the total distance as a Double, which is the sum of distances between consecutive points
     * @throws IllegalArgumentException if the points array is null or contains fewer than two points
     */
    public static double fullRealDistanceOverPoints(List<Coordinates> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("There must be at least two points to calculate distance.");
        }

        double totalDistance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            totalDistance += realDistanceBetweenPoints(points.get(i), points.get(i + 1));
        }

        return totalDistance;
    }

    /**
     * Generates the URL for the Open Source Routing Machine (OSRM) API.
     * @param start the starting point
     * @param end the ending point
     * @return the URL as a String
     */
    private static String getOSRMUrl(Coordinates start, Coordinates end) {
        String startLat = String.valueOf(start.latitude()).replace(",", ".");
        String startLng = String.valueOf(start.longitude()).replace(",", ".");
        String endLat = String.valueOf(end.latitude()).replace(",", ".");
        String endLng = String.valueOf(end.longitude()).replace(",", ".");

        return String.format(OSRM_URL, startLat, startLng, endLat, endLng);
    }
}
