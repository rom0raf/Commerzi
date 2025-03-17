package com.commerzi.commerziapi.address;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.maps.coordinates.CoordinatesTransformer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageResponse;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for validating and fetching coordinates for a given address and city using the OpenCage Geocoder API.
 * This class provides caching functionality to store results for faster future lookups.
 */
public class CheckAddress {

    /**
     * API key for OpenCage Geocoder.
     */
    private static String apiKey;

    /**
     * Maximum number of entries allowed in the cache.
     */
    private final static int MAX_CACHE_SIZE = 1000;

    /**
     * A record that holds the full address (address and city) for caching purposes.
     */
    private record FullAddress(String address, String city) {}

    /**
     * A cache for storing coordinates corresponding to a full address (address + city).
     * The cache is limited to {@link #MAX_CACHE_SIZE} entries and expires after 1 day of inactivity.
     */
    private static final LoadingCache<FullAddress, Coordinates> ADDRESS_CACHE = CacheBuilder.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build(new CacheLoader<FullAddress, Coordinates>() {
                @Override
                public Coordinates load(FullAddress key) throws Exception {
                    // Initialize the geocoder and perform the forward geocoding request
                    JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
                    JOpenCageForwardRequest request = new JOpenCageForwardRequest(key.address + ", " + key.city);
                    request.setRestrictToCountryCode("fr");  // Restrict results to France

                    // Perform the geocoding request and convert the response into coordinates
                    JOpenCageResponse response = jOpenCageGeocoder.forward(request);
                    return CoordinatesTransformer.from(response.getFirstPosition());
                }
            });

    /**
     * Initializes the class by loading the OpenCage API key from the application properties file.
     */
    public static void init() {
        // Read the API key from the application.properties file
        Properties properties = new Properties();
        try {
            properties.load(CheckAddress.class.getClassLoader().getResourceAsStream("application.properties"));
            apiKey = properties.getProperty("opencage.api.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given address and city are invalid. An address is considered invalid if it is null,
     * empty, or if no coordinates can be found for it.
     *
     * @param address The address to check.
     * @param city The city to check.
     * @return true if the address or city is invalid, false otherwise.
     */
    public static boolean isAddressInvalid(String address, String city) {
        if (address == null || address.isEmpty() || city == null || city.isEmpty()) {
            return true;
        }

        try {
            // Check if the coordinates for the address are null
            return getCoordinates(address, city) == null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the coordinates corresponding to the given address and city.
     * If the coordinates are not available in the cache, it makes a request to the OpenCage API.
     *
     * @param address The address for which to fetch coordinates.
     * @param city The city for which to fetch coordinates.
     * @return The coordinates corresponding to the address and city, or null if not found.
     */
    public static Coordinates getCoordinates(String address, String city) {
        if (address == null || address.isEmpty()) {
            return null;
        }

        if (city == null || city.isEmpty()) {
            return null;
        }

        try {
            // Fetch coordinates from the cache or from the OpenCage API if not cached
            return ADDRESS_CACHE.get(getCacheKey(address, city));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a cache key from the address and city.
     *
     * @param address The address.
     * @param city The city.
     * @return A {@link FullAddress} object to be used as the cache key.
     */
    private static FullAddress getCacheKey(String address, String city) {
        return new FullAddress(address, city);
    }
}
