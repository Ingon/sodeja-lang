package org.sodeja.functional;

public abstract class VFunction2<P1, P2> implements Function2<Void, P1, P2> {
	@Override
	public Void execute(P1 p1, P2 p2) {
		executeV(p1, p2);
		return null;
	}
	
	public abstract void executeV(P1 p1, P2 p2);
}
