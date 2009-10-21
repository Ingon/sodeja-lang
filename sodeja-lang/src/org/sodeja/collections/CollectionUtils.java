package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Function2;
import org.sodeja.functional.Predicate1;


public final class CollectionUtils {
    private CollectionUtils() {
    }
    
    public static <T, R> Collection<R> map(Collection<T> source, Collection<R> result, Function1<R, T> functor) {
    	if(source == null) {
    		return null;
    	}
        for (T t : source) {
            result.add(functor.execute(t));
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T, R> R[] mapToArray(Collection<T> source, Function1<R, T> functor) {
    	if(source == null) {
    		return null;
    	}
        return mapToArray(source, (R[]) ArrayUtils.createArray(functor, source.size()), functor);
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R[] mapToArray(Collection<T> source, R[] result, Function1<R, T> functor) {
        int i = 0;
        for (T t : source) {
            R executeResult = functor.execute(t);
            if(result == null && executeResult != null) {
            	result = (R[]) ArrayUtils.createArray1(executeResult, source.size());
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
    
    public static <T> void execute(Iterable<T> collection, Function1<Void, T> functor) {
    	if(collection == null) {
    		return;
    	}
    	for(T t : collection) {
    		functor.execute(t);
    	}
    }
    
    public static <T> boolean isEmpty(Collection<T> collection) {
    	return collection == null || collection.isEmpty();
    }

	public static <R, P> R foldl(Iterable<P> source, R initial, Function2<R, R, P> functor) {
		R result = initial;
		for(P item : source) {
			result = functor.execute(result, item);
		}
		return result;
	}

	public static <T> int sumInt(Iterable<T> source, final Function1<Integer, T> closure) {
		return foldl(source, 0, new Function2<Integer, Integer, T>() {
			public Integer execute(Integer p1, T p2) {
				return p1 + closure.execute(p2);
			}});
	}

	public static <T> double sumDouble(Iterable<T> source, final Function1<Double, T> closure) {
		return foldl(source, 0.0, new Function2<Double, Double, T>() {
			public Double execute(Double p1, T p2) {
				return p1 + closure.execute(p2);
			}});
	}

	public static <R, P> Map<R, List<P>> groupBy(Iterable<P> source, final Function1<R, P> functor) {
		final Map<R, List<P>> groups = new HashMap<R, List<P>>();
		execute(source, new Function1<Void, P>() {
			@Override
			public Void execute(P p) {
				R val = functor.execute(p);
				List<P> group = groups.get(val);
				if(group == null) {
					group = new ArrayList<P>();
					groups.put(val, group);
				}
				group.add(p);
				return null;
			}});
		return groups;
	}

	public static <R, P> Map<R, P> mappedValues(Iterable<P> source, final Function1<R, P> functor) {
		final Map<R, P> groups = new HashMap<R, P>();
		execute(source, new Function1<Void, P>() {
			@Override
			public Void execute(P p) {
				R val = functor.execute(p);
				groups.put(val, p);
				return null;
			}});
		return groups;
	}
}
