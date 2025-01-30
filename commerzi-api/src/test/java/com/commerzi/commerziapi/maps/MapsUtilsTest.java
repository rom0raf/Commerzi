package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

class MapsUtilsTest {

    public static ArrayList<JOpenCageLatLng> points = new ArrayList<>() {{
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

            double actualDistance = MapsUtils.flyingDistanceBetweenTwoPoints(pointsPair[0], pointsPair[1]);

            assertEquals(expectedDistance, actualDistance, 2.5);
        }
    }

}
