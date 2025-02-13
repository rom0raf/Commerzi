package com.commerzi.commerziapi.maps.coordinates;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

public class CoordinatesTransformer {

    public static Coordinates from(JOpenCageLatLng in) {
        return new Coordinates(in.getLat(), in.getLng());
    }

}
