package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.IdentityFunction;
import org.sodeja.functional.Predicate1;


public final class ListUtils {
    private ListUtils() {
    }
    
    public static <T, R> List<R> map(List<T> list, Function1<R, T> functor) {
        return (List<R>) CollectionUtils.map(list, new ArrayList<R>(), functor);
    }
    
    public static <T> List<T> filter(List<T> list, Predicate1<T> functor) {
        return (List<T>) CollectionUtils.filter(list, new ArrayList<T>(), functor);
    }
    
    public static <T> T find(List<T> list, Predicate1<T> predicate) {
        return CollectionUtils.find(list, predicate);
    }
    
    public static <T> boolean elem(List<T> list, Predicate1<T> predicate) {
        return CollectionUtils.elem(list, predicate);
    }
    
    public static <T> List<T> asList(T... objects) {
        List<T> result = new ArrayList<T>();
        Collections.addAll(result, objects);
        return result;
    }
    
    public static <T> T[] asArray(List<T> data) {
    	return CollectionUtils.mapToArray(data, new IdentityFunction<T>());
    }
}
