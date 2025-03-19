package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.maps.algorithms.ATravelerAlgorithm;
import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.maps.GPSRoute;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for map-related operations such as calculating distances between geographical coordinates.
 * This class provides methods to calculate both flying (great-circle) distances and real-world road distances using OSRM.
 * The distances are cached to improve performance.
 */
public class MapsUtils {

    /**
     * The radius of the Earth in kilometers.
     */
    private static final double EARTH_RADIUS = 6371.0;

    /**
     * The URL for OSRM's routing service, which provides real-world road distances.
     */
    private static final String OSRM_URL = "https://router.project-osrm.org/route/v1/driving/%s,%s;%s,%s?overview=full&steps=true&geometries=geojson";

    /**
     * The maximum number of entries allowed in the cache.
     */
    private static final int MAX_CACHE_SIZE = 1000;

    /**
     * A record that holds a pair of coordinates.
     * Used for caching flying distances between two coordinates.
     */
    private record CoordinatePair(Coordinates c1, Coordinates c2) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CoordinatePair that)) return false;
            return c1.equals(that.c1) && c2.equals(that.c2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(c1, c2);
        }
    }

    /**
     * A record that holds a normalized pair of coordinates.
     * Ensures that the first coordinate always has a lower latitude or lexicographically smaller longitude.
     */
    private record NormalizedCoordinatePair(Coordinates c1, Coordinates c2) {
        private NormalizedCoordinatePair {
            if (c1.getLatitude() > c2.getLatitude() ||
                (c1.getLatitude() == c2.getLatitude() && c1.getLongitude() > c2.getLongitude())) {
                Coordinates temp = c1;
                c1 = c2;
                c2 = temp;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CoordinatePair that)) return false;
            return c1.equals(that.c1) && c2.equals(that.c2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(c1, c2);
        }
    }

    /**
     * A cache for storing flying distances between two coordinates.
     * The cache is limited to {@link #MAX_CACHE_SIZE} entries and expires after 1 hour of inactivity.
     */
    private static final LoadingCache<NormalizedCoordinatePair, Double> FLYING_DISTANCE_CACHE = CacheBuilder.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<NormalizedCoordinatePair, Double>() {
                @Override
                public Double load(NormalizedCoordinatePair key) throws Exception {
                    double lat1 = key.c1.getLatitude();
                    double lon1 = key.c1.getLongitude();

                    double lat2 = key.c2.getLatitude();
                    double lon2 = key.c2.getLongitude();

                    double latitudinalDistance = Math.toRadians(lat2 - lat1);
                    double longitudinalDistance = Math.toRadians(lon2 - lon1);

                    // Haversine formula: Calculates the great-circle distance between two points
                    double a = haversine(latitudinalDistance) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * haversine(longitudinalDistance);

                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    return EARTH_RADIUS * c;
                }
            });

    /**
     * A cache for storing real-world road distances between two coordinates.
     * The cache is limited to {@link #MAX_CACHE_SIZE} entries and expires after 1 hour of inactivity.
     */
    private static final LoadingCache<CoordinatePair, Double> REAL_DISTANCE_CACHE = CacheBuilder.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<CoordinatePair, Double>() {
                @Override
                public Double load(CoordinatePair key) throws Exception {
                    // Calls OSRM API to get the distance between two coordinates
                    return getGpsRoute(key.c1, key.c2).getDistance();
                }
            });

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
    public static Double flyingDistanceBetweenTwoPoints(Coordinates p1, Coordinates p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Point 1 or Point 2 can't be null");
        }

        if (p1.equals(p2)) {
            return 0.0;
        }

        try {
            return FLYING_DISTANCE_CACHE.get(new NormalizedCoordinatePair(p1, p2));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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
    public static Double fullFlyingDistanceOverPoints(List<Coordinates> points) {
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
     * Gets the GPS route between two points using the Open Source Routing Machine (OSRM) API.
     *
     * @param start the starting point
     * @param end the ending point
     * @return GPSRoute object containing the distance, duration and steps of the route
     * @throws IOException
     */
    public static GPSRoute getGpsRoute(Coordinates start, Coordinates end) throws IOException {
        String urlString = getOSRMUrl(start, end);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        System.out.println("Sending request to OSRM API: " + urlString);

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
     *
     * @param p1 the starting point
     * @param p2 the ending point
     * @return the real distance between the two points
     */
    public static Double realDistanceBetweenPoints(Coordinates p1, Coordinates p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("Point 1 or Point 2 can't be null");
        }

        if (p1.equals(p2)) {
            return 0.0;
        }

        try {
            return REAL_DISTANCE_CACHE.get(new CoordinatePair(p1, p2));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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
    public static Double fullRealDistanceOverPoints(List<Coordinates> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("There must be at least two points to calculate distance.");
        }

        Double totalDistance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            totalDistance += realDistanceBetweenPoints(points.get(i), points.get(i + 1));
        }

        return totalDistance;
    }

    /**
     * Generates the URL for the Open Source Routing Machine (OSRM) API.
     *
     * @param start the starting point
     * @param end the ending point
     * @return the URL as a String
     */
    private static String getOSRMUrl(Coordinates start, Coordinates end) {
        String startLat = String.valueOf(start.getLatitude()).replace(",", ".");
        String startLng = String.valueOf(start.getLongitude()).replace(",", ".");
        String endLat = String.valueOf(end.getLatitude()).replace(",", ".");
        String endLng = String.valueOf(end.getLongitude()).replace(",", ".");

        return String.format(OSRM_URL, startLng, startLat, endLng, endLat);
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

        final List<Coordinates> points = initialRoute.getCustomersAndProspects().stream()
            .map(Customer::getGpsCoordinates)
            .toList();

        List<Coordinates> newPoints = algorithm.apply(initialRoute.getStartingPoint(), points);

        List<Customer> orderedCustomers = new ArrayList<>();
        for (Coordinates point : newPoints) {
            initialRoute.getCustomersAndProspects().stream()
                    .filter(c -> c.getGpsCoordinates().equals(point))
                    .findFirst().ifPresent(orderedCustomers::add);
        }

        initialRoute.setCustomersAndProspects(orderedCustomers);

        newPoints.add(initialRoute.getEndingPoint());
        newPoints.add(0, initialRoute.getStartingPoint());

        double distance = algorithm.getFullDistanceOverPointsFunc().apply(newPoints);
        initialRoute.setTotalDistance(distance);
    }
}
