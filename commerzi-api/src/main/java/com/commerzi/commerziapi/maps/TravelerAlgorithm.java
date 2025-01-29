package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides algorithms for filtering and optimizing the Traveling Salesman Problem.
 * All methods in this class have the same signature: they take a starting point (for this use case, it will be
 * the commercial home), and they take a list of `JOpenCageLatLng` points and return a sorted list of the
 * same type, representing an optimized travel path.
 *
 * This class can be used to provide different sorting strategies by passing in different implementations
 * of the method signature as a `BiFunction<JOpenCageLatLng, List<JOpenCageLatLng>, List<JOpenCageLatLng>>`.
 */
public class TravelerAlgorithm {

    /**
     * Sorts the points to create the shortest path, starting at the specified starting point
     * and visiting each point exactly once using the nearest-neighbor heuristic.
     * This function assumes that all the points are distinct.
     *
     * @param startingPoint the point to start from, which must not be in the list of points
     * @param points the list of points to be sorted
     * @return points sorted to form the shortest possible route, starting from the specified starting point
     * @throws IllegalArgumentException if points is null, has fewer than 2 elements, or if the starting point is null
     *                                  or is contained in the list
     */
    public static List<JOpenCageLatLng> sortPointsByShortestPath(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("Points cannot be null and must have at least two elements");
        }
        if (startingPoint == null) {
            throw new IllegalArgumentException("Starting point can't be null");
        }
        if (points.contains(startingPoint)) {
            throw new IllegalArgumentException("Starting point cannot be contained in the list of points");
        }

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
                    double distance = MapsUtils.distanceBetweenTwoPoints(sortedPoints.get(sortedPoints.size() - 1), points.get(j));
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

}
