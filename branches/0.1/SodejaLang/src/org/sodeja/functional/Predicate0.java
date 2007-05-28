package org.sodeja.functional;

public interface Predicate0 extends Function0<Boolean> {
	public static class Util {
		public Predicate0 alwaysTrue() {
			return new Predicate0() {
				public Boolean execute() {
					return true;
				}
			};
		}

		public Predicate0 alwaysFalse() {
			return new Predicate0() {
				public Boolean execute() {
					return false;
				}
			};
		}
	}
}
