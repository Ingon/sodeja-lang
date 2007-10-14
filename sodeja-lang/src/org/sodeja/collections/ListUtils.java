package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Function2;
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
    
    public static <T> void execute(List<T> list, Function2<Void, T, Integer> functor) {
    	for(int i = 0;i < list.size();i++) {
    		functor.execute(list.get(i), i);
    	}
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

	public static <T, R> List<R> collectItems(List<T> source, Function1<R, T> closure) {
		List<R> result = new ArrayList<R>();
		CollectionUtils.map(source, result, closure);
		return result;
	}
	
	public static <R, P> R foldr(List<P> source, R initial, Function2<R, P, R> functor) {
		R result = initial;
		for(int i = source.size() - 1;i >= 0;i--) {
			result = functor.execute(source.get(i), result);
		}
		return result;
	}
	
	public static <R, P> R foldl(List<P> source, R initial, Function2<R, R, P> functor) {
		R result = initial;
		for(int i = 0;i < source.size();i++) {
			result = functor.execute(result, source.get(i));
		}
		return result;
	}

	public static <P> boolean any(List<P> source, final Predicate1<P> functor) {
		return foldr(source, false, new Function2<Boolean, P, Boolean>() {
			public Boolean execute(P object, Boolean tempResult) {
				return tempResult | functor.execute(object);
			}});
	}
	
	public static <P> boolean all(List<P> source, final Predicate1<P> functor) {
		return foldr(source, true, new Function2<Boolean, P, Boolean>() {
			public Boolean execute(P object, Boolean tempResult) {
				return tempResult & functor.execute(object);
			}});
	}

	public static <T> int sum(List<T> list, final Function1<Integer, T> closure) {
		return foldr(list, 0, new Function2<Integer, T, Integer>() {
			public Integer execute(T p1, Integer p2) {
				return p2 + closure.execute(p1);
			}});
	}
	
	public static <T> double average(List<T> list, final Function1<Integer, T> closure) {
		int total = sum(list, closure);
		return total/list.size();
	}
	
	public static <T> List<T> flattern(List<List<T>> list) {
		return foldr(list, new ArrayList<T>(), new Function2<List<T>, List<T>, List<T>>() {
			@Override
			public List<T> execute(List<T> p1, List<T> p2) {
				p1.addAll(p2);
				return p1;
			}});
	}
}
