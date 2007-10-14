package org.sodeja.functional;

public abstract class VFunction1<T> implements Function1<Void, T> {

	@Override
	public final Void execute(T p) {
		executeV(p);
		return null;
	}

	public abstract void executeV(T p);
}
