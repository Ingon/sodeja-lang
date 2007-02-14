package org.sodeja.collections;

import java.util.Collection;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Predicate1;


public final class CollectionUtils {
    private CollectionUtils() {
    }
    
    public static <T, R> Collection<R> map(Collection<T> source, Collection<R> result, Function1<R, T> functor) {
        for (T t : source) {
            result.add(functor.execute(t));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T, R> R[] mapToArray(Collection<T> source, Function1<R, T> functor) {
        return mapToArray(source, (R[]) ArrayUtils.createArray(functor, source.size()), functor);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R[] mapToArray(Collection<T> source, R[] result, Function1<R, T> functor) {
        int i = 0;
        for (T t : source) {
            R executeResult = functor.execute(t);
            if(result == null && executeResult != null) {
            	result = (R[]) ArrayUtils.createArray(executeResult, source.size());
            }
            if(result != null) {
            	result[i++] = executeResult;
            }
        }        
        return result;
    }

    public static <T> Collection<T> filter(Collection<T> source, Collection<T> result, Predicate1<T> functor) {
        for (T t : source) {
            if (functor.execute(t)) {
                result.add(t);
            }
        }
        return result;
    }
        
    public static <T> T find(Collection<T> collection, Predicate1<T> functor) {
        for (T t : collection) {
            if (functor.execute(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> boolean elem(Collection<T> collection, Predicate1<T> functor) {
        for (T t : collection) {
            if (functor.execute(t)) {
                return true;
            }
        }
        return false;
    }
    
    public static <T> void execute(Collection<T> collection, Function1<Void, T> functor) {
    	for(T t : collection) {
    		functor.execute(t);
    	}
    }
}
