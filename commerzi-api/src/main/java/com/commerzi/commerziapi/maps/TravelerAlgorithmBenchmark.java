package com.commerzi.commerziapi.maps;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class benchmarks the TravelerAlgorithm class's method for sorting a list of JOpenCageLatLng
 * points using the nearest-neighbor heuristic. It uses JMH to perform accurate microbenchmarks.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class TravelerAlgorithmBenchmark {

    /**
     * This class defines the state for benchmarking purposes.
     * It holds a list of geographical points and provides methods to calculate benchmark-related metrics.
     *
     * The state is used in the context of JMH benchmarks with the {@link org.openjdk.jmh.annotations.State State} annotation and is scoped to a single thread.
     * It also uses the {@link org.openjdk.jmh.annotations.AuxCounters AuxCounters} annotation to track custom event counters.
     */
    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.EVENTS)
    public static class BenchmarkState {
        private List<JOpenCageLatLng> sortedPoints;

        /**
         * Calculates the total distance between the points in the sorted list.
         * The distance is computed by calling {@link MapsUtils#fullFlyingDistanceOverPoints(List)}
         * and dividing the result by 3 (likely for normalization or scaling purposes).
         *
         * @return the total distance between the points in the list, divided by 3
         */
        public Double totalDistance() {
            return MapsUtils.fullFlyingDistanceOverPoints(sortedPoints) / 3;
        }
    }

    // The current list of points to pass to the benchmarked function
    private List<JOpenCageLatLng> points;

    // Datasets
    private List<List<JOpenCageLatLng>> datasets;

    // Initialize the datasets
    @Setup(Level.Trial)
    public void setup() {
        datasets = new ArrayList<>();

        // Dataset 1: 2 point
        datasets.add(TravelerAlgorithmBenchmarkData.createDataset1());

        // Dataset 2: 3 points
        datasets.add(TravelerAlgorithmBenchmarkData.createDataset2());

        // Dataset 3: 4 points
        datasets.add(TravelerAlgorithmBenchmarkData.createDataset3());

        // Dataset 4: 6 points
        datasets.add(TravelerAlgorithmBenchmarkData.createDataset4());

        // Dataset 5: 8 points
        datasets.add(TravelerAlgorithmBenchmarkData.createDataset5());
    }

    @Param({"0", "1", "2", "3", "4"})
    public int datasetIndex;

    @Setup(Level.Invocation)
    public void prepareData() {points = datasets.get(datasetIndex);}

    /**
     * Benchmark for the nearestNeihborHeuristic method.
     * This method measures the time it takes to sort the points and returns the optimized list.
     *
     * @param state the current benchmark state to count some other data
     * @return the optimized list of points sorted using the nearest-neighbor heuristic
     */
    @Benchmark
    public List<JOpenCageLatLng> benchmarkNearestNeihborHeuristic(BenchmarkState state) {
        state.sortedPoints = TravelerAlgorithm.nearestNeihborHeuristic(TravelerAlgorithmBenchmarkData.startingPoint, points);
        return state.sortedPoints;
    }

    /**
     * Benchmark for the bruteForce method.
     * This method measures the time it takes to sort the point and returns the optimized list.
     *
     * @param state the current benchmark state to count some other data
     * @return the optimized list of points sorted using the brute force method
     */
    @Benchmark
    public List<JOpenCageLatLng> benchmarkBruteForce(BenchmarkState state) {
        state.sortedPoints = TravelerAlgorithm.bruteForce(TravelerAlgorithmBenchmarkData.startingPoint, points);
        return state.sortedPoints;
    }

    /**
     * Main method to run the benchmark using JMH.
     * It requires running through JMH harness to get proper benchmarking results.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(TravelerAlgorithmBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .warmupTime(TimeValue.seconds(3))
                .measurementIterations(3)
                .measurementTime(TimeValue.seconds(3))
                .resultFormat(ResultFormatType.JSON)
                .result(benchmarkFileOutput())
                .build();

        new Runner(options).run();
    }

    /**
     * Generates a unique filename for the benchmark output, incorporating the current date and time.
     * The filename format will be: "benchmark-yyyy-MM-dd_HH-mm-ss.json".
     * This ensures each benchmark run produces a distinct output file.
     *
     * @return the generated filename as a String
     */
    private static String benchmarkFileOutput() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateString = dateFormat.format(new Date());
        return "benchmark-results/benchmark-" + dateString + ".json";
    }
}