package org.sodeja.generator;

import org.sodeja.functional.Function0;

public class Generator<T> {
	private final T value;
	
	private boolean generated = false;
	private Function0<T> promise;
	private Generator<T> next;
	
	public Generator(T value, Function0<T> promise) {
		this.value = value;
		this.promise = promise;
	}
	
	public T value() {
		return value;
	}
	
	public Generator<T> next() {
		if(generated) {
			return next;
		}
		return new Generator<T>(promise.execute(), promise);
	}
	
	public Function0<T> promise() {
		return promise;
	}
}
