package org.sodeja.generator;

import org.sodeja.functional.Function0;
import org.sodeja.functional.Maybe;

public class Generator<T> {
	private final T value;
	
	private boolean generated = false;
	private Function0<Maybe<T>> promise;
	private Generator<T> next;
	
	public Generator(T value, Function0<Maybe<T>> promise) {
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
		generated = true;
		
		Maybe<T> result = promise.execute();
		if(result.hasValue()) {
			next = new Generator<T>(result.value(), promise);
		}
		return next;
	}
	
	public Function0<Maybe<T>> promise() {
		return promise;
	}
}
