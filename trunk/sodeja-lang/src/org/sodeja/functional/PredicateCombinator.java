package org.sodeja.functional;

public class PredicateCombinator {
	public Predicate0 and(final Predicate0... functors) {
		return new Predicate0() {
			public Boolean execute() {
				for(Predicate0 functor : functors) {
					if(! functor.execute()) {
						return false;
					}
				}
				return true;
			}
		};
	}

	public Predicate0 or(final Predicate0... functors) {
		return new Predicate0() {
			public Boolean execute() {
				boolean result = false;
				for(Predicate0 functor : functors) {
					result = result || functor.execute();
				}
				return result;
			}
		};
	}
	
	public <P> Predicate1<P> and(final Predicate1<P>... functors) {
		return new Predicate1<P>() {
			public Boolean execute(P p) {
				for(Predicate1<P> functor : functors) {
					if(! functor.execute(p)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	public <P> Predicate1<P> or(final Predicate1<P>... functors) {
		return new Predicate1<P>() {
			public Boolean execute(P p) {
				boolean result = false;
				for(Predicate1<P> functor : functors) {
					result = result || functor.execute(p);
				}
				return result;
			}
		};
	}
	
	public <P1, P2> Predicate2<P1, P2> and(final Predicate2<P1, P2>... functors) {
		return new Predicate2<P1, P2>() {
			public Boolean execute(P1 p1, P2 p2) {
				for(Predicate2<P1, P2> functor : functors) {
					if(! functor.execute(p1, p2)) {
						return false;
					}
				}
				return true;
			}
		};
	}
}
