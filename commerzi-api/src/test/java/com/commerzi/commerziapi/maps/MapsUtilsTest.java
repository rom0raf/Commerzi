package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

class MapsUtilsTest {

    private static ArrayList<JOpenCageLatLng> points = new ArrayList<>() {{
        // Toulouse
        add(new JOpenCageLatLng() {{
            setLat(43.6047);
            setLng(1.4442);
        }});

        // Montpellier
        add(new JOpenCageLatLng() {{
            setLat(43.6117);
            setLng(3.8767);
        }});

        // Nîmes
        add(new JOpenCageLatLng() {{
            setLat(43.8367);
            setLng(4.3601);
        }});

        // Perpignan
        add(new JOpenCageLatLng() {{
            setLat(42.6977);
            setLng(2.8956);
        }});

        // Carcassonne
        add(new JOpenCageLatLng() {{
            setLat(43.2119);
            setLng(2.3500);
        }});

        // Béziers
        add(new JOpenCageLatLng() {{
            setLat(43.3440);
            setLng(3.2192);
        }});

        // Albi
        add(new JOpenCageLatLng() {{
            setLat(43.9263);
            setLng(2.1500);
        }});

        // Tarbes
        add(new JOpenCageLatLng() {{
            setLat(43.2361);
            setLng(0.0822);
        }});
    }};

    public static HashMap<JOpenCageLatLng[], Double> referenceDistances = new HashMap<>() {{
        put(new JOpenCageLatLng[]{points.get(0), points.get(1)}, 195.0); // Toulouse - Montpellier
        put(new JOpenCageLatLng[]{points.get(1), points.get(2)}, 46.0); // Montpellier - Nîmes
        put(new JOpenCageLatLng[]{points.get(3), points.get(4)}, 71.0); // Perpignan - Carcassonne
        put(new JOpenCageLatLng[]{points.get(5), points.get(6)}, 107.); // Béziers - Albi
        put(new JOpenCageLatLng[]{points.get(6), points.get(7)}, 183.0); // Albi - Tarbes
    }};

    @Test
    void distanceBetweenTwoPoints() {
        for (HashMap.Entry<JOpenCageLatLng[], Double> entry : referenceDistances.entrySet()) {
            JOpenCageLatLng[] pointsPair = entry.getKey();
            double expectedDistance = entry.getValue();

            double actualDistance = MapsUtils.distanceBetweenTwoPoints(pointsPair[0], pointsPair[1]);

            assertEquals(expectedDistance, actualDistance, 2.5);
        }
    }

    @Test
    void testSortPointsByShortestPath_basic() {
        JOpenCageLatLng[] pointsToSort = new JOpenCageLatLng[]{
            points.get(0), // Toulouse
            points.get(2), // Nîmes
            points.get(1)  // Montpellier
        };

        JOpenCageLatLng[] sortedPoints = MapsUtils.sortPointsByShortestPath(pointsToSort);

        assertEquals(points.get(0), sortedPoints[0]); // Toulouse
        assertEquals(points.get(1), sortedPoints[1]); // Montpellier
        assertEquals(points.get(2), sortedPoints[2]); // Nîmes
    }

    @Test
    void testSortPointsByShortestPath_complete() {
        JOpenCageLatLng[] sortedPoints = MapsUtils.sortPointsByShortestPath(points.toArray(new JOpenCageLatLng[0]));

        assertEquals(points.get(0), sortedPoints[0]); // Toulouse
        assertEquals(points.get(6), sortedPoints[1]); // Albi
        assertEquals(points.get(4), sortedPoints[2]); // Carcasonne
        assertEquals(points.get(5), sortedPoints[3]); // Bézier
        assertEquals(points.get(1), sortedPoints[4]); // Montpellier
        assertEquals(points.get(2), sortedPoints[5]); // Nimes
        assertEquals(points.get(3), sortedPoints[6]); // Perpignan
        assertEquals(points.get(7), sortedPoints[7]); // Tarbes
    }

    @Test
    void testSortPointsByShortestPath_nullPoints() {
        assertThrows(IllegalArgumentException.class, () -> {
            MapsUtils.sortPointsByShortestPath(null);
        });
    }

    @Test
    void testSortPointsByShortestPath_fewerThanTwoPoints() {
        assertThrows(IllegalArgumentException.class, () -> {
            MapsUtils.sortPointsByShortestPath(new JOpenCageLatLng[]{points.get(0)});
        });
    }
}
