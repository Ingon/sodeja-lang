package org.sodeja.generator;

import org.sodeja.collections.ConsList;
import org.sodeja.functional.Maybe;

public class Generator<T> extends ConsList<T> {

	private boolean generated = false;
	private GeneratorFunction<T> promise;

	public Generator(GeneratorFunction<T> promise) {
		Maybe<T> val = promise.execute();
		if(! val.hasValue()) {
			throw new IllegalArgumentException("Promise should generate at last one thing");
		}
		this.head = val.value();
		this.promise = promise;
	}

	public Generator(T value, GeneratorFunction<T> promise) {
		this.head = value;
		this.promise = promise;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Generator<T> tail() {
		if(generated) {
			return (Generator<T>) super.tail();
		}
		
		generated = true;
		
		Maybe<T> result = promise.execute();
		if(result.hasValue()) {
			this.tail = new Generator<T>(result.value(), promise);
		}
		return (Generator<T>) this.tail;
	}

	public GeneratorFunction<T> promise() {
		return promise;
	}
}
