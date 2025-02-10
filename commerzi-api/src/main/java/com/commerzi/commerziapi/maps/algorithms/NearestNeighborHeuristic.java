package com.commerzi.commerziapi.maps.algorithms;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The NearestNeighborHeuristic class implements the nearest neighbor algorithm for solving the Traveling Salesman Problem (TSP).
 * This heuristic algorithm starts from a given point and iteratively chooses the closest unvisited point as the next destination.
 * The algorithm proceeds until all points have been visited. While it does not guarantee the optimal solution, it provides a
 * computationally efficient approach to finding a reasonable solution.
 *
 * This method only considers the shortest path starting from the given point without explicitly considering the final return to the starting point.
 *
 * @see ATravelerAlgorithm
 */
public class NearestNeighborHeuristic extends ATravelerAlgorithm {

    /**
     * Constructor for initializing the NearestNeighborHeuristic algorithm.
     * This constructor passes the distance functions to the superclass constructor.
     *
     * @param fullDistanceFunc a function to calculate the total distance for a given route
     * @param distanceFunc a function to calculate the distance between two points
     * @see ATravelerAlgorithm#ATravelerAlgorithm(Function, BiFunction)
     */
    public NearestNeighborHeuristic(Function<List<JOpenCageLatLng>, Double> fullDistanceFunc, BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc) {
        super(fullDistanceFunc, distanceFunc);
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
     */
    @Override
    protected List<JOpenCageLatLng> performAlgorithm(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
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
                    double distance = this.distanceBetweenTwoPoints.apply(sortedPoints.get(sortedPoints.size() - 1), points.get(j));
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
