package com.commerzi.commerziapi.maps.algorithms;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class BruteForceUtils {

    /**
     * Generates all permutations of a list of points.
     *
     * @param list the list of points
     * @return a list of all permutations of the input list
     */
    public static List<List<Coordinates>> generatePermutations(List<Coordinates> list) {
        List<List<Coordinates>> permutations = new ArrayList<>();
        if (list.size() == 1) {
            permutations.add(new ArrayList<>(list));
        } else {
            for (int i = 0; i < list.size(); i++) {
                Coordinates current = list.get(i);
                List<Coordinates> remaining = new ArrayList<>(list);
                remaining.remove(i);

                List<List<Coordinates>> remainingPermutations = generatePermutations(remaining);
                for (List<Coordinates> perm : remainingPermutations) {
                    perm.add(0, current);
                    permutations.add(perm);
                }
            }
        }
        return permutations;
    }

}
