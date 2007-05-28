package org.sodeja.collections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class SetUtils {
    private SetUtils() {
    }

    public static <T> Set<T> fromArray(T... array) {
        Set<T> result = new HashSet<T>();
        Collections.addAll(result, array);
        return result;
    }
}
