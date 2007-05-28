package org.sodeja.functional;

public interface Predicate1<P> extends Function1<Boolean, P> {
	public static class Util {
		public <P> Predicate1<P> alwaysTrue() {
			return new Predicate1<P>() {
				public Boolean execute(P p) {
					return true;
				}
			};
		}

		public <P> Predicate1<P> alwaysFalse() {
			return new Predicate1<P>() {
				public Boolean execute(P p) {
					return false;
				}
			};
		}
	}
}
