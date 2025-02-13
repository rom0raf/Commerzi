package com.commerzi.commerziapi.maps.algorithms;

import java.util.concurrent.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

/**
 * The BruteForceThreaded class implements the brute force algorithm for solving the Traveling Salesman Problem (TSP)
 * in a multithreaded way. It starts 1 thread per point in the list of points, and then generates all the possible
 * permutations starting from this chosen point. It also calculates the total distance for each permutation,
 * ultimately selecting the shortest possible route. The algorithm assumes that the journey starts and ends at the
 * specified starting point, making it a round-trip optimization.
 *
 * This algorithm is computationally expensive as it evaluates every possible route, making it suitable only for small
 * numbers of points due to its factorial time complexity.
 *
 * [WARNING] This method is slower than the classic {@link BruteForce} with small amount of points.
 * [WARNING] Also this method is still slow compared to {@link BruteForceOptimized}.
 *
 * @see ATravelerAlgorithm
 */
public class BruteForceThreaded extends ATravelerAlgorithm {

    /**
     * Constructor for initializing the BruteForceThreaded algorithm.
     * This constructor passes the distance functions to the superclass constructor.
     *
     * @param fullDistanceFunc a function to calculate the total distance for a given route
     * @param distanceFunc a function to calculate the distance between two points
     * @see ATravelerAlgorithm#ATravelerAlgorithm(Function, BiFunction)
     */
    public BruteForceThreaded(Function<List<JOpenCageLatLng>, Double> fullDistanceFunc, BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc) {
        super(fullDistanceFunc, distanceFunc);
    }

    /**
     * The BruteForceThreadResult class holds the result of a single thread's execution in the BruteForceThreaded algorithm.
     * It stores the best path and its corresponding distance for that specific execution.
     */
    private static class BruteForceThreadResult {
        /**
         * The optimal path for the specific thread, excluding the start and end points.
         */
        public final List<JOpenCageLatLng> path;

        /**
         * The distance of the optimal path.
         */
        public final double distance;

        /**
         * Constructor for initializing the BruteForceThreadResult object.
         *
         * @param path the optimal path found by the thread
         * @param distance the distance of the optimal path
         */
        BruteForceThreadResult(List<JOpenCageLatLng> path, double distance) {
            this.path = path;
            this.distance = distance;
        }
    }

    /**
     * The BruteForceThread class is responsible for calculating the optimal path for a given starting point and a list of points
     * to visit. It is executed as a task in a separate thread.
     */
    private class BruteForceThread implements Callable<BruteForceThreadResult> {
        /**
         * The starting point of the journey for this thread.
         */
        private JOpenCageLatLng startingPoint;

        /**
         * The point in the list that this thread considers as the starting point for its calculations.
         */
        private JOpenCageLatLng listStartingPoint;

        /**
         * The list of points to be visited, excluding the starting point.
         */
        private List<JOpenCageLatLng> points;

        /**
         * Constructor for initializing the BruteForceThread object.
         *
         * @param startingPoint the starting point of the journey for the thread
         * @param listStartingPoint the specific point in the list chosen as the starting point for this thread's calculations
         * @param points the list of points to visit (excluding the starting point)
         */
        public BruteForceThread(JOpenCageLatLng startingPoint, JOpenCageLatLng listStartingPoint, List<JOpenCageLatLng> points) {
            this.startingPoint = startingPoint;
            this.points = points;
            this.listStartingPoint = listStartingPoint;
        }

        /**
         * The method executed by the thread, which generates all possible permutations of the points,
         * calculates the distance for each permutation, and returns the best (shortest) path found.
         *
         * @return the optimal path for the given thread, along with its corresponding distance
         */
        @Override
        public BruteForceThreadResult call() {
            List<List<JOpenCageLatLng>> permutations = BruteForceUtils.generatePermutations(points);
            List<JOpenCageLatLng> bestPath = null;
            double minDistance = Double.MAX_VALUE;

            for (List<JOpenCageLatLng> path : permutations) {
                List<JOpenCageLatLng> fullPath = new ArrayList<>();
                fullPath.add(startingPoint);

                // We need to add the actual starting point of the list too
                fullPath.add(listStartingPoint);
                fullPath.addAll(path);
                fullPath.add(startingPoint);

                double distance = fullDistanceOverPointsFunc.apply(fullPath);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestPath = new ArrayList<>(fullPath);
                }
            }

            // We delete starting and end points
            bestPath.remove(0);
            bestPath.remove(bestPath.size() - 1);

            return new BruteForceThreadResult(bestPath, minDistance);
        }
    }

    /**
     * The performAlgorithm method overrides the base class method to execute the Traveling Salesman Problem (TSP) algorithm
     * using multiple threads. Each thread starts from a different point in the list and calculates the shortest path from that point.
     * It then selects the optimal path across all threads.
     *
     * @param startingPoint the starting point of the journey
     * @param points the list of points to visit (excluding the starting point)
     * @return the optimal path as a list of points, including the starting point at the beginning
     */
    @Override
    protected List<JOpenCageLatLng> performAlgorithm(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        ExecutorService executor = Executors.newFixedThreadPool(points.size());
        List<Future<BruteForceThreadResult>> futures = new ArrayList<>();

        for (JOpenCageLatLng point : points) {
            List<JOpenCageLatLng> pointsToVisit = new ArrayList<>(points);
            pointsToVisit.remove(point);
            BruteForceThread task = new BruteForceThread(startingPoint, point, pointsToVisit);
            futures.add(executor.submit(task));
        }

        List<JOpenCageLatLng> bestPath = null;
        double minDistance = Double.MAX_VALUE;

        try {
            for (Future<BruteForceThreadResult> future : futures) {
                BruteForceThreadResult result = future.get();
                if (result.distance < minDistance) {
                    minDistance = result.distance;
                    bestPath = result.path;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return bestPath;
    }
}

