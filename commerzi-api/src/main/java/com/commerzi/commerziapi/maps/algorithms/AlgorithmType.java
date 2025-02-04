package com.commerzi.commerziapi.maps.algorithms;

/**
 * Enum representing the different types of algorithms that can be used to solve the Traveling Salesman Problem.
 * This enum is used to specify which algorithm should be instantiated when using the {@link ATravelerAlgorithm#getAlgorithmWithFlyingDistances(AlgorithmType)} method.
 */
public enum AlgorithmType {

    /**
     * {@link BruteForce}
     */
    BRUTE_FORCE,

    /**
     * {@link BruteForceOptimized}
     */
    BRUTE_FORCE_OPTIMIZED,

    /**
     * {@link NearestNeighborHeuristic}
     */
    NEAREST_NEIGHBOR_HEURISTIC
}
