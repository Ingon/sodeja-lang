package org.sodeja.lang;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {
	private final AtomicLong valueRef;
	
	public IDGenerator() {
		valueRef = new AtomicLong();
	}
	
	public IDGenerator(long initialValue) {
		valueRef = new AtomicLong(initialValue);
	}
	
	public long next() {
		return valueRef.getAndIncrement();
	}
}
