package com.commerzi.commerziapi.maps.algorithms;

import com.opencagedata.jopencage.model.JOpenCageLatLng;

import java.util.ArrayList;
import java.util.List;

public class BruteForceUtils {

    /**
     * Generates all permutations of a list of points.
     *
     * @param list the list of points
     * @return a list of all permutations of the input list
     */
    public static List<List<JOpenCageLatLng>> generatePermutations(List<JOpenCageLatLng> list) {
        List<List<JOpenCageLatLng>> permutations = new ArrayList<>();
        if (list.size() == 1) {
            permutations.add(new ArrayList<>(list));
        } else {
            for (int i = 0; i < list.size(); i++) {
                JOpenCageLatLng current = list.get(i);
                List<JOpenCageLatLng> remaining = new ArrayList<>(list);
                remaining.remove(i);

                List<List<JOpenCageLatLng>> remainingPermutations = generatePermutations(remaining);
                for (List<JOpenCageLatLng> perm : remainingPermutations) {
                    perm.add(0, current);
                    permutations.add(perm);
                }
            }
        }
        return permutations;
    }

}
