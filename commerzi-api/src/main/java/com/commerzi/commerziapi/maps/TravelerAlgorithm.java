package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides algorithms for filtering and optimizing the Traveling Salesman Problem.
 * All methods in this class have the same signature: they take a starting point (for this use case, it will be
 * the commercial home), and they take a list of `JOpenCageLatLng` points and return a sorted list of the
 * same type, representing an optimized travel path.
 * Starting point is assumed as starting point AND end point for some algorithms that can take this into account.
 *
 * This class can be used to provide different sorting strategies by passing in different implementations
 * of the method signature as a `BiFunction<JOpenCageLatLng, List<JOpenCageLatLng>, List<JOpenCageLatLng>>`.
 */
public class TravelerAlgorithm {

    /**
     * Do some validation over the passed arguments to the sorting functions
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
     * Generates all permutations of a list of points.
     *
     * @param list the list of points
     * @return a list of all permutations of the input list
     */
    private static List<List<JOpenCageLatLng>> generatePermutations(List<JOpenCageLatLng> list) {
        List<List<JOpenCageLatLng>> permutations = new ArrayList<>();
        if (list.size() == 1) {
            permutations.add(new ArrayList<>(list));
        } else {
            for (int i = 0; i < list.size(); i++) {
                JOpenCageLatLng current = list.get(i);
                List<JOpenCageLatLng> remaining = new ArrayList<>(list);
                remaining.remove(i);

                List<List<JOpenCageLatLng>> remainingPermutations = generatePermutations(remaining);
                for (List<JOpenCageLatLng> perm : remainingPermutations) {
                    perm.add(0, current);
                    permutations.add(perm);
                }
            }
        }
        return permutations;
    }

    /**
     * Sorts the points to create the shortest path, starting at the specified starting point
     * and visiting each point exactly once using the nearest-neighbor heuristic.
     * This function assumes that all the points are distinct.
     * This algorithm takes into account only the starting point, and not the endpoint.
     *
     * @param startingPoint the point to start from, which must not be in the list of points
     * @param points the list of points to be sorted
     * @return points sorted to form the shortest possible route, starting from the specified starting point
     * @throws IllegalArgumentException if points is null, has fewer than 2 elements, or if the starting point is null
     *                                  or is contained in the list
     */
    public static List<JOpenCageLatLng> nearestNeihborHeuristic(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        checkValidPoints(startingPoint, points);

        List<JOpenCageLatLng> sortedPoints = new ArrayList<>();
        sortedPoints.add(startingPoint);

        boolean[] visited = new boolean[points.size()];

        for (int i = 0; i < points.size(); i++) {
            visited[i] = false;
        }

        for (int i = 0; i < points.size(); i++) {
            double closestDistance = Double.MAX_VALUE;
            int closestPointIndex = -1;

            for (int j = 0; j < points.size(); j++) {
                if (!visited[j]) {
                    double distance = MapsUtils.flyingDistanceBetweenTwoPoints(sortedPoints.get(sortedPoints.size() - 1), points.get(j));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPointIndex = j;
                    }
                }
            }

            sortedPoints.add(points.get(closestPointIndex));
            visited[closestPointIndex] = true;
        }

        sortedPoints.remove(0);
        return sortedPoints;
    }

    /**
     * Generates all possible permutations of a list of points (excluding the starting point)
     * and calculates the total distance for each permutation to find the shortest path.
     * This algorithm takes into account the starting point AND end point.
     *
     * @param startingPoint the starting point of the journey
     * @param points the list of points to visit (excluding the starting point)
     * @return the optimal path as a list of points with the shortest distance, including the starting point at the beginning
     * @throws IllegalArgumentException if points is null, has fewer than 2 elements, or if the starting point is null
     *                                  or is contained in the list
     */
    public static List<JOpenCageLatLng> bruteForce(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        checkValidPoints(startingPoint, points);

        List<JOpenCageLatLng> pointsToVisit = new ArrayList<>(points);
        List<List<JOpenCageLatLng>> permutations = generatePermutations(pointsToVisit);

        List<JOpenCageLatLng> bestPath = null;
        double minDistance = Double.MAX_VALUE;

        for (List<JOpenCageLatLng> path : permutations) {
            List<JOpenCageLatLng> fullPath = new ArrayList<>();
            fullPath.add(startingPoint);
            fullPath.addAll(path);
            fullPath.add(startingPoint);

            double distance = MapsUtils.fullFlyingDistanceOverPoints(fullPath);
            if (distance < minDistance) {
                minDistance = distance;
                bestPath = new ArrayList<>(fullPath);
            }
        }

        // We delete starting and end points
        bestPath.remove(0);
        bestPath.remove(bestPath.size() -1);

        return bestPath;
    }

}