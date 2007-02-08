package org.sodeja.functional;

public class IdentityFunction<T> implements Function1<T, T> {
//	public IdentityFunction(Class<T> instance) {
//		System.out.println("Using: " + instance);
//	}
//	
	public T execute(T p) {
		return p;
	}
}
