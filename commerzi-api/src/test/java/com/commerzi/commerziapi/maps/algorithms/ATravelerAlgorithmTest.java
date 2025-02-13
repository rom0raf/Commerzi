package com.commerzi.commerziapi.maps.algorithms;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ATravelerAlgorithmTest {

    public static ArrayList<Coordinates> points = new ArrayList<>() {{
        // Toulouse
        add(new Coordinates(43.6047, 1.4442));

        // Montpellier
        add(new Coordinates(43.6117, 3.8767));

        // Nîmes
        add(new Coordinates(43.8367, 4.3601));

        // Perpignan
        add(new Coordinates(42.6977, 2.8956));

        // Carcassonne
        add(new Coordinates(43.2119, 2.3500));

        // Béziers
        add(new Coordinates(43.3440, 3.2192));

        // Albi
        add(new Coordinates(43.9263, 2.1500));

        // Tarbes
        add(new Coordinates(43.2361, 0.0822));
    }};

    // IUT de Rodez
    public static final Coordinates startingPoint = new Coordinates(44.360052, 2.575674);

    private void testIllegalArguments(ATravelerAlgorithm algorithm) {
        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.apply(startingPoint, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.apply(startingPoint, new ArrayList<>() {{
                add(points.get(0));
            }});
        });

        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.apply(startingPoint, new ArrayList<>() {{
                add(startingPoint);
                add(points.get(1));
            }});
        });

        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.apply(null, points);
        });
    }

    @Nested
    class NearestNeighborHeuristicTests {

        private final ATravelerAlgorithm algorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.NEAREST_NEIGHBOR_HEURISTIC);

        @Test
        void basic() {
            List<Coordinates> pointsToSort = new ArrayList<>() {{
                add(points.get(0)); // Toulouse
                add(points.get(2)); // Nîmes
                add(points.get(1)); // Montpellier
            }};

            List<Coordinates> sortedPoints = algorithm.apply(startingPoint, pointsToSort);

            assertEquals(points.get(0), sortedPoints.get(0)); // Toulouse
            assertEquals(points.get(1), sortedPoints.get(1)); // Montpellier
            assertEquals(points.get(2), sortedPoints.get(2)); // Nîmes
        }

        @Test
        void complete() {
            List<Coordinates> sortedPoints = algorithm.apply(startingPoint, points);

            assertEquals(points.get(6), sortedPoints.get(0)); // Albi
            assertEquals(points.get(0), sortedPoints.get(1)); // Toulouse
            assertEquals(points.get(4), sortedPoints.get(2)); // Carcasonne
            assertEquals(points.get(5), sortedPoints.get(3)); // Bézier
            assertEquals(points.get(1), sortedPoints.get(4)); // Montpellier
            assertEquals(points.get(2), sortedPoints.get(5)); // Nimes
            assertEquals(points.get(3), sortedPoints.get(6)); // Perpignan
            assertEquals(points.get(7), sortedPoints.get(7)); // Tarbes
        }

        @Test
        void illegalArguments() {
            testIllegalArguments(algorithm);
        }
    }
    
    @Nested
    class BruteForceTests {
        
        private final ATravelerAlgorithm algorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE);
        
        @Test
        void basic() {
            List<Coordinates> pointsToVisit = new ArrayList<>() {{
                add(points.get(0)); // Toulouse
                add(points.get(2)); // Nîmes
                add(points.get(1)); // Montpellier
            }};

            List<Coordinates> optimalPath = algorithm.apply(startingPoint, pointsToVisit);

            assertEquals(points.get(0), optimalPath.get(0)); // Toulouse
            assertEquals(points.get(1), optimalPath.get(1)); // Montpellier
            assertEquals(points.get(2), optimalPath.get(2)); // Nîmes
        }

        @Test
        void complete() {
            List<Coordinates> optimalPath = algorithm.apply(startingPoint, points);

            assertEquals(points.get(2), optimalPath.get(0)); // Nimes
            assertEquals(points.get(1), optimalPath.get(1)); // Montpellier
            assertEquals(points.get(5), optimalPath.get(2)); // Bézier
            assertEquals(points.get(3), optimalPath.get(3)); // Perpignan
            assertEquals(points.get(4), optimalPath.get(4)); // Carcasonne
            assertEquals(points.get(7), optimalPath.get(5)); // Tarbes
            assertEquals(points.get(0), optimalPath.get(6)); // Toulouse
            assertEquals(points.get(6), optimalPath.get(7)); // Albi
        }

        @Test
        void illegalArguments() {
            testIllegalArguments(algorithm);
        }
    }
    
    @Nested
    class BruteForceOptimizedTests {

        private final ATravelerAlgorithm algorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE_OPTIMIZED);
        private final ATravelerAlgorithm bruteAlgorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE);

        @Test
        void basic() {
            List<Coordinates> pointsToVisit = new ArrayList<>() {{
                add(points.get(0)); // Toulouse
                add(points.get(2)); // Nîmes
                add(points.get(1)); // Montpellier
            }};

            List<Coordinates> optimalPath = algorithm.apply(startingPoint, pointsToVisit);
            List<Coordinates> bruteForcedPath = bruteAlgorithm.apply(startingPoint, pointsToVisit);

            assertEquals(optimalPath, bruteForcedPath);
        }

        @Test
        void complete() {
            List<Coordinates> optimalPath = algorithm.apply(startingPoint, points);
            List<Coordinates> bruteForcedPath = bruteAlgorithm.apply(startingPoint, points);

            assertEquals(optimalPath, bruteForcedPath);
        }

        @Test
        void illegalArguments() {
            testIllegalArguments(algorithm);
        }
    }

    @Nested
    class BruteForceThreadedTests {

        private final ATravelerAlgorithm algorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE_THREADED);
        private final ATravelerAlgorithm bruteAlgorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE_OPTIMIZED);

        @Test
        void basic() {
            List<Coordinates> pointsToVisit = new ArrayList<>() {{
                add(points.get(0)); // Toulouse
                add(points.get(2)); // Nîmes
                add(points.get(1)); // Montpellier
            }};

            List<Coordinates> optimalPath = algorithm.apply(startingPoint, pointsToVisit);
            List<Coordinates> bruteForcedPath = bruteAlgorithm.apply(startingPoint, pointsToVisit);

            assertEquals(optimalPath, bruteForcedPath);
        }

        @Test
        void complete() {
            List<Coordinates> optimalPath = algorithm.apply(startingPoint, points);
            List<Coordinates> bruteForcedPath = bruteAlgorithm.apply(startingPoint, points);

            assertEquals(optimalPath, bruteForcedPath);
        }

        @Test
        void illegalArguments() {
            testIllegalArguments(algorithm);
        }
    }

    @Nested
    class BruteForceOptimizedThreadedTests {

        private final ATravelerAlgorithm algorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE_OPTIMIZED_THREADED);
        private final ATravelerAlgorithm bruteAlgorithm = ATravelerAlgorithm.getAlgorithmWithFlyingDistances(AlgorithmType.BRUTE_FORCE_OPTIMIZED);

        @Test
        void basic() {
            List<Coordinates> pointsToVisit = new ArrayList<>() {{
                add(points.get(0)); // Toulouse
                add(points.get(2)); // Nîmes
                add(points.get(1)); // Montpellier
            }};

            List<Coordinates> optimalPath = algorithm.apply(startingPoint, pointsToVisit);
            List<Coordinates> bruteForcedPath = bruteAlgorithm.apply(startingPoint, pointsToVisit);

            assertEquals(optimalPath, bruteForcedPath);
        }

        @Test
        void complete() {
            List<Coordinates> optimalPath = algorithm.apply(startingPoint, points);
            List<Coordinates> bruteForcedPath = bruteAlgorithm.apply(startingPoint, points);

            assertEquals(optimalPath, bruteForcedPath);
        }

        @Test
        void illegalArguments() {
            testIllegalArguments(algorithm);
        }
    }

}
