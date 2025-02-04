package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides static methods to generate datasets of geographical points (latitude and longitude).
 * The datasets are used for benchmarking algorithms that work with geographical data.
 */
public class TravelerAlgorithmCrazyBenchmarkData {

    // IUT de Rodez
    public static final JOpenCageLatLng startingPoint = new JOpenCageLatLng() {{
        setLat(44.360052);
        setLng(2.575674);
    }};

    /**
     * Creates a dataset of 10 fixed geographical points representing major cities in France.
     * @return a list of {@link JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset10() {
        List<JOpenCageLatLng> dataset = new ArrayList<>();

        // Major French cities
        dataset.add(new JOpenCageLatLng() {{ setLat(48.8566); setLng(2.3522); }}); // Paris
        dataset.add(new JOpenCageLatLng() {{ setLat(45.7640); setLng(4.8357); }}); // Lyon
        dataset.add(new JOpenCageLatLng() {{ setLat(43.7102); setLng(7.2620); }}); // Nice
        dataset.add(new JOpenCageLatLng() {{ setLat(43.2951); setLng(-0.370797); }}); // Pau
        dataset.add(new JOpenCageLatLng() {{ setLat(44.9778); setLng(4.8722); }}); // Toulouse
        dataset.add(new JOpenCageLatLng() {{ setLat(43.6150); setLng(3.8777); }}); // Montpellier
        dataset.add(new JOpenCageLatLng() {{ setLat(48.5734); setLng(7.7521); }}); // Strasbourg
        dataset.add(new JOpenCageLatLng() {{ setLat(43.6047); setLng(1.4442); }}); // Toulouse
        dataset.add(new JOpenCageLatLng() {{ setLat(45.1575); setLng(5.7133); }}); // Grenoble
        dataset.add(new JOpenCageLatLng() {{ setLat(47.2186); setLng(-1.5549); }}); // Nantes

        return dataset;
    }

    /**
     * Creates a dataset of 11 fixed geographical points representing major cities in France.
     * @return a list of {@link JOpenCageLatLng} representing the dataset
     */
    public static List<JOpenCageLatLng> createDataset11() {
        List<JOpenCageLatLng> dataset = createDataset10();
        dataset.add(new JOpenCageLatLng() {{ setLat(42.6977); setLng(2.8956); }}); // Perpignan

        return dataset;
    }
}
