package org.sodeja.context;

public interface Context {
	public class Key<T> {
	}
	
	public <T> T get(Key<T> key);
	
	public <T> void put(Key<T> key, T value);
}
