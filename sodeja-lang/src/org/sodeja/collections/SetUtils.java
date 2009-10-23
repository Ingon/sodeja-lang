package org.sodeja.collections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Pair;

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

    public static Set<Pair<String, Object>> namedValuesToSet(Object... namedValues) {
		Set<Pair<String, Object>> values = new HashSet<Pair<String,Object>>();
		for(int i = 0; i < namedValues.length; i+=2) {
			values.add(Pair.of((String) namedValues[i], namedValues[i + 1]));
		}
		return values;
	}

	public static <T> T first(Set<T> set) {
		if(set.isEmpty()) {
			return null;
		}
		Iterator<T> ite = set.iterator();
		if(! ite.hasNext()) {
			return null;
		}
		return ite.next();
	}
}
