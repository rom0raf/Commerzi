package com.commerzi.commerziapi.maps.algorithms;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The BruteForce class implements the brute force algorithm for solving the Traveling Salesman Problem (TSP).
 * It generates all possible permutations of the given points and calculates the total distance for each permutation,
 * ultimately selecting the shortest possible route. The algorithm assumes that the journey starts and ends at the
 * specified starting point, making it a round-trip optimization.
 *
 * This algorithm is computationally expensive as it evaluates every possible route, making it suitable only for small
 * numbers of points due to its factorial time complexity.
 *
 * [WARNING] This method is VERY slow with more than 10 points.
 *
 * @see ATravelerAlgorithm
 */
public class BruteForce extends ATravelerAlgorithm {

    /**
     * Constructor for initializing the BruteForce algorithm.
     * This constructor passes the distance functions to the superclass constructor.
     *
     * @param fullDistanceFunc a function to calculate the total distance for a given route
     * @param distanceFunc a function to calculate the distance between two points
     * @see ATravelerAlgorithm#ATravelerAlgorithm(Function, BiFunction)
     */
    public BruteForce(Function<List<JOpenCageLatLng>, Double> fullDistanceFunc, BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc) {
        super(fullDistanceFunc, distanceFunc);
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
     * Generates all possible permutations of a list of points (excluding the starting point)
     * and calculates the total distance for each permutation to find the shortest path.
     * This algorithm takes into account the starting point AND end point.
     *
     * [WARNING] This method is VERY slow with more than 10 points.
     *
     * @param startingPoint the starting point of the journey
     * @param points the list of points to visit (excluding the starting point)
     * @return the optimal path as a list of points with the shortest distance, including the starting point at the beginning
     */
    @Override
    protected List<JOpenCageLatLng> performAlgorithm(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        List<JOpenCageLatLng> pointsToVisit = new ArrayList<>(points);
        List<List<JOpenCageLatLng>> permutations = generatePermutations(pointsToVisit);

        List<JOpenCageLatLng> bestPath = null;
        double minDistance = Double.MAX_VALUE;

        for (List<JOpenCageLatLng> path : permutations) {
            List<JOpenCageLatLng> fullPath = new ArrayList<>();
            fullPath.add(startingPoint);
            fullPath.addAll(path);
            fullPath.add(startingPoint);

            double distance = this.fullDistanceOverPointsFunc.apply(fullPath);
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
