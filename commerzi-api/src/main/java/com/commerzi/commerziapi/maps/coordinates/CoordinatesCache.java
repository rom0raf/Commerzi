package com.commerzi.commerziapi.maps.coordinates;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class CoordinatesCache<V> {

    private final Map<String, V> cache;
    private final int maxSize;

    public CoordinatesCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, V>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
                // Remove the eldest entry when the size exceeds maxSize
                return size() > maxSize;
            }
        });
    }

    public V get(String key) {
        return cache.get(key);
    }

    public void put(String key, V value) {
        cache.put(key, value);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    /**
     * Computes the value for the given key if it's absent in the cache.
     *
     * @param key the cache key
     * @param mappingFunction a function to compute the value if absent
     * @return the existing or computed value
     */
    public V computeIfAbsent(String key, Function<String, V> mappingFunction) {
        return cache.computeIfAbsent(key, mappingFunction);
    }

    /**
     * Generates a unique cache key for a pair of geographic points (latitudes and longitudes).
     * The key ensures that the distance calculation between two points is cached regardless of the order
     * in which the points are passed (i.e., the cache key will be the same for both directions, p1 -> p2 and p2 -> p1).
     *
     * @param p1 the first geographic point (latitude and longitude)
     * @param p2 the second geographic point (latitude and longitude)
     * @return a unique string that represents the cache key for the given pair of points
     */
    public static String generateBothWaysCacheKey(Coordinates p1, Coordinates p2) {
        // To ensure uniqueness, we use both directions (p1 -> p2 and p2 -> p1)
        if (p1.latitude() < p2.latitude() || (p1.latitude() == p2.latitude() && p1.longitude() < p2.longitude())) {
            return p1.latitude() + "," + p1.longitude() + "-" + p2.latitude() + "," + p2.longitude();
        } else {
            return p2.latitude() + "," + p2.longitude() + "-" + p1.latitude() + "," + p1.longitude();
        }
    }

    /**
     * Generates a unique cache key for a pair of geographic points (latitudes and longitudes).
     * The key is generated in one direction, i.e., for p1 -> p2 and not p2 -> p1.
     *
     * @param p1 the first geographic point (latitude and longitude)
     * @param p2 the second geographic point (latitude and longitude)
     * @return a unique string that represents the cache key for the given pair of points
     */
    public static String generateCacheKey(Coordinates p1, Coordinates p2) {
        // Simply return the key in the order of p1 -> p2 without checking both directions
        return p1.latitude() + "," + p1.longitude() + "-" + p2.latitude() + "," + p2.longitude();
    }
}
