package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.IdentityFunction;
import org.sodeja.functional.Predicate1;
import org.sodeja.functional.Predicate2;


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
    
    public static <T, R> boolean equals(List<T> first, List<R> second, Predicate2<T, R> functor) {
    	if(first.size() != second.size()) {
    		return false;
    	}
    	
    	for(int i = 0, n = first.size();i < n;i++) {
    		if(! functor.execute(first.get(i), second.get(i))) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    public static <T> void execute(List<T> list, Function1<Void, T> functor) {
    	CollectionUtils.execute(list, functor);
    }
}
