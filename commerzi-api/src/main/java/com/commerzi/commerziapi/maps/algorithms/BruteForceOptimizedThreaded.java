package com.commerzi.commerziapi.maps.algorithms;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The BruteForceOptimizedThreaded class implements a multi-threaded, optimized brute-force algorithm for solving
 * the Traveling Salesman Problem (TSP).
 * This approach uses a depth-first search (DFS) method to explore all possible paths and optimizes the search by
 * pruning paths that exceed the current best known distance.
 *
 * Each thread independently explores a set of possible routes, ensuring a more efficient traversal of the problem space.
 * The results are merged safely using a thread-safe result storage mechanism, ensuring that the best path and distance
 * are updated concurrently across threads.
 *
 * The algorithm is designed to run faster than a single-threaded brute force solution by leveraging parallelism,
 * but it still faces the factorial time complexity inherent to brute-force algorithms.
 *
 * @see ATravelerAlgorithm
 */
public class BruteForceOptimizedThreaded extends ATravelerAlgorithm {

    /**
     * Constructor for initializing the BruteForceOptimizedThreaded algorithm.
     * This constructor passes the distance functions to the superclass constructor.
     *
     * @param fullDistanceFunc a function to calculate the total distance for a given route
     * @param distanceFunc a function to calculate the distance between two points
     * @see ATravelerAlgorithm#ATravelerAlgorithm(Function, BiFunction)
     */
    public BruteForceOptimizedThreaded(Function<List<Coordinates>, Double> fullDistanceFunc, BiFunction<Coordinates, Coordinates, Double> distanceFunc) {
        super(fullDistanceFunc, distanceFunc);
    }

    /**
     * Thread-safe result storage used to store the best path and the corresponding distance.
     * This ensures that the path and distance are updated correctly even when accessed by multiple threads.
     */
    private static class ThreadSafeResult {
        private final Object lock = new Object();
        private double minDistance = Double.MAX_VALUE;
        private final List<Coordinates> bestPath = new ArrayList<>();

        /**
         * Updates the best path and distance if a new shorter path is found.
         *
         * @param newDistance the new total distance of the path
         * @param newPath the new path being evaluated
         */
        public void update(double newDistance, List<Coordinates> newPath) {
            synchronized (lock) {
                if (newDistance < minDistance) {
                    minDistance = newDistance;
                    bestPath.clear();
                    bestPath.addAll(newPath);
                }
            }
        }

        /**
         * Returns the minimum distance found so far.
         *
         * @return the minimum distance found
         */
        public double getMinDistance() {
            synchronized (lock) {
                return minDistance;
            }
        }

        /**
         * Returns the best path found so far.
         *
         * @return the best path
         */
        public List<Coordinates> getBestPath() {
            synchronized (lock) {
                return bestPath;
            }
        }
    }

    /**
     * A thread that performs the actual brute force search for a given set of points.
     * Each thread independently explores paths starting from a specific point, allowing parallelism to speed up the search.
     */
    private class BruteForceActualThread implements Runnable {
        private final Coordinates startingPoint;
        private final List<Coordinates> points;
        private final List<Coordinates> currentPath;
        private final boolean[] visited;
        private final ThreadSafeResult result;
        private double currentDistance;

         /**
          * Constructs a BruteForceActualThread instance for a given starting point, points to visit, and current path.
          *
          * @param startingPoint the starting point of the journey
          * @param points the list of points to visit
          * @param currentPath the current path being evaluated
          * @param visited an array to track which points have been visited
          * @param result a thread-safe result object to store the best path and distance
          * @param currentDistance the current distance of the path
          */
        public BruteForceActualThread(Coordinates startingPoint, List<Coordinates> points, List<Coordinates> currentPath,
                                      boolean[] visited, ThreadSafeResult result, double currentDistance) {
            this.startingPoint = startingPoint;
            this.points = points;
            this.currentPath = currentPath;
            this.visited = visited;
            this.result = result;
            this.currentDistance = currentDistance;
        }

        @Override
        public void run() {
            explorePaths();
        }

        /**
         * Explores all possible paths from the current point using depth-first search.
         * If a complete path is found, it checks if it is the best path.
         * If the current path distance exceeds the best known distance, it prunes the search.
         */
        private void explorePaths() {
            if (currentPath.size() == points.size() + 1) {
                double totalDistance = currentDistance + distanceBetweenTwoPoints.apply(currentPath.get(currentPath.size() - 1), startingPoint);
                result.update(totalDistance, currentPath);
                return;
            }

            if (currentPath.size() >= 2 && currentDistance > result.getMinDistance()) {
                return;
            }

            for (int i = 0; i < points.size(); i++) {
                if (!visited[i]) {
                    visited[i] = true;
                    Coordinates point = points.get(i);

                    Double distanceToAdd = distanceBetweenTwoPoints.apply(currentPath.get(currentPath.size() - 1), point);
                    currentDistance += distanceToAdd;
                    currentPath.add(point);

                    explorePaths();

                    currentDistance -= distanceToAdd;

                    currentPath.remove(point);
                    visited[i] = false;
                }
            }
        }
    }

    /**
     * Performs the TSP solution using a multi-threaded brute force approach.
     * It spawns multiple threads to explore different paths in parallel, each starting from a different point in the list.
     *
     * @param startingPoint the starting point of the journey
     * @param points the list of points to visit
     * @return the optimal route as a list of points, excluding the starting and ending points in the returned path
     */
    @Override
    protected List<Coordinates> performAlgorithm(Coordinates startingPoint, List<Coordinates> points) {
        ThreadSafeResult result = new ThreadSafeResult();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            List<Coordinates> pointsToVisit = new ArrayList<>(points);
            pointsToVisit.remove(i);
            boolean[] visited = new boolean[pointsToVisit.size()];

            List<Coordinates> currentPath = new ArrayList<>();
            currentPath.add(points.get(i));

            Thread thread = new Thread(new BruteForceActualThread(startingPoint, pointsToVisit, new ArrayList<>(currentPath), visited, result, distanceBetweenTwoPoints.apply(startingPoint, points.get(i))));
            thread.start();
            threads.add(thread);
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.getBestPath();
    }
}
