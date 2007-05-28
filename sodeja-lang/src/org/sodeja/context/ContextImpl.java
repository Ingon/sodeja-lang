package org.sodeja.context;

import java.util.HashMap;
import java.util.Map;

public class ContextImpl implements Context {
	
	private Map<Key<?>, Object> values;
	
	public ContextImpl() {
		values = new HashMap<Key<?>, Object>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Key<T> key) {
		return (T) values.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> void put(Key<T> key, T value) {
		values.put(key, value);
	}
}
