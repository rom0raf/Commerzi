package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides static methods to generate datasets of geographical points (latitude and longitude).
 * Each dataset consists of a list of {@link com.commerzi.commerziapi.maps.coordinates.Coordinates Coordinates} objects representing various cities in the south of France.
 * The datasets are used for benchmarking algorithms that work with geographical data.
 */
public class TravelerAlgorithmBenchmarkData {

    // IUT de Rodez
    public static final Coordinates startingPoint = new Coordinates(44.360052, 2.575674);

    /**
     * Creates a dataset of 2 geographical points: Toulouse and Montpellier.
     *
     * @return a list of {@link com.commerzi.commerziapi.maps.coordinates.Coordinates Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset1() {
        List<Coordinates> dataset = new ArrayList<>();

        Coordinates point1 = new Coordinates(43.6047, 1.4442);  // Toulouse
        dataset.add(point1);

        Coordinates point2 = new Coordinates(43.6117, 3.8767);  // Montpellier
        dataset.add(point2);

        return dataset;
    }

    /**
     * Creates a dataset of 3 geographical points: Toulouse, Montpellier, and Nîmes.
     *
     * @return a list of {@link com.commerzi.commerziapi.maps.coordinates.Coordinates Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset2() {
        List<Coordinates> dataset = createDataset1();

        Coordinates point3 = new Coordinates(43.8367, 4.3601);  // Nîmes
        dataset.add(point3);

        return dataset;
    }

    /**
     * Creates a dataset of 4 geographical points: Toulouse, Montpellier, Nîmes, and Perpignan.
     *
     * @return a list of {@link com.commerzi.commerziapi.maps.coordinates.Coordinates Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset3() {
        List<Coordinates> dataset = createDataset2();

        Coordinates point4 = new Coordinates(42.6977, 2.8956);  // Perpignan
        dataset.add(point4);

        return dataset;
    }

     /**
     * Creates a dataset of 6 geographical points: Toulouse, Montpellier, Nîmes, Perpignan, Carcassonne, and Béziers.
     *
     * @return a list of {@link com.commerzi.commerziapi.maps.coordinates.Coordinates Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset4() {
        List<Coordinates> dataset = createDataset3();

        Coordinates point5 = new Coordinates(43.2119, 2.3500);  // Carcassonne
        dataset.add(point5);

        Coordinates point6 = new Coordinates(43.3440, 3.2192);  // Béziers
        dataset.add(point6);

        return dataset;
    }

    /**
     * Creates a dataset of 8 geographical points: Toulouse, Montpellier, Nîmes, Perpignan, Carcassonne, Béziers, Albi, and Tarbes.
     *
     * @return a list of {@link com.commerzi.commerziapi.maps.coordinates.Coordinates Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset5() {
        List<Coordinates> dataset = createDataset4();

        Coordinates point7 = new Coordinates(43.9263, 2.1500);  // Albi
        dataset.add(point7);

        Coordinates point8 = new Coordinates(43.2361, 0.0822);  // Tarbes
        dataset.add(point8);

        return dataset;
    }
}
