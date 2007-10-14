package org.sodeja.functional;

public abstract class VFunction0 implements Function0<Void> {

	@Override
	public final Void execute() {
		executeV();
		return null;
	}

	public abstract void executeV();
}
