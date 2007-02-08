package org.sodeja.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
