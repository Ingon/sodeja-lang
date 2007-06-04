package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    
    public static <T> T first(List<T> list) {
    	if(list.isEmpty()) {
    		return null;
    	}
    	return list.get(0);
    }
    
    public static <T> T last(List<T> list) {
    	if(list.isEmpty()) {
    		return null;
    	}
    	return list.get(list.size() - 1);
    }
    
    public static <T> T head(List<T> list) {
    	if(list instanceof ConsList) {
    		return ((ConsList<T>) list).getHead();
    	}
    	return first(list);
    }
    
    public static <T> List<T> tail(List<T> list) {
    	if(list instanceof ConsList) {
    		return ((ConsList<T>) list).getTail();
    	}
    	return list.subList(1, list.size());
    }
    
    public static <T> T findMinMatch(List<T> list, Predicate1<T> functor, Comparator<T> comparator) {
    	List<T> filtered = filter(list, functor);
    	if(filtered.isEmpty()) {
    		return null;
    	}
    	return Collections.min(filtered, comparator);
    }

    public static <T> T findMaxMatch(List<T> list, Predicate1<T> functor, Comparator<T> comparator) {
    	List<T> filtered = filter(list, functor);
    	if(filtered.isEmpty()) {
    		return null;
    	}
    	return Collections.max(filtered, comparator);
    }
    
    public static <T> int insertSorted(List<T> list, T obj, Comparator<T> comparator) {
    	for(int i = 0, n = list.size();i < n;i++) {
    		if(comparator.compare(obj, list.get(i)) < 0) {
    			list.add(i, obj);
    			return i;
    		}
    	}
    	
    	list.add(obj);
    	return list.size() - 1;
    }

	public static <T, R> List<R> collectItems(List<T> original, Function1<R, T> closure) {
		List<R> items = new ArrayList<R>();
		for(T obj : original) {
			items.add(closure.execute(obj));
		}
		return items;
	}
	
	public static <T> int sum(List<T> list, Function1<Integer, T> closure) {
		int total = 0;
		for(T obj : list) {
			total += closure.execute(obj);
		}
		return total;
	}
}
