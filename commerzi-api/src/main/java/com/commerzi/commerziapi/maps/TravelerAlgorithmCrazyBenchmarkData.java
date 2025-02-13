package com.commerzi.commerziapi.maps;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides static methods to generate datasets of geographical points (latitude and longitude).
 * The datasets are used for benchmarking algorithms that work with geographical data.
 */
public class TravelerAlgorithmCrazyBenchmarkData {

    // IUT de Rodez
    public static final Coordinates startingPoint = new Coordinates(44.360052, 2.575674);

    /**
     * Creates a dataset of 10 fixed geographical points representing major cities in France.
     * @return a list of {@link Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset10() {
        List<Coordinates> dataset = new ArrayList<>();

        // Major French cities
        dataset.add(new Coordinates(48.8566, 2.3522)); // Paris
        dataset.add(new Coordinates(45.7640, 4.8357)); // Lyon
        dataset.add(new Coordinates(43.7102, 7.2620)); // Nice
        dataset.add(new Coordinates(43.2951, -0.370797)); // Pau
        dataset.add(new Coordinates(44.9778, 4.8722)); // Toulouse
        dataset.add(new Coordinates(43.6150, 3.8777)); // Montpellier
        dataset.add(new Coordinates(48.5734, 7.7521)); // Strasbourg
        dataset.add(new Coordinates(43.6047, 1.4442)); // Toulouse
        dataset.add(new Coordinates(45.1575, 5.7133)); // Grenoble
        dataset.add(new Coordinates(47.2186, -1.5549)); // Nantes

        return dataset;
    }

    /**
     * Creates a dataset of 11 fixed geographical points representing major cities in France.
     * @return a list of {@link Coordinates} representing the dataset
     */
    public static List<Coordinates> createDataset11() {
        List<Coordinates> dataset = createDataset10();

        dataset.add(new Coordinates(42.6977, 2.8956)); // Perpignan

        return dataset;
    }
}
