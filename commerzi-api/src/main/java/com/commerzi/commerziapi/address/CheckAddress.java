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

    private static JOpenCageLatLng callAPI(String address) {
        // call the OpenCage API
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder(apiKey);
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);
        request.setRestrictToCountryCode("fr");

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        return response.getFirstPosition();
    }

    public static boolean checkAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        JOpenCageLatLng coordinates = callAPI(address);
        return coordinates != null;
    }

    public static JOpenCageLatLng getCoordinates(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }

        return callAPI(address);
    }


}
