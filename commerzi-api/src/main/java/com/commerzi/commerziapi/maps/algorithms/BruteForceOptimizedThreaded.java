package com.commerzi.commerziapi.maps.algorithms;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BruteForceOptimizedThreaded extends ATravelerAlgorithm {

    public BruteForceOptimizedThreaded(Function<List<JOpenCageLatLng>, Double> fullDistanceFunc, BiFunction<JOpenCageLatLng, JOpenCageLatLng, Double> distanceFunc) {
        super(fullDistanceFunc, distanceFunc);
    }

    // Thread-safe result storage
    private static class ThreadSafeResult {
        private final Object lock = new Object();
        private double minDistance = Double.MAX_VALUE;
        private List<JOpenCageLatLng> bestPath = new ArrayList<>();

        public void update(double newDistance, List<JOpenCageLatLng> newPath) {
            synchronized (lock) {
                if (newDistance < minDistance) {
                    minDistance = newDistance;
                    bestPath.clear();
                    bestPath.addAll(newPath);
                }
            }
        }

        public double getMinDistance() {
            synchronized (lock) {
                return minDistance;
            }
        }

        public List<JOpenCageLatLng> getBestPath() {
            synchronized (lock) {
                return bestPath;
            }
        }
    }

    // The DFS search performed by each thread
    private class DFSOptimizedThread implements Callable<Void> {
        private final JOpenCageLatLng startingPoint;
        private final List<JOpenCageLatLng> points;
        private final List<JOpenCageLatLng> currentPath;
        private final boolean[] visited;
        private final ThreadSafeResult result;
        private double currentDistance;

        public DFSOptimizedThread(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points, List<JOpenCageLatLng> currentPath,
                                  boolean[] visited, ThreadSafeResult result, double currentDistance) {
            this.startingPoint = startingPoint;
            this.points = points;
            this.currentPath = currentPath;
            this.visited = visited;
            this.result = result;
            this.currentDistance = currentDistance;
        }

        @Override
        public Void call() {
            explorePaths();
            return null;
        }

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
                    JOpenCageLatLng point = points.get(i);

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

    @Override
    protected List<JOpenCageLatLng> performAlgorithm(JOpenCageLatLng startingPoint, List<JOpenCageLatLng> points) {
        ThreadSafeResult result = new ThreadSafeResult();
        ExecutorService executor = Executors.newFixedThreadPool(points.size());

        List<DFSOptimizedThread> tasks = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            List<JOpenCageLatLng> pointsToVisit = new ArrayList<>(points);
            pointsToVisit.remove(i);
            boolean[] visited = new boolean[pointsToVisit.size()];

            List<JOpenCageLatLng> currentPath = new ArrayList<>();
            currentPath.add(points.get(i));

            tasks.add(new DFSOptimizedThread(startingPoint, pointsToVisit, new ArrayList<>(currentPath), visited, result, distanceBetweenTwoPoints.apply(startingPoint, points.get(i))));
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return result.getBestPath();
    }
}
