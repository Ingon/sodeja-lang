package org.sodeja.generator;

import org.sodeja.collections.ConsList;
import org.sodeja.functional.Function0;
import org.sodeja.functional.Maybe;

public class Gen<T> extends ConsList<T> {

	private boolean generated = false;
	private GeneratorFunction<T> promise;

	public Gen(GeneratorFunction<T> promise) {
		Maybe<T> val = promise.execute();
		if(! val.hasValue()) {
			throw new IllegalArgumentException("Promise should generate at last one thing");
		}
		this.head = val.value();
		this.promise = promise;
	}

	public Gen(T value, GeneratorFunction<T> promise) {
		this.head = value;
		this.promise = promise;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Gen<T> tail() {
		if(generated) {
			return (Gen<T>) super.tail();
		}
		
		generated = true;
		
		Maybe<T> result = promise.execute();
		if(result.hasValue()) {
			this.tail = new Gen<T>(result.value(), promise);
		}
		return (Gen<T>) this.tail;
	}

	public GeneratorFunction<T> promise() {
		return promise;
	}
}
