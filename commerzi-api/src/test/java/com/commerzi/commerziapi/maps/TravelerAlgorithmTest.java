package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TravelerAlgorithmTest {

    // IUT de Rodez
    public static final JOpenCageLatLng startingPoint = new JOpenCageLatLng() {{
        setLat(44.360052);
        setLng(2.575674);
    }};

    @Nested
    class NearestNeighborHeuristicTests {
        @Test
        void basic() {
            List<JOpenCageLatLng> pointsToSort = new ArrayList<>() {{
                add(MapsUtilsTest.points.get(0)); // Toulouse
                add(MapsUtilsTest.points.get(2)); // Nîmes
                add(MapsUtilsTest.points.get(1)); // Montpellier
            }};

            List<JOpenCageLatLng> sortedPoints = TravelerAlgorithm.nearestNeihborHeuristic(startingPoint, pointsToSort);

            Assertions.assertEquals(MapsUtilsTest.points.get(0), sortedPoints.get(0)); // Toulouse
            Assertions.assertEquals(MapsUtilsTest.points.get(1), sortedPoints.get(1)); // Montpellier
            Assertions.assertEquals(MapsUtilsTest.points.get(2), sortedPoints.get(2)); // Nîmes
        }

        @Test
        void complete() {
            List<JOpenCageLatLng> sortedPoints = TravelerAlgorithm.nearestNeihborHeuristic(startingPoint, MapsUtilsTest.points);

            Assertions.assertEquals(MapsUtilsTest.points.get(6), sortedPoints.get(0)); // Albi
            Assertions.assertEquals(MapsUtilsTest.points.get(0), sortedPoints.get(1)); // Toulouse
            Assertions.assertEquals(MapsUtilsTest.points.get(4), sortedPoints.get(2)); // Carcasonne
            Assertions.assertEquals(MapsUtilsTest.points.get(5), sortedPoints.get(3)); // Bézier
            Assertions.assertEquals(MapsUtilsTest.points.get(1), sortedPoints.get(4)); // Montpellier
            Assertions.assertEquals(MapsUtilsTest.points.get(2), sortedPoints.get(5)); // Nimes
            Assertions.assertEquals(MapsUtilsTest.points.get(3), sortedPoints.get(6)); // Perpignan
            Assertions.assertEquals(MapsUtilsTest.points.get(7), sortedPoints.get(7)); // Tarbes
        }

        @Test
        void nullPoints() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.nearestNeihborHeuristic(startingPoint, null);
            });
        }

        @Test
        void fewerThanTwoPoints() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.nearestNeihborHeuristic(startingPoint, new ArrayList<>() {{add(MapsUtilsTest.points.get(0));}});
            });
        }
        
        @Test
        void startingPointInPointsList() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.nearestNeihborHeuristic(startingPoint, new ArrayList<>() {{add(startingPoint); add(MapsUtilsTest.points.get(0));}});
            });
        }

        @Test
        void startingPointNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.nearestNeihborHeuristic(null, MapsUtilsTest.points);
            });
        }
    }

    @Nested
    class BruteForceTests {
        @Test
        void basic() {
            List<JOpenCageLatLng> pointsToVisit = new ArrayList<>() {{
                add(MapsUtilsTest.points.get(0)); // Toulouse
                add(MapsUtilsTest.points.get(2)); // Nîmes
                add(MapsUtilsTest.points.get(1)); // Montpellier
            }};

            List<JOpenCageLatLng> optimalPath = TravelerAlgorithm.bruteForce(startingPoint, pointsToVisit);

            Assertions.assertEquals(MapsUtilsTest.points.get(0), optimalPath.get(0)); // Toulouse
            Assertions.assertEquals(MapsUtilsTest.points.get(1), optimalPath.get(1)); // Montpellier
            Assertions.assertEquals(MapsUtilsTest.points.get(2), optimalPath.get(2)); // Nîmes
        }

        @Test
        void complete() {
            List<JOpenCageLatLng> optimalPath = TravelerAlgorithm.bruteForce(startingPoint, MapsUtilsTest.points);

            Assertions.assertEquals(MapsUtilsTest.points.get(2), optimalPath.get(0)); // Nimes
            Assertions.assertEquals(MapsUtilsTest.points.get(1), optimalPath.get(1)); // Montpellier
            Assertions.assertEquals(MapsUtilsTest.points.get(5), optimalPath.get(2)); // Bézier
            Assertions.assertEquals(MapsUtilsTest.points.get(3), optimalPath.get(3)); // Perpignan
            Assertions.assertEquals(MapsUtilsTest.points.get(4), optimalPath.get(4)); // Carcasonne
            Assertions.assertEquals(MapsUtilsTest.points.get(7), optimalPath.get(5)); // Tarbes
            Assertions.assertEquals(MapsUtilsTest.points.get(0), optimalPath.get(6)); // Toulouse
            Assertions.assertEquals(MapsUtilsTest.points.get(6), optimalPath.get(7)); // Albi
        }

        @Test
        void nullPoints() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.bruteForce(startingPoint, null);
            });
        }

        @Test
        void fewerThanTwoPoints() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.bruteForce(startingPoint, new ArrayList<>() {{
                    add(MapsUtilsTest.points.get(0));
                }});
            });
        }

        @Test
        void startingPointInPointsList() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.bruteForce(startingPoint, new ArrayList<>() {{
                    add(startingPoint);
                    add(MapsUtilsTest.points.get(1));
                }});
            });
        }

        @Test
        void startingPointNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.bruteForce(null, MapsUtilsTest.points);
            });
        }
    }
}
