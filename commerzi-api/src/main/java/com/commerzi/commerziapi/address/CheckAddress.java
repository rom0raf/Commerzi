package com.commerzi.commerziapi.address;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.maps.coordinates.CoordinatesCache;
import com.commerzi.commerziapi.maps.coordinates.CoordinatesTransformer;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageResponse;

import java.util.Properties;

public class CheckAddress {

    private static String apiKey;
    private final static int MAX_CACHE_SIZE = 100;
    private static final CoordinatesCache<Coordinates> cache = new CoordinatesCache(MAX_CACHE_SIZE);

    public static void init() {
        // read from application.properties
        Properties properties = new Properties();
        try {
            properties.load(CheckAddress.class.getClassLoader().getResourceAsStream("application.properties"));
            apiKey = properties.getProperty("opencage.api.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Coordinates callAPI(String address, String city) {
        // call the OpenCage API
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address + ", " + city);
        request.setRestrictToCountryCode("fr");

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        return CoordinatesTransformer.from(response.getFirstPosition());
    }

    public static boolean isAddressInvalid(String address, String city) {
        if (address == null || address.isEmpty() || city == null || city.isEmpty()) {
            return true;
        }
        return callAPI(address, city) == null;
    }

    public static Coordinates getCoordinates(String address, String city) {
        if (address == null || address.isEmpty()) {
            return null;
        }

        if (city == null || city.isEmpty()) {
            return null;
        }

        if (cache.containsKey(getCacheKey(address, city))) {
            return cache.get(getCacheKey(address, city));
        }

        Coordinates coordinates = callAPI(address, city);
        cache.put(getCacheKey(address, city), coordinates);
        return coordinates;
    }

    private static String getCacheKey(String address, String city) {
        return address + "_" + city;
    }


}
