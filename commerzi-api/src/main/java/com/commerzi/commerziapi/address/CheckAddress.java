package com.commerzi.commerziapi.address;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;

import java.util.Properties;

public class CheckAddress {

    private static String apiKey;

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

    private static JOpenCageLatLng callAPI(String address, String city) {
        // call the OpenCage API
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address + ", " + city);
        request.setRestrictToCountryCode("fr");

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        return response.getFirstPosition();
    }

    public static boolean isAddressInvalid(String address, String city) {
        if (address == null || address.isEmpty() || city == null || city.isEmpty()) {
            return true;
        }


        JOpenCageLatLng coordinates = callAPI(address, city);
        return coordinates == null;
    }

    public static JOpenCageLatLng getCoordinates(String address, String city) {
        if (address == null || address.isEmpty()) {
            return null;
        }

        return callAPI(address, city);
    }


}
