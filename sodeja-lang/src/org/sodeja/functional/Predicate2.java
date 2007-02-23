package org.sodeja.functional;

public interface Predicate2<P1, P2> extends Function2<Boolean, P1, P2> {
	public static class Util {
		public <P1, P2> Predicate2<P1, P2> alwaysTrue() {
			return new Predicate2<P1, P2>() {
				public Boolean execute(P1 p1, P2 p2) {
					return true;
				}
			};
		}

		public <P1, P2> Predicate2<P1, P2> alwaysFalse() {
			return new Predicate2<P1, P2>() {
				public Boolean execute(P1 p1, P2 p2) {
					return false;
				}
			};
		}
	}
}
