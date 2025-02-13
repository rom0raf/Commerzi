package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

class MapsUtilsTest {

    public static ArrayList<Coordinates> points = new ArrayList<>() {{
        // Toulouse
        add(new Coordinates(43.6047,1.4442));

        // Montpellier
        add(new Coordinates(43.6117,3.8767));

        // Nîmes
        add(new Coordinates(43.8367,4.3601));

        // Perpignan
        add(new Coordinates(42.6977,2.8956));

        // Carcassonne
        add(new Coordinates(43.2119,2.3500));

        // Béziers
        add(new Coordinates(43.3440,3.2192));

        // Albi
        add(new Coordinates(43.9263,2.1500));

        // Tarbes
        add(new Coordinates(43.2361, 0.0822));
    }};

    public static HashMap<Coordinates[], Double> referenceDistances = new HashMap<>() {{
        put(new Coordinates[]{points.get(0), points.get(1)}, 195.0); // Toulouse - Montpellier
        put(new Coordinates[]{points.get(1), points.get(2)}, 46.0); // Montpellier - Nîmes
        put(new Coordinates[]{points.get(3), points.get(4)}, 71.0); // Perpignan - Carcassonne
        put(new Coordinates[]{points.get(5), points.get(6)}, 107.); // Béziers - Albi
        put(new Coordinates[]{points.get(6), points.get(7)}, 183.0); // Albi - Tarbes
    }};

    @Test
    void distanceBetweenTwoPoints() {
        for (HashMap.Entry<Coordinates[], Double> entry : referenceDistances.entrySet()) {
            Coordinates[] pointsPair = entry.getKey();
            double expectedDistance = entry.getValue();

            double actualDistance = MapsUtils.flyingDistanceBetweenTwoPoints(pointsPair[0], pointsPair[1]);

            assertEquals(expectedDistance, actualDistance, 2.5);
        }
    }

}
