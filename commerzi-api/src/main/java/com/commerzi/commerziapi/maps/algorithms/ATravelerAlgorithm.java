package com.commerzi.commerziapi.maps.algorithms;

import com.commerzi.commerziapi.maps.MapsUtils;
import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Abstract base class for implementing different traveling salesman algorithms.
 * This class provides common functionality such as validating points and holding references
 * to functions used for calculating distances between points and the total distance of a route.
 */
public abstract class ATravelerAlgorithm {

    /**
     * A function to calculate the total distance of a route represented by a list of points.
     */
    protected final Function<List<JOpenCageLatLng>, Double> fullDistanceOverPointsFunc;

    /**
     * A function to calculate the distance between two points.
     */
    protected final BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceBetweenTwoPoints;

    /**
     * Constructor for initializing the algorithm with functions to calculate total route distance
     * and the distance between two points.
     *
     * @param fullDistanceFunc a function to calculate the total distance for a given route
     * @param distanceFunc a function to calculate the distance between two points
     */
    public ATravelerAlgorithm(Function<List<JOpenCageLatLng>, Double> fullDistanceFunc, BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc) {
        this.fullDistanceOverPointsFunc = fullDistanceFunc;
        this.distanceBetweenTwoPoints = distanceFunc;
    }

    /**
     * Static method to return an algorithm instance based on the specified type,
     * initialized with the default distance functions.
     *
     * @param type the type of algorithm to return
     * @return an instance of the specified algorithm
     */
    public static ATravelerAlgorithm getAlgorithmWithFlyingDistances(AlgorithmType type) {
        Function<List<JOpenCageLatLng>, Double> fullDistanceFunc = MapsUtils::fullFlyingDistanceOverPoints;
        BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc = MapsUtils::flyingDistanceBetweenTwoPoints;

        switch (type) {
            case BRUTE_FORCE:
                return new BruteForce(fullDistanceFunc, distanceFunc);
            case BRUTE_FORCE_OPTIMIZED:
                return new BruteForceOptimized(fullDistanceFunc, distanceFunc);
            case NEAREST_NEIGHBOR_HEURISTIC:
                return new NearestNeighborHeuristic(fullDistanceFunc, distanceFunc);
            default:
                throw new IllegalArgumentException("Unsupported algorithm type: " + type);
        }
    }

    /**
     * Applies the traveling salesman algorithm to find the optimized route.
     * This method validates the input points and calls the algorithm-specific implementation.
     *
     * @param startingPoint the starting point of the route
     * @param points a list of points to be visited, excluding the starting point
     * @return a list of points representing the optimized route
     * @throws IllegalArgumentException if the points list is invalid
     */
     public List<JOpenCageLatLng> apply(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) throws IllegalArgumentException {
        checkValidPoints(startingPoint, points);
        return performAlgorithm(startingPoint, points);
    }

     /**
     * Validates the passed arguments for the algorithm
     *
     * @param startingPoint the point to start from, which must not be in the list of points
     * @param points the list of points to be sorted
     * @throws IllegalArgumentException if points is null, has fewer than 2 elements, or if the starting point is null
     *                                  or is contained in the list
     */
    private static void checkValidPoints(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) throws IllegalArgumentException {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("Points cannot be null and must have at least two elements");
        }
        if (startingPoint == null) {
            throw new IllegalArgumentException("Starting point can't be null");
        }
        if (points.contains(startingPoint)) {
            throw new IllegalArgumentException("Starting point cannot be contained in the list of points");
        }
    }

    /**
     * Abstract method for performing the specific algorithm (e.g., nearest-neighbor, brute force, etc.).
     * Subclasses must implement this method to provide the actual algorithm.
     *
     * @param startingPoint the starting point of the journey
     * @param points the list of points to be visited (excluding the starting point)
     * @return a list of points representing the optimized route
     */
    protected abstract List<JOpenCageLatLng> performAlgorithm(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points);

}
