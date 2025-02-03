package com.commerzi.commerziapi.maps.algorithms;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The BruteForceOptimized class implements an optimized brute force algorithm for solving the Traveling Salesman Problem (TSP).
 * Similar to the regular brute force method, it generates all possible permutations of the list of points and evaluates
 * the total distance for each permutation. However, this version uses a depth-first search approach, dynamically
 * pruning paths that exceed the current best distance, thus reducing the number of evaluated paths.
 *
 * This algorithm still suffers from factorial time complexity, but its optimization attempts to cut down on unnecessary computations.
 *
 * @see ATravelerAlgorithm
 */
public class BruteForceOptimized extends ATravelerAlgorithm {

    /**
     * Constructor for initializing the BruteForceOptimized algorithm.
     * This constructor passes the distance functions to the superclass constructor.
     *
     * @param fullDistanceFunc a function to calculate the total distance for a given route
     * @param distanceFunc a function to calculate the distance between two points
     * @see ATravelerAlgorithm#ATravelerAlgorithm(Function, BiFunction)
     */
    public BruteForceOptimized(Function<List<JOpenCageLatLng>, Double> fullDistanceFunc, BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc) {
        super(fullDistanceFunc, distanceFunc);
    }

    /**
     * Explores all possible paths in a depth-first search (DFS) manner and updates the best path if a shorter one is found.
     * Optimizes distance calculation by keeping track of partial distances.
     *
     * @param startingPoint the starting point of the journey
     * @param points the list of points to visit
     * @param currentPath the current path being evaluated
     * @param visited a boolean array tracking visited points
     * @param bestPath the list to store the best found path
     * @param minDistance an array containing the minimum distance found so far
     * @param currentDistance the current partial distance of the path
     */
    private void explorePaths(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points,
                              List<JOpenCageLatLng> currentPath, boolean[] visited,
                              List<JOpenCageLatLng> bestPath, double[] minDistance, double currentDistance) {
        if (currentPath.size() == points.size()) {
            double totalDistance = currentDistance + this.distanceBetweenTwoPoints.apply(currentPath.get(currentPath.size() - 1), startingPoint);

            if (totalDistance < minDistance[0]) {
                minDistance[0] = totalDistance;
                bestPath.clear();
                bestPath.addAll(currentPath);
            }

            return;
        }

        if (currentPath.size() >= 2 && currentDistance > minDistance[0]) {
            return;
        }

        for (int i = 0; i < points.size(); i++) {
            if (!visited[i]) {
                visited[i] = true;
                currentPath.add(points.get(i));

                double newDistance = currentDistance;
                if (currentPath.size() == 1) {
                    newDistance += this.distanceBetweenTwoPoints.apply(startingPoint, points.get(i));
                } else {
                    newDistance += this.distanceBetweenTwoPoints.apply(currentPath.get(currentPath.size() - 2), points.get(i));
                }

                explorePaths(startingPoint, points, currentPath, visited, bestPath, minDistance, newDistance);

                currentPath.remove(currentPath.size() - 1);
                visited[i] = false;
            }
        }
    }

    /**
     * This method performs the Brute Force Optimized algorithm to find the optimal TSP route.
     * It generates all paths, evaluates their distances, and selects the shortest route that returns to the starting point.
     *
     * @param startingPoint the point to start the journey from
     * @param points the list of points to visit
     * @return the optimal route as a list of points, excluding the starting and ending points in the returned path
     */
    @Override
    protected List<JOpenCageLatLng> performAlgorithm(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        List<JOpenCageLatLng> bestPath = new ArrayList<>();
        double minDistance = Double.MAX_VALUE;

        double[] minDistanceArray = new double[]{minDistance};

        List<JOpenCageLatLng> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[points.size()];

        explorePaths(startingPoint, points, currentPath, visited, bestPath, minDistanceArray, 0);

        return bestPath;
    }

}
