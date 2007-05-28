package org.sodeja.context;

// TODO put with factory
public interface Context {
	public class Key<T> {
	}
	
	public <T> T get(Key<T> key);
	
	public <T> void put(Key<T> key, T value);
}
