package org.sodeja.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Function2;

public class MapUtils {
	private MapUtils() {
	}
	
	public static <T, K, V> List<T> map(Map<K, V> source, Function2<T, K, V> functor) {
		List<T> result = new ArrayList<T>();
		for(Map.Entry<K, V> entry : source.entrySet()) {
			result.add(functor.execute(entry.getKey(), entry.getValue()));
		}
		return result;
	}
	
	public static <T, R> R getWithCreate(Map<T, R> source, T key, Function1<R, T> functor) {
		R value = source.get(key);
		if(value == null) {
			value = functor.execute(key);
			source.put(key, value);
		}
		return value;
	}
	
	public static <P, R> R getWithUpdate(Map<P, R> source, P key, Function1<R, P> initFunctor, 
			Function2<R, P, R> updateFunctor) {
		
		R value = source.get(key);
		if(value == null) {
			value = initFunctor.execute(key);
			source.put(key, value);
			return value;
		}
		
		value = updateFunctor.execute(key, value);
		source.put(key, value);
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public static Map asMap(Object... vals) {
		if(vals.length % 2 != 0) {
			throw new IllegalArgumentException("The size of the array should be devidable by 2");
		}
		
		Map result = new HashMap();
		for(int i = 0;i < vals.length;i += 2) {
			result.put(vals[i], vals[i+1]);
		}
		return result;
	}
}
