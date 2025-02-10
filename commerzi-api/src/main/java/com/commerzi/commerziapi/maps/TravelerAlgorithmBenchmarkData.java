package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides static methods to generate datasets of geographical points (latitude and longitude).
 * Each dataset consists of a list of {@link com.opencagedata.jopencage.model.JOpenCageLatLng JOpenCageLatLng} objects representing various cities in the south of France.
 * The datasets are used for benchmarking algorithms that work with geographical data.
 */
public class TravelerAlgorithmBenchmarkData {

    // IUT de Rodez
    public static final JOpenCageLatLng startingPoint = new JOpenCageLatLng() {{
        setLat(44.360052);
        setLng(2.575674);
    }};

    /**
     * Creates a dataset of 2 geographical points: Toulouse and Montpellier.
     *
     * @return a list of {@link com.opencagedata.jopencage.model.JOpenCageLatLng JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset1() {
        List<JOpenCageLatLng> dataset = new ArrayList<>();
        JOpenCageLatLng point1 = new JOpenCageLatLng();
        point1.setLat(43.6047);  // Toulouse
        point1.setLng(1.4442);
        dataset.add(point1);

        JOpenCageLatLng point2 = new JOpenCageLatLng();
        point2.setLat(43.6117);  // Montpellier
        point2.setLng(3.8767);
        dataset.add(point2);

        return dataset;
    }

    /**
     * Creates a dataset of 3 geographical points: Toulouse, Montpellier, and Nîmes.
     *
     * @return a list of {@link com.opencagedata.jopencage.model.JOpenCageLatLng JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset2() {
        List<JOpenCageLatLng> dataset = new ArrayList<>();
        JOpenCageLatLng point1 = new JOpenCageLatLng();
        point1.setLat(43.6047);  // Toulouse
        point1.setLng(1.4442);
        dataset.add(point1);

        JOpenCageLatLng point2 = new JOpenCageLatLng();
        point2.setLat(43.6117);  // Montpellier
        point2.setLng(3.8767);
        dataset.add(point2);

        JOpenCageLatLng point3 = new JOpenCageLatLng();
        point3.setLat(43.8367);  // Nîmes
        point3.setLng(4.3601);
        dataset.add(point3);

        return dataset;
    }

    /**
     * Creates a dataset of 4 geographical points: Toulouse, Montpellier, Nîmes, and Perpignan.
     *
     * @return a list of {@link com.opencagedata.jopencage.model.JOpenCageLatLng JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset3() {
        List<JOpenCageLatLng> dataset = new ArrayList<>();
        JOpenCageLatLng point1 = new JOpenCageLatLng();
        point1.setLat(43.6047);  // Toulouse
        point1.setLng(1.4442);
        dataset.add(point1);

        JOpenCageLatLng point2 = new JOpenCageLatLng();
        point2.setLat(43.6117);  // Montpellier
        point2.setLng(3.8767);
        dataset.add(point2);

        JOpenCageLatLng point3 = new JOpenCageLatLng();
        point3.setLat(43.8367);  // Nîmes
        point3.setLng(4.3601);
        dataset.add(point3);

        JOpenCageLatLng point4 = new JOpenCageLatLng();
        point4.setLat(42.6977);  // Perpignan
        point4.setLng(2.8956);
        dataset.add(point4);

        return dataset;
    }

     /**
     * Creates a dataset of 6 geographical points: Toulouse, Montpellier, Nîmes, Perpignan, Carcassonne, and Béziers.
     *
     * @return a list of {@link com.opencagedata.jopencage.model.JOpenCageLatLng JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset4() {
        List<JOpenCageLatLng> dataset = new ArrayList<>();
        JOpenCageLatLng point1 = new JOpenCageLatLng();
        point1.setLat(43.6047);  // Toulouse
        point1.setLng(1.4442);
        dataset.add(point1);

        JOpenCageLatLng point2 = new JOpenCageLatLng();
        point2.setLat(43.6117);  // Montpellier
        point2.setLng(3.8767);
        dataset.add(point2);

        JOpenCageLatLng point3 = new JOpenCageLatLng();
        point3.setLat(43.8367);  // Nîmes
        point3.setLng(4.3601);
        dataset.add(point3);

        JOpenCageLatLng point4 = new JOpenCageLatLng();
        point4.setLat(42.6977);  // Perpignan
        point4.setLng(2.8956);
        dataset.add(point4);

        JOpenCageLatLng point5 = new JOpenCageLatLng();
        point5.setLat(43.2119);  // Carcassonne
        point5.setLng(2.3500);
        dataset.add(point5);

        JOpenCageLatLng point6 = new JOpenCageLatLng();
        point6.setLat(43.3440);  // Béziers
        point6.setLng(3.2192);
        dataset.add(point6);

        return dataset;
    }

    /**
     * Creates a dataset of 8 geographical points: Toulouse, Montpellier, Nîmes, Perpignan, Carcassonne, Béziers, Albi, and Tarbes.
     *
     * @return a list of {@link com.opencagedata.jopencage.model.JOpenCageLatLng JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset5() {
        List<JOpenCageLatLng> dataset = new ArrayList<>();
        JOpenCageLatLng point1 = new JOpenCageLatLng();
        point1.setLat(43.6047);  // Toulouse
        point1.setLng(1.4442);
        dataset.add(point1);

        JOpenCageLatLng point2 = new JOpenCageLatLng();
        point2.setLat(43.6117);  // Montpellier
        point2.setLng(3.8767);
        dataset.add(point2);

        JOpenCageLatLng point3 = new JOpenCageLatLng();
        point3.setLat(43.8367);  // Nîmes
        point3.setLng(4.3601);
        dataset.add(point3);

        JOpenCageLatLng point4 = new JOpenCageLatLng();
        point4.setLat(42.6977);  // Perpignan
        point4.setLng(2.8956);
        dataset.add(point4);

        JOpenCageLatLng point5 = new JOpenCageLatLng();
        point5.setLat(43.2119);  // Carcassonne
        point5.setLng(2.3500);
        dataset.add(point5);

        JOpenCageLatLng point6 = new JOpenCageLatLng();
        point6.setLat(43.3440);  // Béziers
        point6.setLng(3.2192);
        dataset.add(point6);

        JOpenCageLatLng point7 = new JOpenCageLatLng();
        point7.setLat(43.9263);  // Albi
        point7.setLng(2.1500);
        dataset.add(point7);

        JOpenCageLatLng point8 = new JOpenCageLatLng();
        point8.setLat(43.2361);  // Tarbes
        point8.setLng(0.0822);
        dataset.add(point8);

        return dataset;
    }
}
