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
    class SortPointsByShortestPathTests {
        @Test
        void basic() {
            List<JOpenCageLatLng> pointsToSort = new ArrayList<>() {{
                add(MapsUtilsTest.points.get(0)); // Toulouse
                add(MapsUtilsTest.points.get(2)); // Nîmes
                add(MapsUtilsTest.points.get(1)); // Montpellier
            }};

            List<JOpenCageLatLng> sortedPoints = TravelerAlgorithm.sortPointsByShortestPath(startingPoint, pointsToSort);

            Assertions.assertEquals(MapsUtilsTest.points.get(0), sortedPoints.get(0)); // Toulouse
            Assertions.assertEquals(MapsUtilsTest.points.get(1), sortedPoints.get(1)); // Montpellier
            Assertions.assertEquals(MapsUtilsTest.points.get(2), sortedPoints.get(2)); // Nîmes
        }

        @Test
        void complete() {
            List<JOpenCageLatLng> sortedPoints = TravelerAlgorithm.sortPointsByShortestPath(startingPoint, MapsUtilsTest.points);

            Assertions.assertEquals(MapsUtilsTest.points.get(0), sortedPoints.get(0)); // Toulouse
            Assertions.assertEquals(MapsUtilsTest.points.get(6), sortedPoints.get(1)); // Albi
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
                TravelerAlgorithm.sortPointsByShortestPath(startingPoint, null);
            });
        }

        @Test
        void fewerThanTwoPoints() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.sortPointsByShortestPath(startingPoint, new ArrayList<>() {{add(MapsUtilsTest.points.get(0));}});
            });
        }
        
        @Test
        void startingPointInPointsList() {
            assertThrows(IllegalArgumentException.class, () -> {
                TravelerAlgorithm.sortPointsByShortestPath(startingPoint, new ArrayList<>() {{add(startingPoint);}});
            });
        }
    }
}
