package org.sodeja.collections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.sodeja.functional.Function1;

public final class SetUtils {
    private SetUtils() {
    }

    public static <T, R> Set<R> map(Set<T> set, Function1<R, T> functor) {
        return (Set<R>) CollectionUtils.map(set, new HashSet<R>(), functor);
    }
    
    public static <T> Set<T> asSet(T... array) {
        Set<T> result = new HashSet<T>();
        Collections.addAll(result, array);
        return result;
    }
}
