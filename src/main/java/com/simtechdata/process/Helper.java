package com.simtechdata.process;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class Helper {

    @SafeVarargs
    public static <T> Set<T> asSet(T... elements) {
        return new HashSet<>(asList(elements));
    }

    public static Set<Integer> asSet(int[] elements) {
        HashSet<Integer> set = new HashSet<>();
        for (int element : elements) {
            set.add(element);
        }
        return set;
    }
}
