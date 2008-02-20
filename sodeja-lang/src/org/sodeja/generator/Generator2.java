package org.sodeja.generator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sodeja.functional.Maybe;

public class Generator2<T> implements Iterator<T> {
	
	private Maybe<T> cachedValue;
	private GeneratorFunction<T> fun;
	
	public Generator2(GeneratorFunction<T> fun) {
		this.fun = fun;
	}
	
	@Override
	public boolean hasNext() {
		readNextValue();
		return cachedValue.hasValue();
	}

	@Override
	public T next() {
		readNextValue();
		
		if(cachedValue.hasValue()) {
			T val = cachedValue.value();
			cachedValue = null;
			return val;
		}
		
		throw new NoSuchElementException("Stream finished");
	}

	private void readNextValue() {
		if(cachedValue == null) {
			cachedValue = fun.execute();
		}
		if(cachedValue == null) {
			throw new IllegalArgumentException("Generator function should always return a value");
		}
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
